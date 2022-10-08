package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.products.CreditCard;
import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.CreditCardRepository;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import com.jsblanco.springbanking.services.products.interfaces.BankProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BankProductControllerTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;
    @Autowired
    BankProductService bankProductService;

    AccountHolder holder1;
    AccountHolder holder2;

    CreditCard creditCard;
    SavingsAccount savingsAccount;
    CheckingAccount checkingAccount;
    StudentCheckingAccount studentCheckingAccount;

    @BeforeEach
    void setUp() {
        holder1 = accountHolderRepository.save(new AccountHolder("Holder1", LocalDate.of(1990, 1, 1), new Address("door", "postalCode", "city", "country")));
        holder2 = accountHolderRepository.save(new AccountHolder("Holder2", LocalDate.of(1990, 1, 1), new Address("door", "postalCode", "city", "country")));

        creditCard = creditCardRepository.save(new CreditCard(1, new BigDecimal(1000), holder1));
        savingsAccount = savingsAccountRepository.save(new SavingsAccount(2, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        checkingAccount = checkingAccountRepository.save(new CheckingAccount(3, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(new CheckingAccount(4, new BigDecimal(1000), holder2, "secret", new Date(), Status.ACTIVE)));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(holder1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllBankProducts() {
    }

    @Test
    void getBankProductById() {
    }

    @Test
    void getBankProductBalance() {
    }

    @Test
    void saveBankProducts() {
    }

    @Test
    void transferFundsBetweenProducts() {
    }

    @Test
    void updateBankProducts() {
    }

    @Test
    void deleteBankProducts() {
    }
}
