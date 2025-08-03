package core.craft.crateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrateDto {
    private Long id;
    private String name;
    private String description;
    private boolean approved;
}