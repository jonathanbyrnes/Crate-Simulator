package core.craft.openingservice.service;

import core.craft.openingservice.dto.CreateOpeningRequest;
import core.craft.openingservice.publisher.OpeningPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class OpeningRequestServiceImplTests {

    @Mock
    private OpeningPublisher publisher;

    @InjectMocks
    private OpeningRequestServiceImpl service;

    @Test
    public void requestOpening() {
        service.requestOpening(42L);

        ArgumentCaptor<CreateOpeningRequest> captor = ArgumentCaptor.forClass(CreateOpeningRequest.class);
        verify(publisher).publishOpening(captor.capture());
        assertThat(captor.getValue().getCrateId()).isEqualTo(42L);
    }

    @Test
    public void requestOpenings() {
        service.requestOpenings(42L, 3);

        ArgumentCaptor<CreateOpeningRequest> captor = ArgumentCaptor.forClass(CreateOpeningRequest.class);
        verify(publisher, times(3)).publishOpening(captor.capture());
        assertThat(captor.getAllValues()).allSatisfy(r -> assertThat(r.getCrateId()).isEqualTo(42L));
    }

    @Test
    public void requestOpeningsZero() {
        service.requestOpenings(42L, 0);

        verifyNoInteractions(publisher);
    }
}
