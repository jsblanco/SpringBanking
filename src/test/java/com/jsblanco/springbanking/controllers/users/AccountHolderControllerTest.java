package com.jsblanco.springbanking.controllers.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.util.Address;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private AdminRepository adminRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.accountHolderRepository.save(new AccountHolder("accountHolder1", "password1", LocalDate.of(1990, 1, 1), new Address("1","2","3","4")));
        this.accountHolderRepository.save(new AccountHolder("accountHolder2", "password2", LocalDate.of(1990, 1, 1), new Address("1","2","3","4")));
        this.accountHolderRepository.save(new AccountHolder("accountHolder3", "password3", LocalDate.of(1990, 1, 1), new Address("1","2","3","4")));

        Admin admin = adminRepository.save(new Admin("Admin", "password"));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(admin);
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
    }

    @Test
    void getAccountHolderById() throws Exception {
        AccountHolder dbAccountHolder = this.accountHolderRepository.findAll().get(0);
        MvcResult mvcResult = mockMvc.perform(get("/holder/" + dbAccountHolder.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        AccountHolder accountHolderList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountHolder.class);

        assertEquals(accountHolderList.getId(), dbAccountHolder.getId());
        assertEquals(accountHolderList.getName(), dbAccountHolder.getName());
    }

    @Test
    void getAllThirdParties() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/holder/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<AccountHolder> accountHolderList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(3, accountHolderList.size());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("accountHolder1"));
    }

    @Test
    void saveAccountHolder() throws Exception {
        AccountHolder newAccountHolder = new AccountHolder("accountHolder4", "Password4", LocalDate.of(1990, 1, 1), new Address());
        String payload = objectMapper.writeValueAsString(newAccountHolder);
        MvcResult mvcResult = mockMvc.perform(post("/holder/")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        AccountHolder accountHolder = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountHolder.class);

        assertEquals(accountHolder.getName(), "accountHolder4");
    }

    @Test
    void updateAccountHolder() throws Exception {
        AccountHolder updatedAccountHolder = this.accountHolderRepository.findAll().get(0);
        updatedAccountHolder.setName("updatedAccountHolder");

        String payload = objectMapper.writeValueAsString(updatedAccountHolder);
        MvcResult mvcResult = mockMvc.perform(put("/holder/")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        AccountHolder fetchedAccountHolder = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountHolder.class);
        assertEquals(updatedAccountHolder.getId(), fetchedAccountHolder.getId());
        assertEquals(updatedAccountHolder.getName(), fetchedAccountHolder.getName());
    }

    @Test
    void deleteAccountHolder() throws Exception {
        AccountHolder accountHolder = this.accountHolderRepository.findAll().get(0);

        mockMvc.perform(delete("/holder/" + accountHolder.getId()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/holder/" + accountHolder.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
