package com.socialstock.database;

import java.sql.SQLException;

public class DBStarter {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBConnector.Connection();
        DBConnector.CreateDB();
        DBConnector.WriteDB();
        System.out.println(DBConnector.ReadDB());
        DBConnector.CloseDB();
    }
}
