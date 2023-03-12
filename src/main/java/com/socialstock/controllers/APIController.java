package com.socialstock.controllers;
import com.socialstock.database.DBConnector;
import com.socialstock.logic.CandleTimes;
import com.socialstock.logic.Trading;
import com.socialstock.payment.PaymentLogic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.socialstock.clients.Account.accounts;
import static com.socialstock.logic.Trading.stocks;
import static com.socialstock.logic.Trading.times;

@RestController
@RequestMapping("/api/v1")
public class APIController {
    @GetMapping
    public String api() {
        return "This is API for the SocialStock exchange";
    }

    @GetMapping("/getPrice")
    public String getPrice(@RequestParam("symbol") String symbol) throws SQLException, ClassNotFoundException {
        return stocks.get(symbol).getPrice();
    }

    @GetMapping("/limit")
    public String limit(@RequestParam("amount") String amount,
                        @RequestParam("way") String way,
                        @RequestParam("symbol") String symbol,
                        @RequestParam("email") String email,
                        @RequestParam("price") String price) throws SQLException, ClassNotFoundException {
        if (!way.equals("buy") && !way.equals("sell")) {
            return "{'status': 'wayIsWrong'}";
        } else {
            //if (accounts.get(email).getBalance() < Float.parseFloat(amount)) {
                //    return "{'status': 'noMoney'}";
                //}
            stocks.get(symbol).limit(way, Float.parseFloat(amount), Float.parseFloat(price), email);
            System.out.println(stocks.get("IHC").getPrice());
            System.out.println(stocks.get("IHC").getGlass());
            return "{'status': 'success'}";
        }
    }

    @GetMapping("/market")
    public String market(@RequestParam("amount") String amount,
                         @RequestParam("way") String way,
                         @RequestParam("symbol") String symbol,
                         @RequestParam("email") String email) throws SQLException, ClassNotFoundException {
        if (!way.equals("buy") && !way.equals("sell")) {
            return "{'status': 'wayIsWrong'}";
        } else {
           //if (accounts.get(email).getBalance() < Float.parseFloat(amount)) {
           //    return "{'status': 'noMoney'}";
           //}
            stocks.get(symbol).market(way, Float.parseFloat(amount), email);
            System.out.println(getPrice("IHC"));
            System.out.println(stocks.get("IHC").getGlass());
            return "{'status': 'success'}";
        }
    }

    @GetMapping("/topUp")
    public String topUp(@RequestParam("amount") String amount,
                        @RequestParam("email") String email) throws IOException {
        return new PaymentLogic().topUp(Float.valueOf(amount), email);
    }

    @GetMapping("/withdraw")
    public String withdraw(@RequestParam("amount") String amount,
                           @RequestParam("email") String email) throws IOException {
        return null; //new PaymentLogic()(Float.valueOf(amount), email);
    }

    @GetMapping("/getSymbols")
    public String getSymbols() {
        var str = new StringBuilder();
        for (var i : stocks.entrySet()) {
            var s = i.getKey() + ":" + i.getValue().getPrice() + ",";
            str.append(s);
        }
        return str.substring(0, str.toString().length() - 1);
    }

    @GetMapping("/getCandles")
    public HashMap<String, List<List<Float>>> getCandles(@RequestParam("symbol") String symbol,
                                                         @RequestParam("minutes") String minutes) {
        HashMap<String, List<List<Float>>> mapping = new HashMap<>();
        mapping.put("result", times.get(symbol).get(minutes));
        return mapping;
    }

    @GetMapping("/getBalance")
    public Float getBalance(@RequestParam("email") String email) {
        return accounts.get(email) == null ? -1 : accounts.get(email).getBalance();
    }

    @GetMapping("/getBalanceList")
    public String getBalanceList(@RequestParam("email") String email) {
        var str = accounts.get(email).getBalanceMap();
        return accounts.get(email) == null ? "" : str.substring(0, str.length() - 1);
    }
}
