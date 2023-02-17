package com.socialstock.clients;

import com.socialstock.database.DBConnector;
import com.socialstock.logic.Stock;

import java.sql.SQLException;
import java.util.List;

public class Account {
    private String email;
    private float balance;
    private List<Stock> investments;

    public Account(String email, float balance, List<Stock> investments) {
        this.email = email;
        this.balance = balance;
        this.investments = investments;
    }

    public boolean topUp(int amount) throws SQLException, ClassNotFoundException {
        var paySuccess = true;
        if (paySuccess) {
            DBConnector.Connection();
            DBConnector.CreateDBClients();
            int balanceNow = Integer.parseInt(DBConnector.ReadDBBalance(email)) + amount;
            DBConnector.topUpOrWithdraw(String.valueOf(balanceNow));
            DBConnector.CloseDB();
            return true;
        }
        return false;
    }

    public boolean withdraw(int amount) throws SQLException, ClassNotFoundException {
        var paySuccess = true;
        if (paySuccess) {
            DBConnector.Connection();
            DBConnector.CreateDBClients();
            int balanceNow = Integer.parseInt(DBConnector.ReadDBBalance(email)) - amount;
            DBConnector.topUpOrWithdraw(String.valueOf(balanceNow));
            DBConnector.CloseDB();
            return true;
        }
        return false;
    }

}
