package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.ThirdPartyTransferDao;
import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import com.jsblanco.springbanking.repositories.users.AdminRepository;
import com.jsblanco.springbanking.repositories.users.ThirdPartyRepository;
import com.jsblanco.springbanking.services.products.interfaces.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;
    @Autowired
    AccountService accountService;

    Admin admin;
    AccountHolder holder1;
    AccountHolder holder2;
    ThirdParty thirdParty1;
    ThirdParty thirdParty2;

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

        savingsAccount = savingsAccountRepository.save(new SavingsAccount(2, new BigDecimal(1000), holder1, "secret savings", new Date(), Status.ACTIVE));
        checkingAccount = checkingAccountRepository.save(new CheckingAccount(3, new BigDecimal(1000), holder1, "secret checking", new Date(), Status.ACTIVE));
        studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(new CheckingAccount(4, new BigDecimal(1000), holder2, "secret", new Date(), Status.ACTIVE)));
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        studentCheckingAccountRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    void get() {
        assertEquals(studentCheckingAccount, this.accountService.get(studentCheckingAccount.getId()));
        assertEquals(checkingAccount, this.accountService.get(checkingAccount.getId()));
        assertEquals(savingsAccount, this.accountService.get(savingsAccount.getId()));
        assertThrows(ResponseStatusException.class, () -> this.accountService.get(12345), "Should return an exception when fetching an ID not in the DB");
    }

    @Test
    void save() {
        SavingsAccount newSavingsAccount = (SavingsAccount) this.accountService.save(new SavingsAccount(2222, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        assertEquals(newSavingsAccount, this.accountService.get(newSavingsAccount.getId()), "Should store new saving accounts");
        assertThrows(ResponseStatusException.class, () -> this.accountService.save(newSavingsAccount), "Should not let save products if they're already in the db");

        CheckingAccount newCheckingAccount = (CheckingAccount) this.accountService.save(new CheckingAccount(3333, new BigDecimal(1000), holder1, "secret", new Date(), Status.ACTIVE));
        assertEquals(newCheckingAccount, this.accountService.get(newCheckingAccount.getId()), "Should store new checking accounts");
        assertThrows(ResponseStatusException.class, () -> this.accountService.save(newCheckingAccount), "Should not let save products if they're already in the db");

        StudentCheckingAccount newStudentCheckingAccount = (StudentCheckingAccount) this.accountService.save(new StudentCheckingAccount(new CheckingAccount(4444, new BigDecimal(1000), holder2, "secret", new Date(), Status.ACTIVE)));
        assertEquals(newStudentCheckingAccount, this.accountService.get(newStudentCheckingAccount.getId()), "Should store new student checking accounts");
        assertThrows(ResponseStatusException.class, () -> this.accountService.save(newStudentCheckingAccount), "Should not let save products if they're already in the db");
    }

    @Test
    void update() {
        studentCheckingAccount.setBalance(new Money(new BigDecimal(123)));
        assertEquals(studentCheckingAccount, this.accountService.update(studentCheckingAccount));
        checkingAccount.setBalance(new Money(new BigDecimal(123)));
        assertEquals(checkingAccount, this.accountService.update(checkingAccount));
        savingsAccount.setBalance(new Money(new BigDecimal(123)));
        assertEquals(savingsAccount, this.accountService.update(savingsAccount));
        assertThrows(ResponseStatusException.class, () -> this.accountService.update(new CheckingAccount()), "Should return an exception when attempting to update a product that is not in the DB");
    }

    @Test
    void transferFunds() {
        this.accountService.transferFunds(new TransferFundsDao(new Money(new BigDecimal(100)), checkingAccount.getId(), savingsAccount.getId(), "Holder1"), holder1);
        assertEquals(new Money(new BigDecimal(900)), this.accountService.get(checkingAccount.getId()).getBalance());
        assertEquals(new Money(new BigDecimal(1100)), this.accountService.get(savingsAccount.getId()).getBalance());

        assertThrows(IllegalArgumentException.class, () -> this.accountService.transferFunds(new TransferFundsDao(new Money(new BigDecimal(1)), checkingAccount.getId(), savingsAccount.getId(), "Holder1"), holder2), "Should fail when user does not own emitter account");
        assertThrows(IllegalArgumentException.class, () -> this.accountService.transferFunds(new TransferFundsDao(new Money(new BigDecimal(1)), checkingAccount.getId(), savingsAccount.getId(), "Wrong User"), holder1), "Should fail when specified username does not meet any of recipient account's owners' names");
        assertThrows(IllegalArgumentException.class, () -> this.accountService.transferFunds(new TransferFundsDao(new Money(new BigDecimal(99999)), checkingAccount.getId(), savingsAccount.getId(), "Holder1"), holder1), "Should fail when emitter account does not have enough funds");
    }

    @Test
    void thirdPartyOperation() {
        ThirdPartyTransferDao dao = new ThirdPartyTransferDao();
        dao.setAccountId(checkingAccount.getId());
        dao.setSecretKey(checkingAccount.getSecretKey());
        dao.setTransfer(new Money(new BigDecimal(10)));

        Money expectedBalance = checkingAccount.getBalance();
        expectedBalance.increaseAmount(new Money(new BigDecimal(10)));

        accountService.thirdPartyOperation(thirdParty1.getHashedKey(), dao, thirdParty1);
        assertEquals(expectedBalance, accountService.get(checkingAccount.getId()).getBalance(), "Should increase account balance when data is correct and transfer amount is positive");

        dao.setTransfer(new Money(new BigDecimal(-10)));
        expectedBalance.decreaseAmount(new Money(new BigDecimal(-10)));

        accountService.thirdPartyOperation(thirdParty1.getHashedKey(), dao, thirdParty1);
        assertEquals(expectedBalance, accountService.get(checkingAccount.getId()).getBalance(), "Should decrease account balance when data is correct and transfer amount is negative");

        dao.setAccountId(savingsAccount.getId());
        dao.setSecretKey(savingsAccount.getSecretKey());
        expectedBalance = savingsAccount.getBalance();
        expectedBalance.decreaseAmount(new Money(new BigDecimal(-10)));
        accountService.thirdPartyOperation(thirdParty1.getHashedKey(), dao, thirdParty1);
        assertEquals(expectedBalance, accountService.get(savingsAccount.getId()).getBalance(), "Should work with savings accounts");

        dao.setAccountId(studentCheckingAccount.getId());
        dao.setSecretKey(studentCheckingAccount.getSecretKey());
        expectedBalance = studentCheckingAccount.getBalance();
        expectedBalance.decreaseAmount(new Money(new BigDecimal(-10)));
        accountService.thirdPartyOperation(thirdParty1.getHashedKey(), dao, thirdParty1);
        assertEquals(expectedBalance, accountService.get(savingsAccount.getId()).getBalance(), "Should work with student checking accounts");

        dao.setSecretKey(savingsAccount.getSecretKey());
        assertThrows(ResponseStatusException.class, () -> accountService.thirdPartyOperation(thirdParty1.getHashedKey(), dao, thirdParty1), "Should throw an error when provided secret key does not match with that of the provided account");

        dao.setSecretKey(thirdParty2.getHashedKey());
        assertThrows(ResponseStatusException.class, () -> accountService.thirdPartyOperation(thirdParty1.getHashedKey(), dao, thirdParty1), "Should throw an error when provided hash key differs from that of the user");
    }
}
