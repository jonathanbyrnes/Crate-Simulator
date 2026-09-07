package core.craft.rewardservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRewardRequest {

    @NotNull(message = "A reward must belong to a crate.")
    private Long crateId;

    @NotBlank(message = "A reward name is required.")
    private String name;

    @Size(max = 252, message = "A reward description cannot be more than 252 characters.")
    @Nullable
    private String description;

    @NotNull(message = "A reward weight is required.")
    @Positive(message = "A reward weight must be greater than zero.")
    private Double weight;
}