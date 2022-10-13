package com.jsblanco.springbanking.controllers.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.repositories.users.ThirdPartyRepository;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ThirdPartyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        thirdPartyRepository.save(new ThirdParty("thirdParty1", "hash1"));
        thirdPartyRepository.save(new ThirdParty("thirdParty2", "hash2"));
        thirdPartyRepository.save(new ThirdParty("thirdParty3", "hash3"));


        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(null);
    }

    @AfterEach
    void tearDown() {
        thirdPartyRepository.deleteAll();
    }

    @Test
    void getThirdPartyById() throws Exception {
        ThirdParty dbThirdParty = this.thirdPartyRepository.findAll().get(0);
        MvcResult mvcResult = mockMvc.perform(get("/thirdparty/" + dbThirdParty.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ThirdParty thirdPartyList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ThirdParty.class);

        assertEquals(thirdPartyList.getId(), dbThirdParty.getId());
        assertEquals(thirdPartyList.getName(), dbThirdParty.getName());
        assertEquals(thirdPartyList.getHashedKey(), dbThirdParty.getHashedKey());
    }

    @Test
    void getAllThirdParties() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/thirdparty/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<ThirdParty> thirdPartyList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(3, thirdPartyList.size());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("thirdParty1"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("hash3"));
    }

    @Test
    void saveThirdParty() throws Exception {

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new Admin());
        ThirdParty newThirdParty = new ThirdParty("thirdParty4", "hash4");
        String payload = objectMapper.writeValueAsString(newThirdParty);

        MockHttpServletRequestBuilder query = post("/thirdparty/").content(payload).contentType(MediaType.APPLICATION_JSON);
        ensureNonAdminsCannotRunThisQuery(query);

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new Admin());
        MvcResult mvcResult = mockMvc.perform(query)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ThirdParty thirdParty = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ThirdParty.class);

        assertEquals(thirdParty.getName(), "thirdParty4");
        assertEquals(thirdParty.getHashedKey(), "hash4");
    }

    @Test
    void updateThirdParty() throws Exception {
        ThirdParty updatedThirdParty = this.thirdPartyRepository.findAll().get(0);
        updatedThirdParty.setName("updatedThirdParty");
        updatedThirdParty.setHashedKey("updatedHashedKey");
        String payload = objectMapper.writeValueAsString(updatedThirdParty);

        MockHttpServletRequestBuilder query = put("/thirdparty/").content(payload).contentType(MediaType.APPLICATION_JSON);
        ensureNonAdminsCannotRunThisQuery(query);

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new Admin());
        MvcResult mvcResult = mockMvc.perform(query)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ThirdParty fetchedThirdParty = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ThirdParty.class);
        assertEquals(updatedThirdParty.getId(), fetchedThirdParty.getId());
        assertEquals(updatedThirdParty.getName(), fetchedThirdParty.getName());
        assertEquals(updatedThirdParty.getHashedKey(), fetchedThirdParty.getHashedKey());
    }

    @Test
    void deleteThirdParty() throws Exception {
        ThirdParty thirdParty = this.thirdPartyRepository.findAll().get(0);

        MockHttpServletRequestBuilder query = delete("/thirdparty/" + thirdParty.getId());
        ensureNonAdminsCannotRunThisQuery(query);

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new Admin());
        mockMvc.perform(query)
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/thirdparty/" + thirdParty.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    public void ensureNonAdminsCannotRunThisQuery(MockHttpServletRequestBuilder query) throws Exception {
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(null);
        mockMvc.perform(query).andExpect(status().isForbidden());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new AccountHolder());
        mockMvc.perform(query).andExpect(status().isForbidden());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new ThirdParty());
        mockMvc.perform(query).andExpect(status().isForbidden());
    }
}
