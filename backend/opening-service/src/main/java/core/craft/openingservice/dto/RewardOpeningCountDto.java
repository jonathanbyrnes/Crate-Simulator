package core.craft.openingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RewardOpeningCountDto {
    private Long rewardId;
    private String rewardName;
    private long count;
}
