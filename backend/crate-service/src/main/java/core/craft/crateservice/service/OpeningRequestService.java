package core.craft.crateservice.service;

public interface OpeningRequestService {
    void requestOpening(Long crateId);
    void requestOpenings(Long crateId, int count);
}
