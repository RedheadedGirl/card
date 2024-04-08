package ru.sbrf.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.sbrf.dto.ClientInput;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardEndpointTest extends EndpointTest {

    @Nested
    class CreateCard {

        @Test
        @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        public void givenCreatedUser_whenCreateCard_thenCreate() throws Exception {
            createClient();
            String createResult = createCard("1");
            assertFalse(createResult.isBlank());
        }

        @Test
        public void givenNoUser_whenCreateCard_thenThrow() throws Exception {
            String createResult = mockMvc.perform(post("/cards/new/9"))
                    .andExpect(status().is4xxClientError())
                    .andReturn().getResponse().getContentAsString();
            assertEquals("{\"code\":\"NOT_FOUND\",\"message\":\"Client with id = 9 not found\"}", createResult);
        }
    }

    @Nested
    class CloseCard {

        @Test
        public void givenCreatedUserWithCard_whenCloseCard_thenClose() throws Exception {
            createClient();
            createCard("1");
            mockMvc.perform(post("/cards/close/1"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        }

        @Test
        public void givenNoCard_whenCloseCard_thenThrow() throws Exception {
            String createResult = mockMvc.perform(post("/cards/close/8"))
                    .andExpect(status().is4xxClientError())
                    .andReturn().getResponse().getContentAsString();

            assertEquals("{\"code\":\"NOT_FOUND\",\"message\":\"Card with id = 8 not found\"}", createResult);
        }

    }

    @Nested
    class GetAll {

        @Test
        public void givenCreatedUserWithCards_whenGetAll_thenDo() throws Exception {
            createClient();
            createCard("1");
            createCard("1");

            String json = mockMvc.perform(get("/cards"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            JsonNode jsonNode = objectMapper.readTree(json);
            assertEquals(2, jsonNode.size());
            assertEquals("1", jsonNode.get(0).get("clientId").asText());
            assertEquals("1", jsonNode.get(1).get("clientId").asText());
            assertEquals("ACTIVE", jsonNode.get(0).get("status").asText());
            assertEquals("ACTIVE", jsonNode.get(1).get("status").asText());
        }
    }


    private void createClient() throws Exception {
        ClientInput clientInput = new ClientInput("fio", LocalDate.of(2024, 11, 11), "email1@ya.ru", "12345678");

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientInput)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private String createCard(String clientId) throws Exception {
        return mockMvc.perform(post("/cards/new/" + clientId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

}