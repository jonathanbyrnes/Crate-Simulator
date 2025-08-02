package core.craft.crateservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCrateRequest {

    @NotBlank(message = "A crate name is required.")
    private String name;

    @Size(max = 252, message = "A crate description cannot be more than 252 characters.")
    @Nullable
    private String description;
}
