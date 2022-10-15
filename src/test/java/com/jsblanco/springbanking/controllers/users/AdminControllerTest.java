package com.jsblanco.springbanking.controllers.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.repositories.users.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AdminRepository adminRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        adminRepository.save(new Admin("admin1", randomUUID().toString(),"password1"));
        adminRepository.save(new Admin("admin2", randomUUID().toString(), "password2"));
        adminRepository.save(new Admin( "admin3", randomUUID().toString(), "password3"));
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void getAdminById() throws Exception {
        Admin dbAdmin = this.adminRepository.findAll().get(0);
        MvcResult mvcResult = mockMvc.perform(get("/admin/"+dbAdmin.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Admin fetchedAdmin = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Admin.class);

        assertEquals(fetchedAdmin.getId(), dbAdmin.getId());
        assertEquals(fetchedAdmin.getName(), dbAdmin.getName());
    }

    @Test
    void getAllAdmins() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/admin/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Admin> adminList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(3, adminList.size());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("admin1"));
    }

    @Test
    void saveAdmin() throws Exception {
        Admin newAdmin = new Admin("admin4", randomUUID().toString(), "password4");
        String payload = objectMapper.writeValueAsString(newAdmin);
        MvcResult mvcResult = mockMvc.perform(post("/admin/")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Admin admin = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Admin.class);

        assertEquals(admin.getName(), "admin4");
    }

    @Test
    void updateAdmin() throws Exception {
        Admin updatedAdmin = this.adminRepository.findAll().get(0);
        updatedAdmin.setName("updatedAdmin");

        String payload = objectMapper.writeValueAsString(updatedAdmin);
        MvcResult mvcResult = mockMvc.perform(put("/admin/")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Admin fetchedAdmin = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Admin.class);
        assertEquals(updatedAdmin.getId(), fetchedAdmin.getId());
        assertEquals(updatedAdmin.getName(), fetchedAdmin.getName());


    }

    @Test
    void deleteAdmin() throws Exception {
        Admin admin = this.adminRepository.findAll().get(0);

        mockMvc.perform(delete("/admin/" + admin.getId()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/admin/" + admin.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
