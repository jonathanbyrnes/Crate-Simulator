package core.craft.rewardservice.service;

import core.craft.rewardservice.exception.CrateNotApprovedException;
import core.craft.rewardservice.domain.Reward;
import core.craft.rewardservice.dto.CrateDto;
import core.craft.rewardservice.dto.CreateRewardRequest;
import core.craft.rewardservice.dto.RewardDto;
import core.craft.rewardservice.dto.UpdateRewardRequest;
import core.craft.rewardservice.exception.CrateNotFoundException;
import core.craft.rewardservice.exception.RewardNotFoundException;
import core.craft.rewardservice.feign.RewardInterface;
import core.craft.rewardservice.repository.RewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardServiceImplTests {

    @Mock
    private RewardRepository repository;

    @Mock
    private RewardInterface rewardInterface;

    @InjectMocks
    private RewardServiceImpl service;

    private Reward reward(Long id, Long crateId, String name, String description, double weight, boolean approved) {
        return Reward.builder().id(id).crateId(crateId).name(name)
                .description(description).weight(weight).approved(approved).build();
    }

    private ResponseEntity<CrateDto> crateFound(Long crateId) {
        return ResponseEntity.ok(new CrateDto(crateId, "Crate", "Desc", true));
    }

    @Test
    public void create() {
        when(rewardInterface.get(42L)).thenReturn(crateFound(42L));
        when(repository.save(any(Reward.class)))
                .thenReturn(reward(1L, 42L, "Test Reward", "Test Desc", 5, false));

        RewardDto result = service.create(new CreateRewardRequest(42L, "Test Reward", "Test Desc", 5.0));

        ArgumentCaptor<Reward> captor = ArgumentCaptor.forClass(Reward.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getCrateId()).isEqualTo(42L);
        assertThat(captor.getValue().getName()).isEqualTo("Test Reward");
        assertThat(captor.getValue().getDescription()).isEqualTo("Test Desc");
        assertThat(captor.getValue().getWeight()).isEqualTo(5);
        assertThat(captor.getValue().isApproved()).isFalse();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCrateId()).isEqualTo(42L);
        assertThat(result.getName()).isEqualTo("Test Reward");
        assertThat(result.getDescription()).isEqualTo("Test Desc");
        assertThat(result.getWeight()).isEqualTo(5);
        assertThat(result.isApproved()).isFalse();
    }

    @Test
    public void createCrateNotFoundWhenNoBody() {
        when(rewardInterface.get(42L)).thenReturn(ResponseEntity.ok().build());

        assertThatThrownBy(() -> service.create(new CreateRewardRequest(42L, "Test Reward", "Test Desc", 5.0)))
                .isInstanceOf(CrateNotFoundException.class)
                .hasMessage("Crate not found with ID: 42");
        verify(repository, never()).save(any());
    }

    @Test
    public void createCrateNotFoundWhenNot2xx() {
        when(rewardInterface.get(42L))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CrateDto()));

        assertThatThrownBy(() -> service.create(new CreateRewardRequest(42L, "Test Reward", "Test Desc", 5.0)))
                .isInstanceOf(CrateNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    public void update() {
        Reward existing = reward(2L, 42L, "Old Name", "Old Desc", 1, true);
        when(repository.findById(2L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        RewardDto result = service.update(2L, new UpdateRewardRequest("New Name", "New Desc", 7.0));

        assertThat(existing.getCrateId()).isEqualTo(42L);
        assertThat(existing.getName()).isEqualTo("New Name");
        assertThat(existing.getDescription()).isEqualTo("New Desc");
        assertThat(existing.getWeight()).isEqualTo(7);
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getCrateId()).isEqualTo(42L);
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getDescription()).isEqualTo("New Desc");
        assertThat(result.getWeight()).isEqualTo(7);
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    public void updateNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(2L, new UpdateRewardRequest("New Name", "New Desc", 7.0)))
                .isInstanceOf(RewardNotFoundException.class)
                .hasMessage("Reward not found with ID: 2");
        verify(repository, never()).save(any());
    }

    @Test
    public void findById() {
        when(repository.findById(3L)).thenReturn(Optional.of(reward(3L, 42L, "Reward", "Desc", 5, true)));

        RewardDto result = service.findById(3L);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getCrateId()).isEqualTo(42L);
        assertThat(result.getName()).isEqualTo("Reward");
        assertThat(result.getDescription()).isEqualTo("Desc");
        assertThat(result.getWeight()).isEqualTo(5);
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    public void findByIdNotFound() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(3L))
                .isInstanceOf(RewardNotFoundException.class)
                .hasMessage("Reward not found with ID: 3");
    }

    @Test
    public void findByCrateId() {
        when(rewardInterface.get(42L)).thenReturn(crateFound(42L));
        when(repository.findByCrateId(42L)).thenReturn(List.of(
                reward(1L, 42L, "One", "First", 5, false),
                reward(2L, 42L, "Two", "Second", 4, true)));

        List<RewardDto> result = service.findByCrateId(42L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getCrateId()).isEqualTo(42L);
        assertThat(result.get(0).getName()).isEqualTo("One");
        assertThat(result.get(0).getDescription()).isEqualTo("First");
        assertThat(result.get(0).getWeight()).isEqualTo(5);
        assertThat(result.get(0).isApproved()).isFalse();
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("Two");
        assertThat(result.get(1).isApproved()).isTrue();
    }

    @Test
    public void findByCrateIdEmpty() {
        when(rewardInterface.get(42L)).thenReturn(crateFound(42L));
        when(repository.findByCrateId(42L)).thenReturn(List.of());

        assertThat(service.findByCrateId(42L)).isEmpty();
    }

    @Test
    public void findByCrateIdCrateNotFoundWhenNoBody() {
        when(rewardInterface.get(42L)).thenReturn(ResponseEntity.ok().build());

        assertThatThrownBy(() -> service.findByCrateId(42L))
                .isInstanceOf(CrateNotFoundException.class)
                .hasMessage("Crate not found with ID: 42");
        verify(repository, never()).findByCrateId(any());
    }

    @Test
    public void findByCrateIdCrateNotFoundWhenNot2xx() {
        when(rewardInterface.get(42L))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CrateDto()));

        assertThatThrownBy(() -> service.findByCrateId(42L))
                .isInstanceOf(CrateNotFoundException.class);
        verify(repository, never()).findByCrateId(any());
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
                .isInstanceOf(RewardNotFoundException.class)
                .hasMessage("Reward not found with ID: 4");
        verify(repository, never()).deleteById(any());
    }

    @Test
    public void approve() {
        Reward existing = reward(5L, 42L, "Reward", "Desc", 5, false);
        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        RewardDto result = service.approve(5L);

        assertThat(existing.isApproved()).isTrue();
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    public void approveNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approve(5L))
                .isInstanceOf(RewardNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    public void disapprove() {
        Reward existing = reward(6L, 42L, "Reward", "Desc", 5, true);
        when(repository.findById(6L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        RewardDto result = service.disapprove(6L);

        assertThat(existing.isApproved()).isFalse();
        assertThat(result.getId()).isEqualTo(6L);
        assertThat(result.isApproved()).isFalse();
    }

    @Test
    public void disapproveNotFound() {
        when(repository.findById(6L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.disapprove(6L))
                .isInstanceOf(RewardNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    public void findApprovedByCrateId() {
        when(rewardInterface.get(42L)).thenReturn(crateFound(42L));
        when(repository.findByCrateIdAndApprovedTrue(42L)).thenReturn(List.of(
                reward(2L, 42L, "Two", "Second", 4, true)));

        List<RewardDto> result = service.findApprovedByCrateId(42L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).isApproved()).isTrue();
        verify(repository, never()).findByCrateId(any());
    }

    @Test
    public void findApprovedByCrateIdCrateNotApproved() {
        when(rewardInterface.get(42L)).thenReturn(ResponseEntity.ok(new CrateDto(42L, "Crate", "Desc", false)));

        assertThatThrownBy(() -> service.findApprovedByCrateId(42L))
                .isInstanceOf(CrateNotApprovedException.class)
                .hasMessage("Crate is not approved with ID: 42");
        verify(repository, never()).findByCrateIdAndApprovedTrue(any());
    }

    @Test
    public void findApprovedByCrateIdCrateNotFound() {
        when(rewardInterface.get(42L)).thenReturn(ResponseEntity.ok().build());

        assertThatThrownBy(() -> service.findApprovedByCrateId(42L))
                .isInstanceOf(CrateNotFoundException.class);
        verify(repository, never()).findByCrateIdAndApprovedTrue(any());
    }
}
