package core.craft.crateservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRewardRequest {

    @NotBlank(message = "A reward name is required.")
    private String name;

    @Size(max = 252, message = "A reward description cannot be more than 252 characters.")
    @Nullable
    private String description;

    @NotNull
    private double weight;
}