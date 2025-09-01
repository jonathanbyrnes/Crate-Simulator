package core.craft.openingservice.controller;

import core.craft.openingservice.service.OpeningRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpeningController.class)
public class OpeningControllerTests {

    private final String baseEndpoint = "/api/crates/{crateId}/open";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpeningRequestService openingRequestService;

    @Test
    public void open() throws Exception {
        Long crateId = 2L;

        mockMvc.perform(post(baseEndpoint, crateId))
                .andExpect(status().isAccepted());

        verify(openingRequestService).requestOpening(crateId);
    }
}
