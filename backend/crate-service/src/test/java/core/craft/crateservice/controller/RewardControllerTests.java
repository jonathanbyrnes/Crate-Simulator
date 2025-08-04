package core.craft.crateservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.craft.crateservice.dto.*;
import core.craft.crateservice.exception.RewardNotFoundException;
import core.craft.crateservice.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardController.class)
public class RewardControllerTests {

    private final String baseEndpoint = "/api/rewards";
    private final String rewardTargettedEndpoint = baseEndpoint.concat("/{rewardId}");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create() throws Exception {
        CreateRewardRequest input = new CreateRewardRequest(
                1L, "Test Reward", "Test Desc", 5
        );

        String requestBody = objectMapper.writeValueAsString(input);

        RewardDto savedOutput = new RewardDto(
                1L, 1L, "Test Reward", "Test Desc", 5, false
        );

        when(rewardService.create(any(CreateRewardRequest.class)))
                .thenReturn(savedOutput);

        mockMvc.perform(post(baseEndpoint)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedOutput.getId()))
                .andExpect(jsonPath("$.crateId").value(savedOutput.getCrateId()))
                .andExpect(jsonPath("$.name").value(savedOutput.getName()))
                .andExpect(jsonPath("$.description").value(savedOutput.getDescription()))
                .andExpect(jsonPath("$.approved").value(false));
    }

    @Test
    public void listByCrate() throws Exception {
        List<RewardDto> rewards = List.of(
                new RewardDto(1L,42L, "Reward One", "First", 5, false),
                new RewardDto(2L, 42L, "Reward Two", "Second", 4, false)
        );

        when(rewardService.findByCrateId(42L)).thenReturn(rewards);

        mockMvc.perform(MockMvcRequestBuilders.get(baseEndpoint + "/crate/{crateId}", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].crateId").value(42L))
                .andExpect(jsonPath("$[0].name").value("Reward One"))
                .andExpect(jsonPath("$[0].description").value("First"))
                .andExpect(jsonPath("$[0].weight").value(5))
                .andExpect(jsonPath("$[0].approved").value(false))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].crateId").value(42L))
                .andExpect(jsonPath("$[1].name").value("Reward Two"))
                .andExpect(jsonPath("$[1].description").value("Second"))
                .andExpect(jsonPath("$[1].weight").value(4))
                .andExpect(jsonPath("$[1].approved").value(false));
    }

    @Test
    public void get() throws Exception {
        RewardDto output = new RewardDto(42L,42L,
                "Test Reward", "Test Desc", 5, false);

        when(rewardService.findById(42L)).thenReturn(output);

        mockMvc.perform(MockMvcRequestBuilders.get(rewardTargettedEndpoint,
                        42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.crateId").value(42L))
                .andExpect(jsonPath("$.name").value("Test Reward"))
                .andExpect(jsonPath("$.description").value("Test Desc"))
                .andExpect(jsonPath("$.weight").value(5))
                .andExpect(jsonPath("$.approved").value(false));
    }

    @Test
    public void update() throws Exception {
        UpdateRewardRequest input = new UpdateRewardRequest(
                "Test Reward", "Test Desc", 5);

        String requestBody = objectMapper.writeValueAsString(input);

        RewardDto savedOutput = new RewardDto(2L, 2L,
                "Test Reward", "Test Desc", 5, false);

        when(rewardService.update(eq(2L), any(UpdateRewardRequest.class)))
                .thenReturn(savedOutput);

        mockMvc.perform(put(rewardTargettedEndpoint, 2L)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Test Reward"))
                .andExpect(jsonPath("$.description").value("Test Desc"))
                .andExpect(jsonPath("$.weight").value(5))
                .andExpect(jsonPath("$.approved").value(false));
    }

    @Test
    public void deleteSuccess() throws Exception {
        willDoNothing().given(rewardService).delete(3L);

        mockMvc.perform(MockMvcRequestBuilders.delete(rewardTargettedEndpoint, 3L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUnsuccessful() throws Exception {
        willThrow(new RewardNotFoundException(4L))
                .given(rewardService).delete(4L);

        mockMvc.perform(MockMvcRequestBuilders.delete(rewardTargettedEndpoint, 4L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void approveSuccess() throws Exception {
        RewardDto approvedDto = new RewardDto(5L, 5L, "Some Crate",
                "Some Desc", 5, true);

        given(rewardService.approve(5L)).willReturn(approvedDto);

        mockMvc.perform(patch(rewardTargettedEndpoint + "/approve", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.approved").value(true));
    }

    @Test
    public void approveUnsuccessful() throws Exception {
        given(rewardService.approve(6L))
                .willThrow(new RewardNotFoundException(6L));

        mockMvc.perform(patch(rewardTargettedEndpoint + "/approve", 6L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void disapproveSuccess() throws Exception {
        RewardDto disapprovedDto = new RewardDto(5L, 5L, "Some Crate",
                "Some Desc", 5, false);

        given(rewardService.disapprove(5L)).willReturn(disapprovedDto);

        mockMvc.perform(patch(rewardTargettedEndpoint + "/disapprove", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.approved").value(false));
    }

    @Test
    public void disapproveUnsuccessful() throws Exception {
        given(rewardService.disapprove(6L)).
                willThrow(new RewardNotFoundException(6L));

        mockMvc.perform(patch(rewardTargettedEndpoint + "/disapprove", 6L))
                .andExpect(status().isNotFound());
    }

}
