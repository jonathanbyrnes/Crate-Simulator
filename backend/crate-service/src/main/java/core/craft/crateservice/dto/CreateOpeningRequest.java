package core.craft.crateservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOpeningRequest {

    @NotNull(message = "A crate must belong to a opening.")
    private Long crateId;

}