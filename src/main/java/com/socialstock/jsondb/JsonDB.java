package com.socialstock.jsondb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonDB {
    private String name;
    private Table table;
    public JsonDB(String name) {
        this.name = name;
    }
    public void setTable(Table table) {
        this.table = table;
    }
    public void write(String key, Object value) {
        if (table.getMap().get(key) != null) {
            Gson gson = new GsonBuilder().create();
            try(FileWriter writer = new FileWriter("notes3.txt", true))
            {
                writer.append(gson.toJson(table));
                writer.flush();
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
            gson.toJson(table);
        }
    }
    public void read() {

    }
    public void update() {

    }
    public void delete() {

    }
}
