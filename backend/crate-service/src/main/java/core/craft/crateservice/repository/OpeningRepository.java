package core.craft.crateservice.repository;

import core.craft.crateservice.domain.Opening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningRepository extends JpaRepository<Opening,Long> {
}
