package com.asrevo.cloud.ecs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class EcsConfigClientPropertySourceLocator implements PropertySourceLocator {

    private final EcsConfigProperties properties;
    private final S3Client s3Client;

    public EcsConfigClientPropertySourceLocator(EcsConfigProperties properties) {
        this.properties = properties;
        this.s3Client = S3Client.builder().region(Region.of(properties.getRegion())).build();
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        if (environment instanceof ConfigurableEnvironment env) {
            CompositePropertySource composite = new CompositePropertySource("composite-ecs");
            properties.getPaths().forEach(path -> {
                URI s3Uri = getS3Uri(path);
                if (s3Uri != null && s3Uri.getScheme().equals("s3"))
                    composite.addFirstPropertySource(getMapPropertySource(s3Uri, env));

            });
            return composite;
        }
        return null;
    }

    URI getS3Uri(String path) {
        try {
            return new URI(path);
        } catch (URISyntaxException e) {
            log.error("invalid path " + path);
        }
        return null;
    }

    private PropertySource<?> getMapPropertySource(URI s3Uri, ConfigurableEnvironment env) {
        String path = s3Uri.getPath();
        if (path.startsWith("/")) path = path.substring(1);
        Yaml yaml = new Yaml(new Constructor(HashMap.class));
        Map<String, Object> load = yaml.load(s3Client.getObjectAsBytes(GetObjectRequest.builder()
                        .bucket(s3Uri.getHost()).key(path)
                .build()).asInputStream());
        return new MapPropertySource(s3Uri.toString(), convert("", load));
    }

    private static HashMap<String, Object> convert(String prefix, Object main) {
        if (main instanceof HashMap) {
            HashMap<String, Object> value = new HashMap<>();
            for (Map.Entry<Object, Object> entry : ((HashMap<Object, Object>) main).entrySet()) {
                String newPrefix = prefix;
                if (!prefix.isEmpty()) newPrefix = prefix + ".";
                value.putAll(convert(newPrefix + entry.getKey(), entry.getValue()));
            }
            return value;

        } else if (main instanceof ArrayList) {
            HashMap<String, Object> value = new HashMap<>();
            ArrayList<Object> list = ((ArrayList<Object>) main);
            for (int i = 0; i < list.size(); i++) {
                value.putAll(convert(prefix + "[" + i + "]", list.get(i)));
            }
            return value;
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put(prefix, main);
            return map;
        }
    }

}
