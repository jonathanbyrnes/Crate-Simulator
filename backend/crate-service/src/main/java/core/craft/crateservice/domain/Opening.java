package core.craft.crateservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Entity
@Table(name = "opening")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Opening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @Column(name="crate", nullable = false)
    private Crate crate;

    @ManyToOne(optional = false)
    @Column(name="reward", nullable = false)
    private Reward reward;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;
}
