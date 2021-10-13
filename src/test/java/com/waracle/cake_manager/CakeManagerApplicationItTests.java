package com.waracle.cake_manager;

import com.waracle.cake_manager.repository.CakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests
 */
@SpringBootTest
class CakeManagerApplicationItTests { // As the class is not public, the name of it doesn't have to match the filename

    @Autowired
    private CakeRepository cakeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void contextLoads() {
        // Make sure the database is populated
        assertEquals(20, cakeRepository.count());
    }

    @Test
    void webappFetchesJsonData() throws Exception {
        mvc.perform(get("/cakes").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$[0].title", is("Lemon cheesecake")))
                .andExpect(jsonPath("$[19].title", is("Birthday cake")))
                .andExpect(jsonPath("$[19].description", is("a yearly treat")));
    }
}
