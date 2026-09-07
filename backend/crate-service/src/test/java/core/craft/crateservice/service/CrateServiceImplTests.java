package core.craft.crateservice.service;

import core.craft.crateservice.domain.Crate;
import core.craft.crateservice.dto.CrateDto;
import core.craft.crateservice.dto.CreateCrateRequest;
import core.craft.crateservice.exception.CrateNotFoundException;
import core.craft.crateservice.repository.CrateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CrateServiceImplTests {

    @Mock
    private CrateRepository repository;

    @InjectMocks
    private CrateServiceImpl service;

    private Crate crate(Long id, String name, String description, boolean approved) {
        return Crate.builder().id(id).name(name).description(description).approved(approved).build();
    }

    @Test
    public void create() {
        CreateCrateRequest request = new CreateCrateRequest("Test Crate", "Test Description");
        when(repository.save(any(Crate.class)))
                .thenReturn(crate(1L, "Test Crate", "Test Description", false));

        CrateDto result = service.create(request);

        ArgumentCaptor<Crate> captor = ArgumentCaptor.forClass(Crate.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getName()).isEqualTo("Test Crate");
        assertThat(captor.getValue().getDescription()).isEqualTo("Test Description");
        assertThat(captor.getValue().isApproved()).isFalse();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Crate");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.isApproved()).isFalse();
    }

    @Test
    public void update() {
        Crate existing = crate(2L, "Old Name", "Old Description", true);
        when(repository.findById(2L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        CrateDto result = service.update(2L, new CreateCrateRequest("New Name", "New Description"));

        assertThat(existing.getName()).isEqualTo("New Name");
        assertThat(existing.getDescription()).isEqualTo("New Description");
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getDescription()).isEqualTo("New Description");
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    public void updateNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(2L, new CreateCrateRequest("New Name", "New Description")))
                .isInstanceOf(CrateNotFoundException.class)
                .hasMessage("Crate not found with ID: 2");
        verify(repository, never()).save(any());
    }

    @Test
    public void findById() {
        when(repository.findById(3L)).thenReturn(Optional.of(crate(3L, "Crate", "Desc", true)));

        CrateDto result = service.findById(3L);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Crate");
        assertThat(result.getDescription()).isEqualTo("Desc");
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    public void findByIdNotFound() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(3L))
                .isInstanceOf(CrateNotFoundException.class)
                .hasMessage("Crate not found with ID: 3");
    }

    @Test
    public void findAll() {
        when(repository.findAll()).thenReturn(List.of(
                crate(1L, "One", "First", false),
                crate(2L, "Two", "Second", true)));

        List<CrateDto> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("One");
        assertThat(result.get(0).getDescription()).isEqualTo("First");
        assertThat(result.get(0).isApproved()).isFalse();
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("Two");
        assertThat(result.get(1).getDescription()).isEqualTo("Second");
        assertThat(result.get(1).isApproved()).isTrue();
    }

    @Test
    public void findAllEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        assertThat(service.findAll()).isEmpty();
    }

    @Test
    public void deleteSuccess() {
        when(repository.existsById(4L)).thenReturn(true);

        service.delete(4L);

        verify(repository).deleteById(4L);
    }

    @Test
    public void deleteNotFound() {
        when(repository.existsById(4L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(4L))
                .isInstanceOf(CrateNotFoundException.class)
                .hasMessage("Crate not found with ID: 4");
        verify(repository, never()).deleteById(any());
    }

    @Test
    public void approve() {
        Crate existing = crate(5L, "Crate", "Desc", false);
        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        CrateDto result = service.approve(5L);

        assertThat(existing.isApproved()).isTrue();
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    public void approveNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approve(5L))
                .isInstanceOf(CrateNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    public void disapprove() {
        Crate existing = crate(6L, "Crate", "Desc", true);
        when(repository.findById(6L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        CrateDto result = service.disapprove(6L);

        assertThat(existing.isApproved()).isFalse();
        assertThat(result.getId()).isEqualTo(6L);
        assertThat(result.isApproved()).isFalse();
    }

    @Test
    public void disapproveNotFound() {
        when(repository.findById(6L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.disapprove(6L))
                .isInstanceOf(CrateNotFoundException.class);
        verify(repository, never()).save(any());
    }
}
