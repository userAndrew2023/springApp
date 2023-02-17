package com.socialstock.clients;

import com.socialstock.database.DBConnector;

import java.sql.SQLException;

public class Authorization {
    public boolean register(String email, String password) throws SQLException, ClassNotFoundException {
        if (!email.contains("@") || !email.contains(".") || password.length() < 8) {
            return false;
        } else {
            DBConnector.Connection();
            DBConnector.CreateDBUsers();
            if (!DBConnector.CheckE(email)) {
                DBConnector.WriteDBUsers(email, password);
                DBConnector.CloseDB();
                return true;
            } else {
                return false;
            }
        }
    }
    public boolean login(String email, String password) throws SQLException, ClassNotFoundException {
        DBConnector.Connection();
        DBConnector.CreateDBUsers();
        if (DBConnector.ReadDBUsers(email, password)) {
            DBConnector.CloseDB();
            return true;
        }
        DBConnector.CloseDB();
        return false;
    }
}
