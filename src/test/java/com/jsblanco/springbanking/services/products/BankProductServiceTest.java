package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.CreditCardRepository;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import com.jsblanco.springbanking.repositories.users.AdminRepository;
import com.jsblanco.springbanking.repositories.users.ThirdPartyRepository;
import com.jsblanco.springbanking.services.products.interfaces.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankProductServiceTest {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
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
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    SavingsAccountService savingsAccountService;
    @Autowired
    CheckingAccountService checkingAccountService;
    @Autowired
    StudentCheckingAccountService studentCheckingAccountService;

    Admin admin;
    AccountHolder holder1;
    AccountHolder holder2;
    ThirdParty thirdParty1;
    ThirdParty thirdParty2;

    CreditCard creditCard;
    SavingsAccount savingsAccount;
    CheckingAccount checkingAccount;
    StudentCheckingAccount studentCheckingAccount;

    @BeforeEach
    void setUp() {
        admin = adminRepository.save(new Admin("Admin", randomUUID().toString(), "Password"));
        holder1 = accountHolderRepository.save(new AccountHolder("Holder1", randomUUID().toString(), "Password1", LocalDate.of(1990, 1, 1), new Address("door", "postalCode", "city", "country")));
        holder2 = accountHolderRepository.save(new AccountHolder("Holder2", randomUUID().toString(), "Password2", LocalDate.of(1990, 1, 1), new Address("door", "postalCode", "city", "country")));
        thirdParty1 = thirdPartyRepository.save(new ThirdParty("Third party 1", randomUUID().toString(), "Password1", "Secret key 1"));
        thirdParty2 = thirdPartyRepository.save(new ThirdParty("Third party 2", randomUUID().toString(), "Password2", "Secret key 2"));

        creditCard = creditCardRepository.save(new CreditCard(1, new BigDecimal(1000), new Date(), holder1));
        savingsAccount = savingsAccountRepository.save(new SavingsAccount(2, new BigDecimal(1000), holder1, "secret savings", new Date(), Status.ACTIVE));
        checkingAccount = checkingAccountRepository.save(new CheckingAccount(3, new BigDecimal(1000), holder1, "secret checking", new Date(), Status.ACTIVE));
        studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(new CheckingAccount(4, new BigDecimal(1000), holder2, "secret", new Date(), Status.ACTIVE)));
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
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
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.get(12345), "Should return an exception when fetching an ID not in the DB");
    }

    @Test
    void save() {
        CreateBankProductDao<CreditCard> creditCardDao = new CreateBankProductDao<>(new CreditCard(99, new BigDecimal(1111), new Date(), null), holder2.getId());
        CreditCard newCreditCard = this.creditCardService.createNewProduct(creditCardDao);
        assertEquals(newCreditCard, this.bankProductService.get(newCreditCard.getId()), "Should store new credit cards");
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.save(new CreateBankProductDao<>(newCreditCard, holder2.getId())), "Should not let save products if they're already in the db");

        CreateBankProductDao<SavingsAccount> savingsAccountDao = new CreateBankProductDao<>(new SavingsAccount(99, new BigDecimal(1111),null, "secret", new Date(), Status.ACTIVE), holder2.getId());
        SavingsAccount newSavingsAccount = this.savingsAccountService.createNewProduct(savingsAccountDao);
        assertEquals(newSavingsAccount, this.bankProductService.get(newSavingsAccount.getId()), "Should store new credit cards");
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.save(new CreateBankProductDao<>(newSavingsAccount, holder2.getId())), "Should not let save products if they're already in the db");

        CreateBankProductDao<CheckingAccount> checkingAccountDao = new CreateBankProductDao<>(new CheckingAccount(99, new BigDecimal(1111),null, "secret", new Date(), Status.ACTIVE), holder2.getId());
        CheckingAccount newCheckingAccount = this.checkingAccountService.createNewProduct(checkingAccountDao);
        assertEquals(newCheckingAccount, this.bankProductService.get(newCheckingAccount.getId()), "Should store new credit cards");
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.save(new CreateBankProductDao<>(newCheckingAccount, holder2.getId())), "Should not let save products if they're already in the db");

        CreateBankProductDao<StudentCheckingAccount> studentCheckingAccountDao = new CreateBankProductDao<>(new StudentCheckingAccount(new CheckingAccount(99, new BigDecimal(1111),null, "secret", new Date(), Status.ACTIVE)), holder2.getId());
        StudentCheckingAccount newStudentCheckingAccount = this.studentCheckingAccountService.createNewProduct(studentCheckingAccountDao);
        assertEquals(newStudentCheckingAccount, this.bankProductService.get(newStudentCheckingAccount.getId()), "Should store new credit cards");
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.save(new CreateBankProductDao<>(newStudentCheckingAccount, holder2.getId())), "Should not let save products if they're already in the db");
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
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.update(new CreditCard()), "Should return an exception when attempting to update a product that is not in the DB");
    }

    @Test
    void delete() {
        this.bankProductService.delete(creditCard.getId());
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.get(creditCard.getId()), "Deleted product should no longer be in the DB");
        assertThrows(ResponseStatusException.class, () -> this.bankProductService.delete(creditCard.getId()), "Should return an error when attempting to delete an account not in DB");
    }

    @Test
    void getProductBalance() {
        assertEquals(studentCheckingAccount.getBalance(), this.bankProductService.getProductBalance(studentCheckingAccount.getId(), holder2));
        assertEquals(checkingAccount.getBalance(), this.bankProductService.getProductBalance(checkingAccount.getId(), holder1));
        assertEquals(savingsAccount.getBalance(), this.bankProductService.getProductBalance(savingsAccount.getId(), holder1));
        assertEquals(creditCard.getBalance(), this.bankProductService.getProductBalance(creditCard.getId(), holder1));
    }

    @Test
    void modifyProductBalance() {
        Money balanceModification = new Money(new BigDecimal(10));
        Money updatedBalance = checkingAccount.getBalance();
        updatedBalance.increaseAmount(balanceModification);
        this.bankProductService.modifyProductBalance(checkingAccount.getId(), balanceModification);
        assertEquals(updatedBalance, this.bankProductService.getProductBalance(checkingAccount.getId(), admin));

        balanceModification = new Money(new BigDecimal(-10));
        updatedBalance.decreaseAmount(balanceModification);
        this.bankProductService.modifyProductBalance(checkingAccount.getId(), balanceModification);
        assertEquals(updatedBalance, this.bankProductService.getProductBalance(checkingAccount.getId(), admin));
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
        assertEquals(3, productList.size());
        assertTrue(productList.contains(creditCard));
        assertTrue(productList.contains(savingsAccount));
        assertTrue(productList.contains(checkingAccount));
        assertFalse(productList.contains(studentCheckingAccount));
    }
}
