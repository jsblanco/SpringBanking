package com.jsblanco.springbanking.controllers.products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.products.CreditCard;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.repositories.products.CreditCardRepository;
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
class CreditCardControllerTest {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    private AccountHolder holder1;
    private AccountHolder holder2;

    private CreditCard creditCard1;
    private CreditCard creditCard2;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Admin admin = adminRepository.save(new Admin("Admin", "BankProductControllerTestAdmin", "password"));
        holder1 = accountHolderRepository.save(new AccountHolder("oldHolder", randomUUID().toString(), "Password1", LocalDate.of(1970, 1, 1), new Address("door", "postalCode", "city", "country")));
        holder2 = accountHolderRepository.save(new AccountHolder("studentHolder", randomUUID().toString(), "Password2", LocalDate.of(2010, 1, 1), new Address("door", "postalCode", "city", "country")));

        creditCard1 = creditCardRepository.save(new CreditCard(123, new BigDecimal(1000), new Date(), holder1));
        creditCard2 = creditCardRepository.save(new CreditCard(456, new BigDecimal(1000), new Date(), holder1));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(admin);
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
    }


    @Test
    void getAllCreditCards() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/card/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<BankProduct> productList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(2, productList.size());
        assertTrue(productList.containsAll(new ArrayList<>(Arrays.asList(creditCard1, creditCard2))));
    }

    @Test
    void getCreditCardById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/card/" + creditCard1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard fetchedCreditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);
        assertEquals(fetchedCreditCard, creditCard1);
    }

    @Test
    void saveCreditCards() throws Exception {
        CreditCard newCreditCard = new CreditCard(222, new BigDecimal(10000), new Date(), null);

        MvcResult mvcResult = mockMvc.perform(post("/card/")
                        .content(objectMapper.writeValueAsString(new CreateBankProductDao<>(newCreditCard, holder1.getId(), holder2.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard fetchedCreditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);
        assertEquals(fetchedCreditCard.getBalance(), newCreditCard.getBalance());
        assertEquals(holder1, fetchedCreditCard.getPrimaryOwner());
        assertEquals(holder2, fetchedCreditCard.getSecondaryOwner());
    }

    @Test
    void updateCreditCards() throws Exception {
        creditCard1.setBalance(new Money(new BigDecimal(123456)));
        String payload = objectMapper.writeValueAsString(creditCard1);
        MvcResult mvcResult = mockMvc.perform(put("/card/")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard fetchedCreditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);
        assertEquals(fetchedCreditCard, creditCard1);
    }

    @Test
    void deleteCreditCards() throws Exception {
        mockMvc.perform(delete("/card/" + creditCard1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/card/" + creditCard1.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
