package ru.edu.pr02;

/**
 * Все пороги и числовые правила табеля собраны здесь,
 * чтобы при смене требования правилось одно место.
 * Вариант 6: недельная норма = 35 часов.
 */
public final class TimesheetRules {
    public static final int DAYS_COUNT = 5;      // дней в рабочей неделе
    public static final int MIN_HOURS = 0;       // нижняя граница за день (включительно)
    public static final int MAX_HOURS = 16;      // верхняя граница за день (включительно)
    public static final int DAILY_NORM = 8;      // больше этого значения - переработка за день
    public static final int WEEKLY_NORM = 35;    // недельная норма (вариант 6, было 40)

    private TimesheetRules() {
    }
}
