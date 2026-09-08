package core.craft.openingservice.controller;

import core.craft.openingservice.dto.OpeningSummaryDto;
import core.craft.openingservice.dto.RewardOpeningCountDto;
import core.craft.openingservice.exception.RewardForCrateNotFoundException;
import core.craft.openingservice.service.OpeningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpeningSummaryController.class)
public class OpeningSummaryControllerTests {

    private final String baseEndpoint = "/api/crates/{crateId}/openings";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpeningService openingService;

    @Test
    public void summarise() throws Exception {
        when(openingService.summarise(2L)).thenReturn(new OpeningSummaryDto(2L, 5, List.of(
                new RewardOpeningCountDto(10L, "Common", 4),
                new RewardOpeningCountDto(11L, "Rare", 1))));

        mockMvc.perform(get(baseEndpoint, 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.crateId").value(2L))
                .andExpect(jsonPath("$.total").value(5))
                .andExpect(jsonPath("$.rewards.length()").value(2))
                .andExpect(jsonPath("$.rewards[0].rewardId").value(10L))
                .andExpect(jsonPath("$.rewards[0].rewardName").value("Common"))
                .andExpect(jsonPath("$.rewards[0].count").value(4))
                .andExpect(jsonPath("$.rewards[1].rewardName").value("Rare"));
    }

    @Test
    public void summariseCrateNotFound() throws Exception {
        when(openingService.summarise(99L)).thenThrow(new RewardForCrateNotFoundException(99L));

        mockMvc.perform(get(baseEndpoint, 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Rewards for crate: 99 cannot be found."));
    }
}
