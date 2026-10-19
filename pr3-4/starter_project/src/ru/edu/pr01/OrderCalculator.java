package ru.edu.pr01;

import java.util.Locale;
import java.util.Scanner;

public class OrderCalculator {

    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10_000;
    public static final double MAX_UNIT_PRICE = 5_000_000.0;
    public static final double MIN_DISCOUNT = 0.0;
    public static final double MAX_DISCOUNT = 30.0; 
    public static final double VAT_RATE = 20.0;
    private static final Locale RU = Locale.forLanguageTag("ru-RU");

    public static boolean isValid(int quantity, double unitPrice, double discountPercent) {
        return describeError(quantity, unitPrice, discountPercent) == null;
    }

    public static String describeError(int quantity, double unitPrice, double discountPercent) {
        if (!(quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY)) {
            return "Количество должно быть от " + MIN_QUANTITY + " до " + MAX_QUANTITY + " (получено " + quantity + ")";
        }
        if (!(unitPrice > 0 && unitPrice <= MAX_UNIT_PRICE)) {
            return "Цена должна быть больше 0 и не более " + (long) MAX_UNIT_PRICE + " руб. (получено " + unitPrice + ")";
        }
        if (!(discountPercent >= MIN_DISCOUNT && discountPercent <= MAX_DISCOUNT)) {
            return "Скидка должна быть от " + (int) MIN_DISCOUNT + " до " + (int) MAX_DISCOUNT + " % (получено " + discountPercent + ")";
        }
        return null;
    }

    public static double calculateBase(int quantity, double unitPrice) {
        return quantity * unitPrice;
    }

    public static double applyDiscount(double base, double discountPercent) {
        return base * (1.0 - discountPercent / 100.0);
    }

    public static double calculateVat(double discounted, double vatPercent) {
        return discounted * vatPercent / 100.0;
    }

    public static double calculateTotal(int quantity, double unitPrice, double discountPercent, double vatPercent) {
        double base = calculateBase(quantity, unitPrice);
        double discounted = round2(applyDiscount(base, discountPercent));
        double vat = round2(calculateVat(discounted, vatPercent));
        return round2(discounted + vat);
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static String rub(double value) {
        return String.format(RU, "%.2f руб.", value);
    }

    static Integer parseInt(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Double parseDouble(String s) {
        try {
            if (s == null || s.isBlank()) return null;
            double v = Double.parseDouble(s.trim().replace(',', '.'));
            return (Double.isNaN(v) || Double.isInfinite(v)) ? null : v;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static void process(String q, String p, String d) {
        Integer quantity = parseInt(q);
        Double price = parseDouble(p);
        Double discount = parseDouble(d);
        if (quantity == null || price == null || discount == null) {
            System.out.println("Ошибка: значение пустое или имеет неверный формат (" + q + "; " + p + "; " + d + "). Расчёт не выполнен.");
            return;
        }
        String error = describeError(quantity, price, discount);
        if (error != null) {
            System.out.println("Ошибка: " + error + ". Расчёт не выполнен.");
            return;
        }
        double base = calculateBase(quantity, price);
        double discounted = round2(applyDiscount(base, discount));
        double vat = round2(calculateVat(discounted, VAT_RATE));
        double total = calculateTotal(quantity, price, discount, VAT_RATE);
        System.out.println("Базовая сумма:  " + rub(base));
        System.out.println("После скидки:   " + rub(discounted));
        System.out.println("НДС " + (int) VAT_RATE + "%:        " + rub(vat));
        System.out.println("Итого:          " + rub(total));
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.print("Количество: ");
        String q = in.hasNextLine() ? in.nextLine() : "";
        System.out.print("Цена за единицу, руб.: ");
        String p = in.hasNextLine() ? in.nextLine() : "";
        System.out.print("Скидка, %: ");
        String d = in.hasNextLine() ? in.nextLine() : "";
        process(q, p, d);
    }
}
