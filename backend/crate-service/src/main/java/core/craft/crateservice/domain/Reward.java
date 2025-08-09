package core.craft.crateservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="crate_id", nullable = false)
    private Crate crate;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description", length = 252)
    private String description;

    @Column(name="weight", nullable = false)
    private double weight;

    @Column(name = "approved", nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private boolean approved = false;
}
