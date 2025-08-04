package core.craft.crateservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "reward")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @Column(name="crate", nullable = false)
    private Crate crate;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description", length = 252)
    private String description;

    @Column(name="weight", nullable = false)
    private double weight;
}
