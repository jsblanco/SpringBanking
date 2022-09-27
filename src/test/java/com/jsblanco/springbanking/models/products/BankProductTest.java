package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.util.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class TestBankProduct extends BankProduct {}

class BankProductTest {

    TestBankProduct bankProduct;

    @BeforeEach
    void setUp() {
        bankProduct = new TestBankProduct();
    }

    @DisplayName("Should increase balance as long as deposit is in the account's currency")
    @Test
    void increaseBalance() {
        bankProduct.setBalance(new Money(new BigDecimal(1000), Currency.getInstance("USD")));
        bankProduct.increaseBalance(new Money(new BigDecimal(10), Currency.getInstance("USD")));
        assertEquals(new Money(new BigDecimal(1010), Currency.getInstance("USD")), bankProduct.getBalance(), "Should increase balance when currencies are the same");

        assertThrows(IllegalArgumentException.class, () -> bankProduct.increaseBalance(new Money(new BigDecimal(1000), Currency.getInstance("EUR"))), "Should throw an exception when currencies differ");
    }

    @DisplayName("Should decrease balance as long as deposit is in the account's currency")
    @Test
    void decreaseBalance() {
        bankProduct.setBalance(new Money(new BigDecimal(1000), Currency.getInstance("USD")));
        bankProduct.decreaseBalance(new Money(new BigDecimal(10), Currency.getInstance("USD")));
        assertEquals(new Money(new BigDecimal(990), Currency.getInstance("USD")), bankProduct.getBalance(), "Should decrease balance when currencies are the same");

        bankProduct.decreaseBalance(new Money(new BigDecimal(1000), Currency.getInstance("USD")));
        assertEquals(new Money(new BigDecimal(-10), Currency.getInstance("USD")), bankProduct.getBalance(), "Should decrease balance below 0 when more money than available is extracted");

        assertThrows(IllegalArgumentException.class, () -> bankProduct.decreaseBalance(new Money(new BigDecimal(1000), Currency.getInstance("EUR"))), "Should throw an exception when currencies differ");
    }

    @DisplayName("Should use the Amount and Currency keys to produce a Money instance when requesting the balance")
    @Test
    void getBalance() {
        BigDecimal amount = new BigDecimal("12345.00");
        Currency currency = Currency.getInstance("EUR");
        bankProduct.setAmount(amount);
        bankProduct.setCurrency(currency);

        assertEquals(new Money(amount, currency), bankProduct.getBalance());
    }

    @DisplayName("When setting a balance, it should store the Money class keys in the Amount and Currency keys")
    @Test
    void setBalance() {
        BigDecimal amount = new BigDecimal("12345.00");
        Currency currency = Currency.getInstance("EUR");
        bankProduct.setBalance(new Money(amount, currency));

        assertEquals(amount, bankProduct.getAmount());
        assertEquals(currency, bankProduct.getCurrency());
    }

    @DisplayName("When setting a minimum balance, it should store the Money class amount and check the currency is the same as in the account")
    @Test
    void setMinimumBalance() {
        BigDecimal amount = new BigDecimal("12345.00");
        Currency currency = Currency.getInstance("USD");
        bankProduct.setMinimumBalance(new Money(amount, currency));

        assertEquals(amount, bankProduct.getMinimumAmount(), "Minimum amount should correspond to the value passed to setMinimumAmount.");
        assertThrows(IllegalArgumentException.class, ()->bankProduct.setMinimumBalance(new Money(amount, Currency.getInstance("EUR"))), "Minimum balance should be in the same currency as the account's current balance.");
    }
}
