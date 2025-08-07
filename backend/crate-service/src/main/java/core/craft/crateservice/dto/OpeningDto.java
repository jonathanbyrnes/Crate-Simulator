package core.craft.crateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpeningDto {
    private Long id;
    private Long crateId;
    private Long rewardId;
    private String rewardName;
    private Instant timestamp;
}