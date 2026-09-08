package core.craft.openingservice.service;

import core.craft.openingservice.dto.OpeningDto;
import core.craft.openingservice.dto.OpeningSummaryDto;

public interface OpeningService {
    OpeningDto open(Long crateId);
    OpeningSummaryDto summarise(Long crateId);
}
