package core.craft.rewardservice.repository;

import core.craft.rewardservice.TestcontainersConfiguration;
import core.craft.rewardservice.domain.Reward;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class RewardRepositoryTests {

    @Autowired
    private RewardRepository repository;

    private Reward reward(Long crateId, String name, double weight, boolean approved) {
        return Reward.builder().crateId(crateId).name(name).weight(weight).approved(approved).build();
    }

    @Test
    public void findByCrateId() {
        repository.save(reward(1L, "One", 5, true));
        repository.save(reward(1L, "Two", 4, false));
        repository.save(reward(2L, "Other", 3, true));

        List<Reward> result = repository.findByCrateId(1L);

        assertThat(result).extracting(Reward::getName).containsExactlyInAnyOrder("One", "Two");
        assertThat(result).allSatisfy(r -> assertThat(r.getCrateId()).isEqualTo(1L));
    }

    @Test
    public void findByCrateIdEmpty() {
        repository.save(reward(1L, "One", 5, true));

        assertThat(repository.findByCrateId(99L)).isEmpty();
    }

    @Test
    public void approvedDefaultsToFalse() {
        Reward saved = repository.save(Reward.builder().crateId(1L).name("One").weight(5).build());

        assertThat(repository.findById(saved.getId())).get()
                .extracting(Reward::isApproved).isEqualTo(false);
    }
}
