package core.craft.openingservice.service;

import core.craft.openingservice.domain.Opening;
import core.craft.openingservice.dto.OpeningDto;
import core.craft.openingservice.dto.RewardDto;
import core.craft.openingservice.exception.ApprovedRewardNotFoundException;
import core.craft.openingservice.exception.RewardForCrateNotFoundException;
import core.craft.openingservice.exception.RewardNotFoundException;
import core.craft.openingservice.feign.OpeningInterface;
import core.craft.openingservice.repository.OpeningRepository;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpeningServiceImplTests {

    @Mock
    private OpeningRepository repository;

    @Mock
    private OpeningInterface openingInterface;

    @InjectMocks
    private OpeningServiceImpl service;

    private RewardDto reward(Long id, String name, double weight) {
        return new RewardDto(id, 42L, name, "Desc", weight, true);
    }

    @Test
    public void openSingleReward() {
        Instant before = Instant.now();
        when(openingInterface.listByCrate(42L)).thenReturn(ResponseEntity.ok(List.of(reward(7L, "Only", 5))));
        when(openingInterface.get(7L)).thenReturn(ResponseEntity.ok(reward(7L, "Only", 5)));

        OpeningDto result = service.open(42L);

        ArgumentCaptor<Opening> captor = ArgumentCaptor.forClass(Opening.class);
        verify(repository).save(captor.capture());
        Opening saved = captor.getValue();
        assertThat(saved.getCrateId()).isEqualTo(42L);
        assertThat(saved.getRewardId()).isEqualTo(7L);
        assertThat(saved.getTimestamp()).isBetween(before, Instant.now());

        assertThat(result.getCrateId()).isEqualTo(42L);
        assertThat(result.getRewardId()).isEqualTo(7L);
        assertThat(result.getRewardName()).isEqualTo("Only");
        assertThat(result.getTimestamp()).isEqualTo(saved.getTimestamp());
    }

    @Test
    public void openSkipsZeroWeightRewards() {
        when(openingInterface.listByCrate(42L)).thenReturn(ResponseEntity.ok(List.of(
                reward(1L, "Never", 0),
                reward(2L, "Always", 5),
                reward(3L, "Never", 0))));
        when(openingInterface.get(2L)).thenReturn(ResponseEntity.ok(reward(2L, "Always", 5)));

        for (int i = 0; i < 100; i++) {
            assertThat(service.open(42L).getRewardId()).isEqualTo(2L);
        }
    }

    @Test
    public void openRewardsForCrateNotFound() {
        when(openingInterface.listByCrate(42L)).thenReturn(ResponseEntity.ok().build());

        assertThatThrownBy(() -> service.open(42L))
                .isInstanceOf(RewardForCrateNotFoundException.class)
                .hasMessage("Rewards for crate: 42 cannot be found.");
        verify(repository, never()).save(any());
    }

    @Test
    public void openNoApprovedRewards() {
        when(openingInterface.listByCrate(42L)).thenReturn(ResponseEntity.ok(List.of()));

        assertThatThrownBy(() -> service.open(42L))
                .isInstanceOf(ApprovedRewardNotFoundException.class)
                .hasMessage("No approved rewards in crate: 42");
        verify(repository, never()).save(any());
    }

    @Test
    public void openSelectedRewardNotFound() {
        when(openingInterface.listByCrate(42L)).thenReturn(ResponseEntity.ok(List.of(reward(7L, "Only", 5))));
        when(openingInterface.get(7L)).thenReturn(ResponseEntity.ok().build());

        assertThatThrownBy(() -> service.open(42L))
                .isInstanceOf(RewardNotFoundException.class)
                .hasMessage("Reward not found with ID: 7");
    }

    private FeignException.NotFound notFound() {
        Request request = Request.create(Request.HttpMethod.GET, "/", Map.of(), null, StandardCharsets.UTF_8, null);
        return new FeignException.NotFound("not found", request, null, null);
    }

    @Test
    public void openRewardsForCrateFeignNotFound() {
        when(openingInterface.listByCrate(42L)).thenThrow(notFound());

        assertThatThrownBy(() -> service.open(42L))
                .isInstanceOf(RewardForCrateNotFoundException.class)
                .hasMessage("Rewards for crate: 42 cannot be found.");
        verify(repository, never()).save(any());
    }

    @Test
    public void openSelectedRewardFeignNotFound() {
        when(openingInterface.listByCrate(42L)).thenReturn(ResponseEntity.ok(List.of(reward(7L, "Only", 5))));
        when(openingInterface.get(7L)).thenThrow(notFound());

        assertThatThrownBy(() -> service.open(42L))
                .isInstanceOf(RewardNotFoundException.class)
                .hasMessage("Reward not found with ID: 7");
    }
}
