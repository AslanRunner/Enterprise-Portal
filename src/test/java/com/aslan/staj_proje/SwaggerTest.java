package com.aslan.staj_proje;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class SwaggerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSwagger() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andDo(print());
    }
}
