package com.jsblanco.springbanking.controllers.products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.CreditCardRepository;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import com.jsblanco.springbanking.repositories.users.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BankProductControllerTest {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;

    private Admin admin;
    private AccountHolder holder1;
    private AccountHolder holder2;

    private CreditCard creditCard;
    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;
    private StudentCheckingAccount studentCheckingAccount;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        admin = adminRepository.save(new Admin("Admin", "BankProductControllerTestAdmin", "password"));
        holder1 = accountHolderRepository.save(new AccountHolder("Holder1", "BankProductControllerTestHolder1","Password1", LocalDate.of(1990, 1, 1), new Address("door", "postalCode", "city", "country")));
        holder2 = accountHolderRepository.save(new AccountHolder("Holder2","BankProductControllerTestHolder2", "Password2", LocalDate.of(1990, 1, 1), new Address("door", "postalCode", "city", "country")));

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
        adminRepository.deleteAll();
        accountHolderRepository.deleteAll();
        creditCardRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        studentCheckingAccountRepository.deleteAll();
    }

    @Test
    void getAllBankProducts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/product/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<BankProduct> productList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(4, productList.size());
        assertTrue(productList.containsAll(new ArrayList<>(Arrays.asList(savingsAccount, creditCard, checkingAccount, studentCheckingAccount))));
    }

    @Test
    void getBankProductById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/product/" + creditCard.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard fetchedCreditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);
        assertEquals(fetchedCreditCard, creditCard);

        mvcResult = mockMvc.perform(get("/product/" + checkingAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CheckingAccount fetchedCheckingAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CheckingAccount.class);
        assertEquals(fetchedCheckingAccount, checkingAccount);

        mvcResult = mockMvc.perform(get("/product/" + studentCheckingAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        StudentCheckingAccount fetchedStudentCheckingAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), StudentCheckingAccount.class);
        assertEquals(fetchedStudentCheckingAccount, studentCheckingAccount);

        mvcResult = mockMvc.perform(get("/product/" + savingsAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        SavingsAccount fetchedSavingsAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SavingsAccount.class);
        assertEquals(fetchedSavingsAccount, savingsAccount);
    }

    @Test
    void getBankProductBalance() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/product/balance/" + savingsAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Money fetchedSavingsAccountBalance = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Money.class);
        assertEquals(fetchedSavingsAccountBalance, savingsAccount.getBalance());

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(admin);
        mvcResult = mockMvc.perform(get("/product/balance/"+ savingsAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        fetchedSavingsAccountBalance = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Money.class);
        assertEquals(fetchedSavingsAccountBalance, savingsAccount.getBalance());

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(holder2);
        mockMvc.perform(get("/product/balance/" + savingsAccount.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void transferFundsBetweenProducts() throws Exception {
        Money transfer = new Money(new BigDecimal(10));
        TransferFundsDao transferFundsDao = new TransferFundsDao(transfer, checkingAccount.getId(), savingsAccount.getId(), holder1.getName());

        String payload = objectMapper.writeValueAsString(transferFundsDao);
        MvcResult mvcResult = mockMvc.perform(post("/product/transfer")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<BankProduct> fetchedSavingsAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Money expectedEmitterBalance = checkingAccount.getBalance();
        expectedEmitterBalance.decreaseAmount(transfer);

        Money expectedRecipientBalance = savingsAccount.getBalance();
        expectedRecipientBalance.increaseAmount(transfer);
        assertEquals(fetchedSavingsAccount.get(0).getBalance(), expectedEmitterBalance);
        assertEquals(fetchedSavingsAccount.get(1).getBalance(), expectedRecipientBalance);

    }
}
