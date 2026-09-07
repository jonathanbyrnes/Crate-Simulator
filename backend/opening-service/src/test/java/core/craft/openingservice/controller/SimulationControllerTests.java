package core.craft.openingservice.controller;

import core.craft.openingservice.service.OpeningRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimulationController.class)
public class SimulationControllerTests {

    private final String baseEndpoint = "/api/crates/{crateId}/simulate";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpeningRequestService openingRequestService;

    @Test
    public void simulate_withExplicitCount() throws Exception {
        Long crateId = 2L;
        int count = 100;

        mockMvc.perform(post(baseEndpoint, crateId)
                        .param("count", String.valueOf(count)))
                .andExpect(status().isAccepted());

        verify(openingRequestService).requestOpenings(crateId, count);
    }

    @Test
    public void simulate_withDefaultCount() throws Exception {
        Long crateId = 2L;

        mockMvc.perform(post(baseEndpoint, crateId))
                .andExpect(status().isAccepted());

        verify(openingRequestService).requestOpenings(crateId, 1000);
    }

    @Test
    public void simulate_withMaxCount() throws Exception {
        mockMvc.perform(post(baseEndpoint, 2L)
                        .param("count", String.valueOf(SimulationController.MAX_COUNT)))
                .andExpect(status().isAccepted());

        verify(openingRequestService).requestOpenings(2L, SimulationController.MAX_COUNT);
    }

    @Test
    public void simulate_rejectsCountAboveMax() throws Exception {
        mockMvc.perform(post(baseEndpoint, 2L)
                        .param("count", String.valueOf(SimulationController.MAX_COUNT + 1)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(openingRequestService);
    }

    @Test
    public void simulate_rejectsZeroCount() throws Exception {
        mockMvc.perform(post(baseEndpoint, 2L)
                        .param("count", "0"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(openingRequestService);
    }
}
