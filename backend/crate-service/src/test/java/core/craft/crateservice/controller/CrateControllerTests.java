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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrateController.class)
public class CrateControllerTests {

    private final String baseEndpoint = "/api/crates";
    private final String crateTargettedEndpoint = baseEndpoint.concat("/{crateId}");

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
                "Test Crate", "Test Description", false);

        when(crateService.create(any(CreateCrateRequest.class)))
                .thenReturn(savedOutput);

        mockMvc.perform(post(baseEndpoint)
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
                new CrateDto(1L, "Crate One", "First", false),
                new CrateDto(2L, "Crate Two", "Second", false)
        );

        when(crateService.findAll()).thenReturn(crates);

        mockMvc.perform(MockMvcRequestBuilders.get(baseEndpoint))
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
                "Test Crate", "Test Desc", false);

        when(crateService.findById(42L)).thenReturn(output);

        mockMvc.perform(MockMvcRequestBuilders.get(crateTargettedEndpoint,
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
                "Test Crate", "Test Description", false);

        when(crateService.update(eq(2L), any(CreateCrateRequest.class)))
                .thenReturn(savedOutput);

        mockMvc.perform(put(crateTargettedEndpoint, 2L)
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

        mockMvc.perform(MockMvcRequestBuilders.delete(crateTargettedEndpoint, 3L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUnsuccessful() throws Exception {
        willThrow(new CrateNotFoundException(4L))
                .given(crateService).delete(4L);

        mockMvc.perform(MockMvcRequestBuilders.delete(crateTargettedEndpoint, 4L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void approveSuccess() throws Exception {
        CrateDto approvedDto = new CrateDto(5L,
                "Some Crate", "Desc", true);

        given(crateService.approve(5L)).willReturn(approvedDto);

        mockMvc.perform(patch(crateTargettedEndpoint + "/approve", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.approved").value(true));
    }

    @Test
    public void approveUnsuccessful() throws Exception {
        given(crateService.approve(6L))
                .willThrow(new CrateNotFoundException(6L));

        mockMvc.perform(patch(crateTargettedEndpoint + "/approve", 6L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void disapproveSuccess() throws Exception {
        CrateDto disapprovedDto = new CrateDto(5L,
                "Some Crate", "Desc", false);

        given(crateService.disapprove(5L)).willReturn(disapprovedDto);

        mockMvc.perform(patch(crateTargettedEndpoint + "/disapprove", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.approved").value(false));
    }

    @Test
    public void disapproveUnsuccessful() throws Exception {
        given(crateService.disapprove(6L)).
                willThrow(new CrateNotFoundException(6L));

        mockMvc.perform(patch(crateTargettedEndpoint + "/disapprove", 6L))
                .andExpect(status().isNotFound());
    }

}
