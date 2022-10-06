package com.jsblanco.springbanking.controllers.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    private SavingsAccountRepository savingsAccountRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.accountHolderRepository.save(new AccountHolder( "accountHolder1", new Date(), new Address()));
        this.accountHolderRepository.save(new AccountHolder( "accountHolder2", new Date(), new Address()));
        this.accountHolderRepository.save(new AccountHolder( "accountHolder3", new Date(), new Address()));
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
        AccountHolder newAccountHolder = new AccountHolder("accountHolder4", new Date(), new Address());
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

        System.out.println(+accountHolder.getId());
        mockMvc.perform(delete("/holder/" + accountHolder.getId()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/holder/" + accountHolder.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
