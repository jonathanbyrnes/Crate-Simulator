package core.craft.crateservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.craft.crateservice.dto.CreateOpeningRequest;
import core.craft.crateservice.dto.OpeningDto;
import core.craft.crateservice.service.OpeningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpeningController.class)
public class OpeningControllerTests {

    private final String baseEndpoint = "/api/crates/{crateId}/open";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpeningService openingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void open() throws Exception {
        Long crateId = 2L;

        OpeningDto output = new OpeningDto(
                1L, crateId, 42L,
                "Reward", Instant.parse("2025-08-04T12:00:00Z")
        );

        given(openingService.open(crateId)).willReturn(output);

        mockMvc.perform(post(baseEndpoint, crateId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(output.getId()))
                .andExpect(jsonPath("$.crateId").value(crateId))
                .andExpect(jsonPath("$.rewardName").value("Reward"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
