package com.socialstock.jsondb;

import java.util.HashMap;
import java.util.Map;

public class Table {
    public Map<String, Object> getMap() {
        return map;
    }

    private String name;
    private Map<String, Object> map = new HashMap<>();
    public Table(String name) {
        this.name = name;
    }
    public void addKeys(Map<String, Class<?>> addMap) {
        for (var i : addMap.entrySet()) {
            map.putIfAbsent(i.getKey(), i.getValue());
        }
    }
    public void addKey(String key, Object value) {
        map.putIfAbsent(key, value);
    }
    public void deleteKey(String key) {
        if (map.get(key) != null) {
            map.remove(key);
        }
    }
}
