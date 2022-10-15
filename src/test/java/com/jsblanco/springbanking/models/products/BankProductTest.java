package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;


class BankProductTest {

    private static class TestBankProduct extends BankProduct {
    }

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

    @DisplayName("Should return if the account is owned by the provided user, no matter if as the primary or secondary owner")
    @Test
    void isOwnedBy() {
        AccountHolder primaryOwner = new AccountHolder("primaryOwner", "Password", LocalDate.now(), new Address());
        primaryOwner.setId(1);
        bankProduct.setPrimaryOwner(primaryOwner);
        assertTrue(bankProduct.isOwnedBy(primaryOwner), "Should return true when its primary owner is passed, even if secondary owner is null");

        AccountHolder secondaryOwner = new AccountHolder("secondaryOwner", "Password", LocalDate.now(), new Address());
        secondaryOwner.setId(2);
        bankProduct.setSecondaryOwner(secondaryOwner);
        assertTrue(bankProduct.isOwnedBy(primaryOwner), "Should return true when its primary owner is passed and the secondary owner is not null");
        assertTrue(bankProduct.isOwnedBy(secondaryOwner), "Should return true when its secondary owner is passed");

        AccountHolder nonOwner = new AccountHolder("nonOwner", "Password", LocalDate.now(), new Address());
        nonOwner.setId(3);
        assertFalse(bankProduct.isOwnedBy(nonOwner), "Should return false when a user that is neither its primary nor its secondary owner is passed");
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
}
