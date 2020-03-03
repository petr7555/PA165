package cz.muni.fi.pa165.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
@Named
public class CurrencyConvertorImpl implements CurrencyConvertor {
    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    @Inject
    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        logger.trace("Converting amount of {} from {} to {}.", sourceAmount, sourceCurrency, targetCurrency);
        if (sourceCurrency == null || targetCurrency == null || sourceAmount == null){
            throw new IllegalArgumentException("sourceCurrency, targetCurrency and sourceAmount must not be null");
        }
        BigDecimal exchangeRate = null;
        try {
            exchangeRate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
            if (exchangeRate == null) {
                logger.error("Exchange rate for {} and {} is missing.", sourceCurrency, targetCurrency);
                throw new UnknownExchangeRateException("Exchange rate for given pair is not known.");
            }
        } catch (ExternalServiceFailureException e) {
            logger.error("ExternalServiceFailureException has occurred.");
            throw new UnknownExchangeRateException(e.getMessage(), e);
        }
        return exchangeRate.multiply(sourceAmount).setScale(2, RoundingMode.HALF_EVEN);
    }
}
