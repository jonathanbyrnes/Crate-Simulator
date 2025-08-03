package core.craft.crateservice.service;

import core.craft.crateservice.domain.Crate;
import core.craft.crateservice.dto.CrateDto;
import core.craft.crateservice.dto.CreateCrateRequest;
import core.craft.crateservice.exception.CrateNotFoundException;
import core.craft.crateservice.repository.CrateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CrateService {

    private final CrateRepository repository;

    public CrateDto create(CreateCrateRequest request) {
        return saveAndReturnDto(new Crate(), request);
    }

    public CrateDto update(Long crateId, CreateCrateRequest request) {
        Crate searchedForCrate = getCrateById(crateId);
        return saveAndReturnDto(searchedForCrate, request);
    }

    @Transactional(readOnly = true)
    public CrateDto findById(Long crateId) {
        return repository.findById(crateId)
                .map(this::toDto)
                .orElseThrow(() -> new CrateNotFoundException(crateId));
    }

    @Transactional(readOnly = true)
    public List<CrateDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public void delete(Long crateId) {
        if (!repository.existsById(crateId)) {
            throw new CrateNotFoundException(crateId);
        }
        repository.deleteById(crateId);
    }

    public CrateDto approve(Long crateId) {
        return setApproval(crateId, true);
    }

    public CrateDto disapprove(Long crateId) {
        return setApproval(crateId, false);
    }

    private Crate getCrateById(Long crateId) {
        return repository.findById(crateId)
                .orElseThrow(() -> new CrateNotFoundException(crateId));
    }

    private CrateDto toDto(Crate crate) {
        return new CrateDto(crate.getId(), crate.getName(), crate.getDescription(), crate.isApproved());
    }

    private void mapRequestToEntity(CreateCrateRequest request, Crate crate) {
        crate.setName(request.getName());
        crate.setDescription(request.getDescription());
    }

    private CrateDto saveAndReturnDto(Crate crate, CreateCrateRequest request) {
        mapRequestToEntity(request, crate);
        return toDto(repository.save(crate));
    }

    private CrateDto setApproval(Long crateId, boolean approved) {
        Crate searchedForCrate = getCrateById(crateId);
        searchedForCrate.setApproved(approved);
        return toDto(repository.save(searchedForCrate));
    }


}
