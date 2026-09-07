package core.craft.openingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpeningSummaryDto {
    private Long crateId;
    private long total;
    private List<RewardOpeningCountDto> rewards;
}
