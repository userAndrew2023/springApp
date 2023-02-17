package com.socialstock.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trading {
    private static final List<List<String>> glass = new ArrayList<>();
    private static int currentPrice = 120;
    public static void market(String longOrShort, int amount) {
        System.out.println(amount);
        int buyCenter = 0;
        int sellCenter = 0;
        for (int i = 0; i < glass.size(); i++) {
            try {
                if (glass.get(i).get(0).equals("Sell") && glass.get(i + 1).get(0).equals("Buy")) {
                    buyCenter = i + 1;
                    sellCenter = i;
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Первая заявка в стакане!");
            }

        }
        if (longOrShort.equals("Buy")) {
            if (amount > Integer.parseInt(glass.get(sellCenter).get(1))) {
                var a = amount - Integer.parseInt(glass.get(sellCenter).get(1));
                currentPrice = Integer.parseInt((glass.get(sellCenter).get(2)));
                glass.remove(sellCenter);
                System.out.println(currentPrice);
                // Пробили маркетным ордером, идем дальше искать, где купить
                market("Buy", a);
            } else if (amount < Integer.parseInt(glass.get(sellCenter).get(1))) {
                var a = Integer.parseInt(glass.get(sellCenter).get(1)) - amount;
                glass.get(sellCenter).set(1, String.valueOf(a));
                market("Sell", a);

                // Купили, но не пробили, разъедаем
            } else if (amount == Integer.parseInt(glass.get(sellCenter).get(1))) {
                glass.remove(sellCenter);
                currentPrice = Integer.parseInt(glass.get(buyCenter - 1).get(2));
                // Взаимно
            }
        } else {
            if (amount > Integer.parseInt(glass.get(buyCenter).get(1))) {
                var a = amount - Integer.parseInt(glass.get(buyCenter).get(1));
                currentPrice = Integer.parseInt((glass.get(buyCenter).get(2)));
                glass.remove(buyCenter);
                System.out.println(currentPrice);
                // Пробили маркетным ордером, идем дальше искать, где купить
                market("Sell", a);
            } else if (amount < Integer.parseInt(glass.get(buyCenter).get(1))) {
                var a = Integer.parseInt(glass.get(buyCenter).get(1)) - amount;
                glass.get(buyCenter).set(1, String.valueOf(a));
                market("Buy", a);
                // Продали, но не пробили, разъедаем
            } else if (amount == Integer.parseInt(glass.get(buyCenter).get(1))){
                currentPrice = Integer.parseInt(glass.get(buyCenter - 1).get(2));
                glass.remove(buyCenter);
            }
        }
        System.out.println(currentPrice);
    }

    public static void limit(String longOrShort, int amount, int price) {
        if (longOrShort.equals("Buy")) {
            if (price > currentPrice) {
                market("Buy", amount);
            }
            glass.add(Arrays.asList("Buy", String.valueOf(amount), String.valueOf(price)));
        } else {
            if (price < currentPrice) {
                market("Sell", amount);
            }
            glass.add(Arrays.asList("Sell", String.valueOf(amount), String.valueOf(price)));
        }
        System.out.println(glass);
        System.out.println(currentPrice);
    }

    public static String getPrice() {
        return String.valueOf(currentPrice);
    }
}
