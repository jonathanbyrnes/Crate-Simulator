package core.craft.openingservice.listener;

import core.craft.openingservice.dto.CreateOpeningRequest;
import core.craft.openingservice.service.OpeningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OpeningListenerTests {

    @Mock
    private OpeningService openingService;

    @InjectMocks
    private OpeningListener listener;

    @Test
    public void onOpeningRequested() {
        listener.onOpeningRequested(new CreateOpeningRequest(42L));

        verify(openingService).open(42L);
    }
}
