package com.codefresher.services;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component("calculateService")
public class CalculateService {
    public int getPriceDiscount(int price, int discount){
        return price - price*discount/100;
    }

    public String numberToCurrency(int number){
        Locale locale = new Locale("vn", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String currency = numberFormat.format(number);
        return currency.substring(2, currency.length());
    }
}
