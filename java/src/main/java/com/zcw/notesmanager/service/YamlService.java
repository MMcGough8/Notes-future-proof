package com.zcw.notesmanager.service;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.LinkedHashMap;
import java.util.Map;

public class YamlService {

    private final Yaml yaml;
    private static final int MAX_YAML_SIZE = 100000;

    public YamlService() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        options.setWidth(80);
        
        this.yaml = new Yaml(options);
    }

    public Map<String, Object> parseYaml(String yamlContent) {

        if (yamlContent == null || yamlContent.trim().isEmpty()) {
            return new LinkedHashMap<>();
        }
        
        if (yamlContent.length() > MAX_YAML_SIZE) {
            throw new IllegalArgumentException(
                String.format("YAML content too large: %d bytes (max: %d bytes)", 
                    yamlContent.length(), MAX_YAML_SIZE)
            );
        }
        
        try {
            Map<String, Object> result = yaml.load(yamlContent);
            
            return result != null ? result : new LinkedHashMap<>();
            
        } catch (YAMLException e) {
            throw new YAMLException("Failed to parse YAML: " + e.getMessage(), e);
        }
    }

    public String writeYaml(Map<String, Object> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        if (data.isEmpty()) {
            return "";
        }
        
        try {
            String dumped = yaml.dump(data);
            
            if (dumped.startsWith("---\n")) {
                return dumped.substring(4);
            } else if (dumped.startsWith("---")) {
                return dumped.substring(3);
            }
            
            return dumped;
            
        } catch (YAMLException e) {
            throw new YAMLException("Failed to write YAML: " + e.getMessage(), e);
        }
    }
}