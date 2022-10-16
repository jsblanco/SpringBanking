package com.jsblanco.springbanking.controllers.products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
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
class CheckingAccountControllerTest {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    private AccountHolder oldHolder;
    private AccountHolder studentHolder;

    private CheckingAccount checkingAccount1;
    private CheckingAccount checkingAccount2;

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

        checkingAccount1 = checkingAccountRepository.save(new CheckingAccount(123, new BigDecimal(1000), oldHolder, "secret1", new Date(), Status.ACTIVE));
        checkingAccount2 = checkingAccountRepository.save(new CheckingAccount(456, new BigDecimal(1000), oldHolder, "secret2", new Date(), Status.ACTIVE));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(admin);
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
    }


    @Test
    void getAllCheckingAccounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/checking/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<BankProduct> productList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(2, productList.size());
        assertTrue(productList.containsAll(new ArrayList<>(Arrays.asList(checkingAccount1, checkingAccount2))));
    }

    @Test
    void getCheckingAccountById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/checking/" + checkingAccount1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CheckingAccount fetchedCheckingAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CheckingAccount.class);
        assertEquals(fetchedCheckingAccount, checkingAccount1);
    }

    @Test
    void saveCheckingAccounts() throws Exception {
        CheckingAccount newCheckingAccount = new CheckingAccount(222, new BigDecimal(10000), null, "secretKey1", new Date(), Status.ACTIVE);

        MvcResult mvcResult = mockMvc.perform(post("/checking/")
                        .content(objectMapper.writeValueAsString(new CreateBankProductDao<>(newCheckingAccount, oldHolder.getId(), studentHolder.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CheckingAccount fetchedCheckingAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CheckingAccount.class);
        assertEquals(fetchedCheckingAccount.getBalance(), newCheckingAccount.getBalance());
        assertEquals(oldHolder, fetchedCheckingAccount.getPrimaryOwner());
        assertEquals(studentHolder, fetchedCheckingAccount.getSecondaryOwner());

        newCheckingAccount = new CheckingAccount(333, new BigDecimal(10000), null, "secretKey2", new Date(), Status.ACTIVE);
        mvcResult = mockMvc.perform(post("/checking/")
                        .content(objectMapper.writeValueAsString(new CreateBankProductDao<>(newCheckingAccount, studentHolder.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        StudentCheckingAccount fetchedStudentCheckingAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), StudentCheckingAccount.class);
        assertEquals(fetchedStudentCheckingAccount.getBalance(), newCheckingAccount.getBalance());
        assertEquals(studentHolder, fetchedStudentCheckingAccount.getPrimaryOwner());
    }

    @Test
    void updateCheckingAccounts() throws Exception {
        checkingAccount1.setBalance(new Money(new BigDecimal(10000)));
        checkingAccount1.setSecretKey("updatedSecretKey");
        String payload = objectMapper.writeValueAsString(checkingAccount1);
        MvcResult mvcResult = mockMvc.perform(put("/checking/")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CheckingAccount fetchedCheckingAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CheckingAccount.class);
        assertEquals(fetchedCheckingAccount, checkingAccount1);
    }

    @Test
    void deleteCheckingAccounts() throws Exception {
        mockMvc.perform(delete("/checking/" + checkingAccount1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/checking/" + checkingAccount1.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
