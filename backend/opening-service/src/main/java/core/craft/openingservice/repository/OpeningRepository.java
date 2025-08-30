package core.craft.openingservice.repository;

import core.craft.openingservice.domain.Opening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpeningRepository extends JpaRepository<Opening,Long> {
    List<Opening> findByCrateId(Long crateId);
}
