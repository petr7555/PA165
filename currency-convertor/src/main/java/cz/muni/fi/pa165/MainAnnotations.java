package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author Petr Janik 485122
 * @since 03.03.2020
 */
public class MainAnnotations {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("cz.muni.fi.pa165");
        CurrencyConvertor currencyConvertor = ctx.getBean(CurrencyConvertor.class);
        BigDecimal value = currencyConvertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), BigDecimal.ONE);
        System.out.println(value);
    }
}
