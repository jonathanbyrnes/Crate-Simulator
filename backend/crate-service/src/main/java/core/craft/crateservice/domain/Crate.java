package core.craft.crateservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "crate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description", length = 252)
    private String description;

    @Column(name = "approved", nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private boolean approved = false;

}
