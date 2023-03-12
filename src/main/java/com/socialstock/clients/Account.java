package com.socialstock.clients;

import com.socialstock.database.DBConnector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account {
    public static Map<String, Account> accounts = new HashMap<>();
    private String email;
    private String password;
    private float balance;
    public Map<String, Float> balanceMap = new HashMap<>();

    public Account(String email, String password, float balance) {
        this.email = email;
        this.balance = balance;
        this.password = password;
        balanceMap.put("home", 0F);
        balanceMap.put("spot", 0F);
        balanceMap.put("margin", 0F);
        balanceMap.put("divs", 0F);
    }

    public Float getBalance() {
        return balance;
    }

    public boolean topUp(Float amount) {
        balance++;
        return true;
    }

    public void transfer(Float amount, String where, String to) {
        if (balanceMap.get(where) >= amount) {
            balanceMap.put(where, balanceMap.get(where) - amount);
            balanceMap.put(to, balanceMap.get(to) + amount);
        }
    }

    public boolean withdraw(Float amount) {
        if (amount <= balance) {
            balance--;
            return true;
        }
        return false;
    }

    public boolean equalsPassword(String password) {
        return password.equals(this.password);
    }

    public String getBalanceMap() {
        StringBuilder toRet = new StringBuilder();
        for (var i : balanceMap.entrySet()) {
            toRet.append(i.getValue()).append(",");
        }

        // Возвращает баланс кошелька

        return toRet.toString();
    }
}
