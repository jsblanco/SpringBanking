package com.jsblanco.springbanking.controllers.products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
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

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class SavingsAccountControllerTest {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    private AccountHolder oldHolder;
    private AccountHolder studentHolder;

    private SavingsAccount savingsAccount1;
    private SavingsAccount savingsAccount2;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Admin admin = adminRepository.save(new Admin("Admin", "BankProductControllerTestAdmin", "password"));
        oldHolder = accountHolderRepository.save(new AccountHolder("oldHolder", randomUUID().toString(), "Password1", LocalDate.of(1970, 1, 1), new Address("door", "postalCode", "city", "country")));
        studentHolder = accountHolderRepository.save(new AccountHolder("studentHolder", randomUUID().toString(), "Password2", LocalDate.of(2010, 1, 1), new Address("door", "postalCode", "city", "country")));

        savingsAccount1 = savingsAccountRepository.save(new SavingsAccount(123, new BigDecimal(1000), oldHolder, "secret1", new Date(), Status.ACTIVE));
        savingsAccount2 = savingsAccountRepository.save(new SavingsAccount(456, new BigDecimal(1000), oldHolder, "secret2", new Date(), Status.ACTIVE));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(admin);
    }

    @AfterEach
    void tearDown() {
        savingsAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
    }


    @Test
    void getAllSavingsAccounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/savings/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<BankProduct> productList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(2, productList.size());
        assertTrue(productList.containsAll(new ArrayList<>(Arrays.asList(savingsAccount1, savingsAccount2))));
    }

    @Test
    void getSavingsAccountById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/savings/" + savingsAccount1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        SavingsAccount fetchedSavingsAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SavingsAccount.class);
        assertEquals(fetchedSavingsAccount, savingsAccount1);
    }

    @Test
    void saveSavingsAccounts() throws Exception {
        SavingsAccount newSavingsAccount = new SavingsAccount(222, new BigDecimal(10000), null, "secretKey1", new Date(), Status.ACTIVE);

        MvcResult mvcResult = mockMvc.perform(post("/savings/")
                        .content(objectMapper.writeValueAsString(new CreateBankProductDao<>(newSavingsAccount, oldHolder.getId(), studentHolder.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        SavingsAccount fetchedSavingsAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SavingsAccount.class);
        assertEquals(fetchedSavingsAccount.getBalance(), newSavingsAccount.getBalance());
        assertEquals(oldHolder, fetchedSavingsAccount.getPrimaryOwner());
        assertEquals(studentHolder, fetchedSavingsAccount.getSecondaryOwner());
    }

    @Test
    void updateSavingsAccounts() throws Exception {
        savingsAccount1.setBalance(new Money(new BigDecimal(10000)));
        savingsAccount1.setSecretKey("updatedSecretKey");
        String payload = objectMapper.writeValueAsString(savingsAccount1);
        MvcResult mvcResult = mockMvc.perform(put("/savings/")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        SavingsAccount fetchedSavingsAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SavingsAccount.class);
        assertEquals(fetchedSavingsAccount, savingsAccount1);
    }

    @Test
    void deleteSavingsAccounts() throws Exception {
        mockMvc.perform(delete("/savings/" + savingsAccount1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/savings/" + savingsAccount1.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
