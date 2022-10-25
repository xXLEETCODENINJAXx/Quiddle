package com.quiddle.quiddleApplication.integration.controllers;

import com.quiddle.quiddleApplication.constants.AppConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {

    private static final String GET_ALL_ROLES_URL = "/roles";
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void all() throws Exception {
        mvc.perform(get(GET_ALL_ROLES_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.*", hasSize(AppConstants.DEFAULT_ROLES_PERMISSIONS.size())));
    }
}