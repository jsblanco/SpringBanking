package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.CreditCardRepository;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import com.jsblanco.springbanking.services.products.interfaces.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankProductServiceTest {

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
        holder1 = accountHolderRepository.save(new AccountHolder("Holder1", new Date(), new Address()));
        holder2 = accountHolderRepository.save(new AccountHolder("Holder2", new Date(), new Address()));

        creditCard = creditCardRepository.save(new CreditCard(1, new BigDecimal(1000), holder1));
        savingsAccount = savingsAccountRepository.save(new SavingsAccount(2, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        checkingAccount = checkingAccountRepository.save(new CheckingAccount(3, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(new CheckingAccount(4, new BigDecimal(1000), holder2, "secret", new Date(), Status.ACTIVE)));
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
        creditCardRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        studentCheckingAccountRepository.deleteAll();
    }

    @Test
    void get() {
        assertEquals(studentCheckingAccount, this.bankProductService.get(studentCheckingAccount.getId()));
        assertEquals(checkingAccount, this.bankProductService.get(checkingAccount.getId()));
        assertEquals(savingsAccount, this.bankProductService.get(savingsAccount.getId()));
        assertEquals(creditCard, this.bankProductService.get(creditCard.getId()));
    }

    @Test
    void save() {
        CreditCard newCard = (CreditCard) this.bankProductService.save(new CreditCard(99, new BigDecimal(1111), holder2));
        assertEquals(newCard, this.bankProductService.get(newCard.getId()), "Should store new credit cards");
        assertThrows(IllegalArgumentException.class, ()-> this.bankProductService.save(newCard), "Should not let save products if they're already in the db");

        SavingsAccount newSavingsAccount = (SavingsAccount) this.bankProductService.save(new SavingsAccount(2222, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        assertEquals(newSavingsAccount, this.bankProductService.get(newSavingsAccount.getId()), "Should store new saving accounts");
        assertThrows(IllegalArgumentException.class, ()-> this.bankProductService.save(newSavingsAccount), "Should not let save products if they're already in the db");

        CheckingAccount newCheckingAccount = (CheckingAccount) this.bankProductService.save(new CheckingAccount(3333, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        assertEquals(newCheckingAccount, this.bankProductService.get(newCheckingAccount.getId()), "Should store new checking accounts");
        assertThrows(IllegalArgumentException.class, ()-> this.bankProductService.save(newCheckingAccount), "Should not let save products if they're already in the db");

        StudentCheckingAccount newStudentCheckingAccount = (StudentCheckingAccount) this.bankProductService.save(new StudentCheckingAccount(new CheckingAccount(4444, new BigDecimal(1000), holder2, "secret", new Date(), Status.ACTIVE)));
        assertEquals(newStudentCheckingAccount, this.bankProductService.get(newStudentCheckingAccount.getId()), "Should store new student checking accounts");
        assertThrows(IllegalArgumentException.class, ()-> this.bankProductService.save(newStudentCheckingAccount), "Should not let save products if they're already in the db");
    }

    @Test
    void update() {
        studentCheckingAccount.setBalance(new Money(new BigDecimal(123)));
        assertEquals(studentCheckingAccount, this.bankProductService.update(studentCheckingAccount));
        checkingAccount.setBalance(new Money(new BigDecimal(123)));
        assertEquals(checkingAccount, this.bankProductService.update(checkingAccount));
        savingsAccount.setBalance(new Money(new BigDecimal(123)));
        assertEquals(savingsAccount, this.bankProductService.update(savingsAccount));
        creditCard.setBalance(new Money(new BigDecimal(123)));
        assertEquals(creditCard, this.bankProductService.update(creditCard));
    }

    @Test
    void delete() {
        this.bankProductService.delete(creditCard.getId());
        assertThrows(IllegalArgumentException.class, () -> this.bankProductService.get(creditCard.getId()), "Deleted product should no longer be in the DB");
        assertThrows(IllegalArgumentException.class, () -> this.bankProductService.delete(creditCard.getId()), "Should return an error when attempting to delete an account not in DB");
    }

    @Test
    void getAll() {
        List<BankProduct> productList = this.bankProductService.getAll();
        assertEquals(4, productList.size());
        assertTrue(productList.contains(creditCard));
        assertTrue(productList.contains(savingsAccount));
        assertTrue(productList.contains(checkingAccount));
        assertTrue(productList.contains(studentCheckingAccount));
    }

    @Test
    void getByOwner() {
        List<BankProduct> productList = this.bankProductService.getByOwner(holder1);
        System.out.println(productList);
        assertEquals(3, productList.size());
        assertTrue(productList.contains(creditCard));
        assertTrue(productList.contains(savingsAccount));
        assertTrue(productList.contains(checkingAccount));
        assertFalse(productList.contains(studentCheckingAccount));
    }

    @Test
    void transferFunds() {
        this.bankProductService.transferFunds(new Money(new BigDecimal(100)), checkingAccount, savingsAccount);
        assertEquals(new Money(new BigDecimal(900)), this.bankProductService.get(checkingAccount.getId()).getBalance());
        assertEquals(new Money(new BigDecimal(1100)), this.bankProductService.get(savingsAccount.getId()).getBalance());
    }
}
