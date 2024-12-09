package spring.alotra.config;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrency(double amount) {
        Locale localeVN = new Locale("vi", "VN"); // Định dạng tiền Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormat.format(amount);
    }
}
