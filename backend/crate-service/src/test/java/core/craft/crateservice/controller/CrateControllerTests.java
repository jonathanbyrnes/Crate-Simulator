package core.craft.crateservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.craft.crateservice.dto.CrateDto;
import core.craft.crateservice.dto.CreateCrateRequest;
import core.craft.crateservice.exception.CrateNotFoundException;
import core.craft.crateservice.service.CrateService;
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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrateController.class)
public class CrateControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrateService crateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create() throws Exception {
        CreateCrateRequest input = new CreateCrateRequest(
                "Test Crate", "Test Description");

        String requestBody = objectMapper.writeValueAsString(input);

        CrateDto savedOutput = new CrateDto(1L,
                "Test Crate", "Test Description");

        when(crateService.create(any(CreateCrateRequest.class)))
                .thenReturn(savedOutput);

        mockMvc.perform(post("/api/crates")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedOutput.getId()))
                .andExpect(jsonPath("$.name").value(savedOutput.getName()))
                .andExpect(jsonPath("$.description").value(savedOutput.getDescription()));
    }

    @Test
    public void list() throws Exception {
        List<CrateDto> crates = List.of(
                new CrateDto(1L, "Crate One", "First"),
                new CrateDto(2L, "Crate Two", "Second")
        );

        when(crateService.findAll()).thenReturn(crates);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/crates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Crate One"))
                .andExpect(jsonPath("$[0].description").value("First"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Crate Two"))
                .andExpect(jsonPath("$[1].description").value("Second"));
    }

    @Test
    public void get() throws Exception {
        CrateDto output = new CrateDto(42L,
                "Test Crate", "Test Desc");

        when(crateService.findById(42L)).thenReturn(output);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/crates/{crateId}",
                        42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("Test Crate"))
                .andExpect(jsonPath("$.description").value("Test Desc"));
    }

    @Test
    public void update() throws Exception {
        CreateCrateRequest input = new CreateCrateRequest(
                "Test Crate", "Test Description");

        String requestBody = objectMapper.writeValueAsString(input);

        CrateDto savedOutput = new CrateDto(2L,
                "Test Crate", "Test Description");

        when(crateService.update(eq(2L), any(CreateCrateRequest.class)))
                .thenReturn(savedOutput);

        mockMvc.perform(put("/api/crates/{crateId}", 2L)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Test Crate"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    public void deleteSuccess() throws Exception {
        willDoNothing().given(crateService).delete(3L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/crates/{crateId}", 3L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUnsuccessful() throws Exception {
        willThrow(new CrateNotFoundException(4L))
                .given(crateService).delete(4L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/crates/{crateId}", 4L))
                .andExpect(status().isNotFound());
    }
}
