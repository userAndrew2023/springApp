package com.socialstock.logic;

import java.util.*;

public class Trading {
    public List<List<String>> getGlass() {
        return glass;
    }
    private final List<List<String>> glass = new ArrayList<>();
    public static Map<String, Map<String, List<List<Float>>>> times = new HashMap<>();
    public static HashMap<String, Trading> stocks = new HashMap<>();
    private float currentPrice = 0.5F;
    private String name;
    private String symbol;

    public void run() {
        for (CandleTimes i : CandleTimes.values()) {
            candleTime(i);
        }
    }
    public Trading(String name, String symbol, Float listingPrice) {
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = listingPrice;
    }

    public void market(String longOrShort, float amount, String email) {
        sort();
        int buyCenter = 0;
        int sellCenter = 0;
        for (int i = 0; i < glass.size(); i++) {
            try {
                if (glass.get(i).get(0).equals("buy") && glass.get(i + 1).get(0).equals("sell")) {
                    buyCenter = i;
                    sellCenter = i + 1;
                    break;
                }
            } catch (IndexOutOfBoundsException ignored) {}

        }
        if (longOrShort.equals("buy")) {
            if (amount > Float.parseFloat(glass.get(sellCenter).get(1))) {
                var a = amount - Float.parseFloat(glass.get(sellCenter).get(1));
                currentPrice = Float.parseFloat((glass.get(sellCenter).get(2)));
                glass.remove(sellCenter);
                // Пробили маркетным ордером, идем дальше искать, где купить
                market("buy", a, email);
            } else if (amount < Float.parseFloat(glass.get(sellCenter).get(1))) {
                var a = Float.parseFloat(glass.get(sellCenter).get(1)) - amount;
                currentPrice = Float.parseFloat((glass.get(sellCenter).get(2)));
                limit("sell", a, currentPrice, email);
                glass.remove(sellCenter);
            } else if (amount == Float.parseFloat(glass.get(sellCenter).get(1))) {
                currentPrice = Float.parseFloat(glass.get(sellCenter).get(2));
                glass.remove(sellCenter);
            }
        } else {
            if (amount > Float.parseFloat(glass.get(buyCenter).get(1))) {
                var a = amount - Float.parseFloat(glass.get(buyCenter).get(1));
                currentPrice = Float.parseFloat((glass.get(buyCenter).get(2)));
                glass.remove(buyCenter);
                // Пробили маркетным ордером, идем дальше искать, где купить
                market("sell", a, email);
            } else if (amount < Float.parseFloat(glass.get(buyCenter).get(1))) {
                var a = Float.parseFloat(glass.get(buyCenter).get(1)) - amount;
                currentPrice = Float.parseFloat((glass.get(buyCenter).get(2)));
                limit("buy", a, currentPrice, email);
                glass.remove(buyCenter);
                // Продали, но не пробили, разъедаем
            } else if (amount == Float.parseFloat(glass.get(sellCenter).get(1))){
                currentPrice = Float.parseFloat(glass.get(buyCenter).get(2));
                glass.remove(buyCenter);
            }
        }
    }

    public void limit(String longOrShort, float amount, float price, String email) {
        if (longOrShort.equals("buy")) {
            if (price > currentPrice) {
                market("buy", amount, email);
                return;
            }
            glass.add(Arrays.asList("buy", String.valueOf(amount), String.valueOf(price), email));
        } else {
            if (price < currentPrice) {
                market("sell", amount, email);
                return;
            }
            glass.add(Arrays.asList("sell", String.valueOf(amount), String.valueOf(price), email));
        }
        sort();
    }

    public String getPrice() {
        return String.valueOf(currentPrice);
    }

    private void sort() {
        glass.sort((o1, o2) -> Float.compare(Float.parseFloat(o1.get(2)), Float.parseFloat(o2.get(2))));
    }
    public void candleTime(CandleTimes name) {
        new Thread(() -> {
            long minutes = 0;
            switch (name) {
                case M1 -> minutes = 1;
                case M5 -> minutes = 5;
                case M15 -> minutes = 15;
                case H1 -> minutes = 60;
                case H4 -> minutes = 60 * 4;
                case D1 -> minutes = 60 * 4 * 6;
                case W1 -> minutes = 60 * 4 * 6 * 7;
                case MO1 -> minutes = 60 * 4 * 6 * 7 * 30;
            }
            List<Float> list;
            float start = Float.parseFloat(getPrice());
            float high = start;
            float low = start;
            int count = 0;
            while (count <= 60 * minutes) {
                try {
                    if (count == 0) {
                        Map<String, List<List<Float>>> m = new HashMap<>();
                        m.put(String.valueOf(minutes), new ArrayList<>());
                        times.putIfAbsent(symbol, m);
                        times.get(symbol).putIfAbsent(String.valueOf(minutes), new ArrayList<>());
                        list = List.of(start, start, start, start);
                        times.get(symbol).get(String.valueOf(minutes)).add(list);
                    } else {
                        float price = Float.parseFloat(getPrice());
                        if (price > high) {
                            high = price;
                        } else if (Float.parseFloat(getPrice()) < low) {
                            low = Float.parseFloat(getPrice());
                        }
                        float end = Float.parseFloat(getPrice());
                        list = List.of(high, low, start, end);
                        times.get(symbol).get(String.valueOf(minutes)).set(times.get(symbol).get(String.valueOf(minutes)).size() - 1, list);
                    }
                    count += 1;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            candleTime(name);
        }).start();
    }
    public List<List<Float>> getToCandle(String name, String mins) {
        return times.get(name).get(mins);
    }
}
