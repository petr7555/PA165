package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.ExchangeRateTable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.Exchanger;

/**
 * @author Petr Janik 485122
 * @since 03.03.2020
 */
public class MainXml {
    public static void main(String[] args) {
        ApplicationContext ctx = new
                ClassPathXmlApplicationContext("beans.xml");

        CurrencyConvertor currencyConvertor = ctx.getBean(CurrencyConvertor.class);
        BigDecimal value = currencyConvertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), BigDecimal.ONE);
        System.out.println(value);
    }
}
