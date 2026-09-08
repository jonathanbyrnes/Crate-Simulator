package core.craft.openingservice.repository;

import core.craft.openingservice.TestcontainersConfiguration;
import core.craft.openingservice.domain.Opening;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class OpeningRepositoryTests {

    @Autowired
    private OpeningRepository repository;

    private Opening opening(Long crateId, Long rewardId) {
        return new Opening(null, crateId, rewardId, Instant.now());
    }

    @Test
    public void findByCrateId() {
        repository.save(opening(1L, 10L));
        repository.save(opening(1L, 11L));
        repository.save(opening(2L, 12L));

        List<Opening> result = repository.findByCrateId(1L);

        assertThat(result).extracting(Opening::getRewardId).containsExactlyInAnyOrder(10L, 11L);
        assertThat(result).allSatisfy(o -> assertThat(o.getCrateId()).isEqualTo(1L));
    }

    @Test
    public void findByCrateIdEmpty() {
        repository.save(opening(1L, 10L));

        assertThat(repository.findByCrateId(99L)).isEmpty();
    }

    @Test
    public void countByRewardForCrate() {
        repository.save(opening(1L, 10L));
        repository.save(opening(1L, 10L));
        repository.save(opening(1L, 11L));
        repository.save(opening(2L, 10L));

        List<RewardOpeningCount> result = repository.countByRewardForCrate(1L);

        assertThat(result).hasSize(2);
        assertThat(result).filteredOn(c -> c.getRewardId().equals(10L)).singleElement()
                .extracting(RewardOpeningCount::getCount).isEqualTo(2L);
        assertThat(result).filteredOn(c -> c.getRewardId().equals(11L)).singleElement()
                .extracting(RewardOpeningCount::getCount).isEqualTo(1L);
    }

    @Test
    public void countByRewardForCrateEmpty() {
        repository.save(opening(1L, 10L));

        assertThat(repository.countByRewardForCrate(99L)).isEmpty();
    }
}
