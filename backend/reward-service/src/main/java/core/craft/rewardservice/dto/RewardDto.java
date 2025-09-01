package core.craft.rewardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RewardDto {
    private Long id;
    private Long crateId;
    private String name;
    private String description;
    private double weight;
    private boolean approved;
}