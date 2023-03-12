package com.socialstock.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socialstock.clients.Account;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class PaymentLogic {
    public static Long id = 1000000L;

    private final String PAYOUT_API = "CKNgg3jpVU9rWHd9ILFZZdPH6ONvyX06NOcwHTdiJEsO1hRi5wHBCo6PVkoH5" +
            "OGwi9vqYpnPm2iCBrwWkt3jdm7y9aC9nVv46KtdakHClHFt31khLiqkRYSkfMRsJ5pd";
    private final String MERCHANT_API = "DM0XM6vdpbTDBJlEjSkOjU5tyEG4hHVuOes8liIRplZkIPNOXQNoRTfUhYST2HWH" +
            "z7W9ZlfKutEDDjx3Py5wENR4tCnAhVenSL15LL41eGtIc6Tq2a9ToBJxbt5SINmj";
    private final String MERCHANT_ID = "769678a9-2ded-4699-9732-691d479622b8";
    public String topUp(Float amount, String email) throws IOException {
        id++;

        String data =
                "{\"amount\": \"" + amount.toString() + "\"," +
                        "\"currency\": \"USD\"," +
                        "\"order_id\": \"" + id.toString() + "\"}";
        String encodedBytes = Base64.getEncoder().encodeToString(data.getBytes());
        String MD5 = getMD5(encodedBytes + MERCHANT_API);
        URL url = new URL("https://api.cryptomus.com/v1/payment");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("merchant", MERCHANT_ID);
        con.setRequestProperty("sign", MD5);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        String res = "";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            res = response.toString();
        }
        new Thread(() -> {
            var c = 0;
            var new_id = id;
            while (c != 60) {
                try {
                    if (checkTopUp(new_id.toString()).equals("paid")) {
                        Account.accounts.get(email).topUp(amount);
                        break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                c++;
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        HashMap<String, HashMap<String, String>> mapping = new ObjectMapper().readValue(res.toString(), HashMap.class);
        return mapping.get("result").get("url");
    }
    public String checkTopUp(String order) throws IOException {
        String data = "{\"order_id\": \"" + order + "\"}";
        String encodedBytes = Base64.getEncoder().encodeToString(data.getBytes());
        String MD5 = getMD5(encodedBytes + MERCHANT_API);
        URL url = new URL("https://api.cryptomus.com/v1/payment/info");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("merchant", MERCHANT_ID);
        con.setRequestProperty("sign", MD5);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }
        String resp = "";
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            resp = response.toString();
        }
        HashMap<String, HashMap<String, String>> mapping = new ObjectMapper().readValue(resp, HashMap.class);
        return mapping.get("result").get("status");
    }
    private String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
