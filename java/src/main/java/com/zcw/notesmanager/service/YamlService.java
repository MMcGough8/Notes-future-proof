package com.zcw.notesmanager.service;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

public class YamlService {

    private final Yaml yaml;

    public YamlService() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        this.yaml = new Yaml(options);
    }

    public Map<String, Object> parseYaml(String yamlContent) {
        if (yamlContent == null) {
            throw new IllegalArgumentException("YAML content cannot be null");
        }
        if (yamlContent.trim().isEmpty()) {
            return null;
        }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = yaml.load(yamlContent);
            return result != null ? result : new LinkedHashMap<>();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse YAML: " + e.getMessage(), e);
        }
    }

    public String writeYaml(Map<String, Object> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (data.isEmpty()) {
            return "";
        }

        String dumped = yaml.dump(data);

        if (dumped.startsWith("---\n")) {
            dumped = dumped.substring(4);
        } else if (dumped.startsWith("---")) {
            dumped = dumped.substring(3);
        }

        return dumped;
    }
    public Map<String, Object> readYaml(String yamlContent) {
        return yaml.load(yamlContent);
    }
}