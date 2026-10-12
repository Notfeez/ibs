package ru.edu.pr01;

public class SelfCheck {
    private static int failed = 0;

    private static void check(String name, boolean cond) {
        System.out.println((cond ? "PASS  " : "FAIL  ") + name);
        if (!cond) failed++;
    }

    private static boolean near(double a, double b) { return Math.abs(a - b) < 0.001; }

    public static void main(String[] args) {
        boolean ok = false;
        try { ok = (OrderCalculator.calculateBase(2, 100.0) == 200.0 && Math.abs(OrderCalculator.calculateTotal(2,100.0,10.0,20.0)-216.0)<0.001); } catch (Exception e) { System.out.println("FAIL exception " + e.getMessage()); }
        check("исходная проверка (2 x 100, -10%, НДС 20 = 216)", ok);

        check("типовое: 12 x 2490.50, -5%, НДС 20 = 34070.04", near(OrderCalculator.calculateTotal(12, 2490.50, 5, 20), 34_070.04));
        check("скидка — процент, а не сумма", near(OrderCalculator.applyDiscount(1000, 10), 900));
        check("НДС считается после скидки", near(OrderCalculator.calculateVat(900, 20), 180));

        check("граница: quantity=1 допустимо", OrderCalculator.isValid(1, 100, 0));
        check("граница: quantity=0 недопустимо", !OrderCalculator.isValid(0, 100, 0));
        check("граница: quantity=10000 допустимо", OrderCalculator.isValid(10_000, 100, 0));
        check("граница: quantity=10001 недопустимо", !OrderCalculator.isValid(10_001, 100, 0));

        check("граница: цена 0 недопустима", !OrderCalculator.isValid(1, 0, 0));
        check("граница: цена 0.01 допустима", OrderCalculator.isValid(1, 0.01, 0));
        check("граница: цена 5 000 000 допустима", OrderCalculator.isValid(1, 5_000_000, 0));
        check("граница: цена 5 000 000.01 недопустима", !OrderCalculator.isValid(1, 5_000_000.01, 0));
        check("отрицательная цена недопустима", !OrderCalculator.isValid(5, -20, 5));
        
        check("граница: скидка 0 допустима", OrderCalculator.isValid(1, 100, 0));
        check("граница: скидка 30 допустима", OrderCalculator.isValid(1, 100, 30));
        check("граница: скидка 30.01 недопустима", !OrderCalculator.isValid(1, 100, 30.01));
        check("граница: скидка -0.01 недопустима", !OrderCalculator.isValid(1, 100, -0.01));
        check("скидка 35 (sample.csv) недопустима", !OrderCalculator.isValid(3, 999.99, 35));
        
        check("NaN в цене недопустим", !OrderCalculator.isValid(1, Double.NaN, 0));
        check("Infinity в цене недопустима", !OrderCalculator.isValid(1, Double.POSITIVE_INFINITY, 0));

        check("пустая строка -> null", OrderCalculator.parseInt("") == null && OrderCalculator.parseDouble("  ") == null);
        check("null -> null", OrderCalculator.parseInt(null) == null);
        check("неверный формат 'abc' -> null", OrderCalculator.parseDouble("abc") == null && OrderCalculator.parseInt("1.5") == null);
        check("запятая как разделитель '99,5'", near(OrderCalculator.parseDouble("99,5"), 99.5));
        check("'NaN' как текст отвергается", OrderCalculator.parseDouble("NaN") == null);

        check("округление до копеек", near(OrderCalculator.calculateTotal(3, 0.01, 0, 20), 0.04));

        System.out.println(failed == 0 ? "ALL PASS" : "FAILED: " + failed);
        if (failed != 0) System.exit(1);
    }
}
