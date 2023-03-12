package com.socialstock.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.socialstock.database.DBConnector;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.socialstock.clients.Account.accounts;

public class Authorization {
    public boolean register(String email, String password) throws SQLException, ClassNotFoundException, JsonProcessingException {
        if (email.contains("@") && email.contains(".") && password.length() >= 8 && accounts.get(email) == null) {
            accounts.put(email, new Account(email, password, 0));
            return true;
        }
        return false;
    }
    public boolean login(String email, String password) {
        return accounts.get(email).equalsPassword(password);
    }
}
