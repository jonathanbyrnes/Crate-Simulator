package core.craft.crateservice.domain;

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

    @ManyToOne(optional = false)
    @JoinColumn(name="crate_id", nullable = false)
    private Crate crate;

    @ManyToOne(optional = false)
    @JoinColumn(name="reward_id", nullable = false)
    private Reward reward;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;
}
