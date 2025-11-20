package com.zcw.notesmanager.service;

import org.yaml.snakeyaml.Yaml;
import java.util.Map;

public class YamlService {
    
    private final Yaml yaml;
    
    public YamlService() {
        this.yaml = new Yaml();
    }
    
    public Map<String, Object> parseYaml(String yamlContent) {
        return yaml.load(yamlContent);
    }
}
