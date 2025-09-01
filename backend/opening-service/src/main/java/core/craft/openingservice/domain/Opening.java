package core.craft.openingservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "opening")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="crate_id", nullable = false)
    private Long crateId;

    @Column(name="reward_id", nullable = false)
    private Long rewardId;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;
}
