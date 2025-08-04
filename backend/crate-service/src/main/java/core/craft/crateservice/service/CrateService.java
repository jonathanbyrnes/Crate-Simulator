package core.craft.crateservice.service;

import core.craft.crateservice.dto.CrateDto;
import core.craft.crateservice.dto.CreateCrateRequest;

import java.util.List;

public interface CrateService {
    CrateDto create(CreateCrateRequest request);
    CrateDto update(Long crateId, CreateCrateRequest request);
    CrateDto findById(Long crateId);
    List<CrateDto> findAll();
    void delete(Long crateId);
    CrateDto approve(Long crateId);
    CrateDto disapprove(Long crateId);
}
