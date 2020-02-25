package cz.muni.fi.pa165.currency;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrencyConvertorImplTest {

    @Test
    public void testConvertSameCurrency() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        when(et.getExchangeRate(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance(Locale.FRANCE)))
                .thenReturn(BigDecimal.ONE);
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //when
        BigDecimal result = convertor.convert(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance(Locale.FRANCE), new BigDecimal("15.29"));
        //then
        assertEquals(new BigDecimal("15.29"), result);
    }

    @Test
    public void testConvertDifferentCurrency() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        when(et.getExchangeRate(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK")))
                .thenReturn(new BigDecimal("25"));
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //when
        BigDecimal result = convertor.convert(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK"), new BigDecimal("10"));
        //then
        assertEquals(new BigDecimal("250.00"), result);
    }

    @Test
    public void testConvertMaxValue() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        when(et.getExchangeRate(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK")))
                .thenReturn(new BigDecimal("25"));
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //when
        BigDecimal result = convertor.convert(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK"), new BigDecimal(Integer.MAX_VALUE));
        //then
        assertEquals(new BigDecimal(Integer.MAX_VALUE).multiply(new BigDecimal("25.00")), result);
    }

    @Test
    public void testConvertMinValue() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        when(et.getExchangeRate(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK")))
                .thenReturn(new BigDecimal("25"));
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //when
        BigDecimal result = convertor.convert(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK"), new BigDecimal(Integer.MIN_VALUE));
        //then
        assertEquals(new BigDecimal(Integer.MIN_VALUE).multiply(new BigDecimal("25.00")), result);
    }

    @Test
    public void testConvertRounding() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        when(et.getExchangeRate(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK")))
                .thenReturn(new BigDecimal("25"));
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //when
        BigDecimal resultDown = convertor.convert(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK"), new BigDecimal("4.0938"));
        BigDecimal resultUp = convertor.convert(Currency.getInstance(Locale.GERMANY),
                Currency.getInstance("CZK"), new BigDecimal("4.1006"));
        //then
        assertEquals(new BigDecimal("102.34"), resultDown);
        assertEquals(new BigDecimal("102.52"), resultUp);
    }

    @Test
    public void testConvertWithNullSourceCurrency() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> convertor.convert(null,
                        Currency.getInstance(Locale.FRANCE), new BigDecimal("15.29")));
    }

    @Test
    public void testConvertWithNullTargetCurrency() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> convertor.convert(Currency.getInstance(Locale.GERMANY),
                        null, new BigDecimal("15.29")));
    }

    @Test
    public void testConvertWithNullSourceAmount() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> convertor.convert(Currency.getInstance(Locale.GERMANY),
                        Currency.getInstance(Locale.FRANCE), null));
    }

    @Test
    public void testConvertWithUnknownCurrency() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        when(et.getExchangeRate(Currency.getInstance(Locale.GERMANY), Currency.getInstance(Locale.FRANCE)))
                .thenReturn(null);
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //then
        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> convertor.convert(Currency.getInstance(Locale.GERMANY),
                        Currency.getInstance(Locale.FRANCE), new BigDecimal("15.29")));
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws Exception {
        //given
        ExchangeRateTable et = mock(ExchangeRateTable.class);
        when(et.getExchangeRate(Currency.getInstance(Locale.GERMANY), Currency.getInstance(Locale.FRANCE)))
                .thenThrow(new ExternalServiceFailureException("Some external problem"));
        CurrencyConvertor convertor = new CurrencyConvertorImpl(et);

        //then
        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> convertor.convert(Currency.getInstance(Locale.GERMANY),
                        Currency.getInstance(Locale.FRANCE), new BigDecimal("15.29")));
    }


}
