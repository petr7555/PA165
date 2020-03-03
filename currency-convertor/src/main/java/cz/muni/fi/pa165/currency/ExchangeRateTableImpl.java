package cz.muni.fi.pa165.currency;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author Petr Janik 485122
 * @since 03.03.2020
 */
@Named
public class ExchangeRateTableImpl implements ExchangeRateTable {
    @Override
    public BigDecimal getExchangeRate(Currency sourceCurrency, Currency targetCurrency) throws ExternalServiceFailureException {
        if (sourceCurrency == Currency.getInstance("EUR") && targetCurrency == Currency.getInstance("CZK")) {
            return new BigDecimal("27");
        }
        return null;
    }
}
