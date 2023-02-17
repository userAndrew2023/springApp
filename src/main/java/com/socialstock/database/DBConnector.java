package com.socialstock.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;

    public static void Connection() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:symbols.s3db");
    }

    public static void CreateDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'symbols' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'symbol' text);");

    }
    public static void CreateDBUsers() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'email' text, 'password' text);");

    }
    public static void CreateDBClients() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'clients' " +
                "('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'email' text, 'balance' text, 'investments' text);");

    }
    public static void WriteDB() throws SQLException
    {
        statement.execute("INSERT INTO 'symbols' ('name', 'symbol') VALUES ('MrBeast', 'MRBST'); ");
        statement.execute("INSERT INTO 'symbols' ('name', 'symbol') VALUES ('Invest House', 'IHC'); ");
        statement.execute("INSERT INTO 'symbols' ('name', 'symbol') VALUES ('A4', 'VBMG'); ");
    }
    public static void WriteDBUsers(String email, String password) throws SQLException
    {
        statement.execute("INSERT INTO 'users' ('email', 'password') VALUES ('" +
                email + "', '" + password + "'); ");
    }
    public static void WriteDBClients(String email, String balance, String investments) throws SQLException
    {
        statement.execute("INSERT INTO 'clients' ('email', 'password', 'investments') VALUES ('" +
                email + "', '" + "0" + "', '" + investments + "'); ");
    }
    public static void topUpOrWithdraw(String balance) throws SQLException {
        statement.execute("INSERT INTO 'clients' ('balance') VALUES ('" + balance + "')'");
    }
    public static List<String> ReadDB() throws ClassNotFoundException, SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM symbols");
        List<String> list = new ArrayList<>();
        while(resultSet.next())
        {
            String symbol = resultSet.getString("symbol");
            list.add(symbol);
        }
        return list;
    }
    public static boolean ReadDBUsers(String email, String password) throws ClassNotFoundException, SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM users");
        while(resultSet.next())
        {
            String email1 = resultSet.getString("email");
            String password1 = resultSet.getString("password");
            if (email1.equals(email)) {
                if (password.equals(password1)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean CheckE(String email) throws ClassNotFoundException, SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM users");
        while(resultSet.next())
        {
            String email1 = resultSet.getString("email");
            if (email1.equals(email)) {
                return true;
            }
        }
        return false;
    }
    public static String ReadDBBalance(String email) throws ClassNotFoundException, SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM clients");
        while(resultSet.next())
        {
            String email1 = resultSet.getString("email");
            if (email1.equals(email)) {
                return resultSet.getString("balance");
            }
        }
        return null;
    }
    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        statement.close();
        try {
            resultSet.close();
        } catch (Exception ignored) {}
        connection.close();
    }

}
