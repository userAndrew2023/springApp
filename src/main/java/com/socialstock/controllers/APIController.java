package com.socialstock.controllers;
import com.socialstock.database.DBConnector;
import com.socialstock.logic.Trading;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class APIController {
    @GetMapping
    public String api() {
        return "This is API for the SocialStock exchange";
    }
    @GetMapping("/getPrice")
    public String getPrice(@RequestParam("symbol") String symbol) throws SQLException, ClassNotFoundException {
        DBConnector.Connection();
        DBConnector.CreateDB();
        List<String> symbols = DBConnector.ReadDB();
        DBConnector.CloseDB();
        if (symbols.contains(symbol)) {
            return Trading.getPrice();
        }
        return "Wrong symbol";
    }
    @GetMapping("/limit")
    public String limit(@RequestParam("way") String way,
                        @RequestParam("amount") String amount,
                        @RequestParam("price") String price,
                        @RequestParam("email") String email) throws SQLException, ClassNotFoundException {
        if (!way.equals("Buy") && !way.equals("Sell")) {
            return "{'status': 'wayIsWrong'}";
        } else {
            DBConnector.Connection();
            DBConnector.CreateDBClients();
            if (Integer.parseInt(DBConnector.ReadDBBalance(email)) <
                    (Integer.parseInt(amount)) * Integer.parseInt(price)) {
                return "'status': 'noMoney'";
            }
            Trading.limit(way, Integer.parseInt(amount), Integer.parseInt(price));
            return "{'status': 'success'}";
        }
    }
    @GetMapping("/market")
    public String market(@RequestParam("way") String way,
                         @RequestParam("amount") String amount,
                         @RequestParam("amount") String email) throws SQLException, ClassNotFoundException {
        if (!way.equals("Buy") && !way.equals("Sell")) {
            return "{'status': 'wayIsWrong'}";
        } else {
            DBConnector.Connection();
            DBConnector.CreateDBClients();
            if (Integer.parseInt(DBConnector.ReadDBBalance(email)) <
                    (Integer.parseInt(amount)) * Integer.parseInt(Trading.getPrice())) {
                return "'status': 'noMoney'";
            }
            Trading.market(way, Integer.parseInt(amount));
            return "{'status': 'success'}";
        }
    }
}
