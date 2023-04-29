package me.frankv.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest(classes = CoreApplicationTests.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
//@EmbeddedKafka(ports = {9092})
class CoreApplicationTests {

    @Autowired
    MockMvc mockMvc;


    @Test
    void run() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/test_pair/add")
                .requestAttr("amount", "200")
                .requestAttr("price", "500")
                .requestAttr("type", "buy"))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }


}
