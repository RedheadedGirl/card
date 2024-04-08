package ru.sbrf.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.sbrf.dto.ClientInput;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClientEndpointTest extends EndpointTest {

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenCreatedUsers_whenGetAll_thenReturn() throws Exception {
        String createClientResult1 = createClient("email1@ya.ru", "12345678");
        assertEquals("null", createClientResult1);
        String createClientResult2 = createClient("email2@ya.ru", "87654321");
        assertEquals("null", createClientResult2);

        String getAllResult = mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = IntegrationUtil.readResourceToString("json/clients.json");
        assertEquals(getAllResult, expected);
    }

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenCreatedUser_whenCreateWithSameInn_thenReturnExistingId() throws Exception {
        String createClientResult1 = createClient("email1@ya.ru", "12345678");
        assertEquals("null", createClientResult1);
        String createClientResult2 = createClient("email2@ya.ru", "12345678");
        assertEquals("1", createClientResult2);
    }

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenCreatedUser_whenCreateWithSameEmail_thenBadRequest() throws Exception {
        String createClientResult1 = createClient("email1@ya.ru", "12345678");
        assertEquals(createClientResult1, "null");

        ClientInput clientInput = new ClientInput("fio", LocalDate.now(), "email1@ya.ru", "12345679");
        String contentAsString = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientInput)))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();
        assertEquals(contentAsString, "{\"code\":\"BAD_REQUEST\",\"message\":\"Email email1@ya.ru is already in use\"}");
    }

    private String createClient(String email, String inn) throws Exception {
        ClientInput clientInput = new ClientInput("fio", LocalDate.of(2024, 11, 11), email, inn);

        return mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientInput)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

}