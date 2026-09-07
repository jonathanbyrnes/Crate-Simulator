package core.craft.openingservice.repository;

import core.craft.openingservice.domain.Opening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpeningRepository extends JpaRepository<Opening,Long> {
    List<Opening> findByCrateId(Long crateId);

    @Query("select o.rewardId as rewardId, count(o) as count from Opening o where o.crateId = :crateId group by o.rewardId")
    List<RewardOpeningCount> countByRewardForCrate(Long crateId);
}
