package freddym.webportfolio.Service;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Repository.ParticipantRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GiftExchangeServiceRetryTests {

    @Test
    void retrySendsOnlyPendingAssignments() {
        GiftExchangePreparationService preparationService = mock(GiftExchangePreparationService.class);
        ParticipantRepository participantRepository = mock(ParticipantRepository.class);
        SmsNotificationService smsNotificationService = mock(SmsNotificationService.class);
        GiftExchangeService service = new GiftExchangeService(
                preparationService,
                participantRepository,
                smsNotificationService
        );
        Participant alice = participant("Alice", "+15550000001", "Bob");
        Participant bob = participant("Bob", "+15550000002", "Alice");
        when(participantRepository.findBySessionId(12)).thenReturn(List.of(alice, bob));
        doThrow(new IllegalStateException("Telnyx unavailable"))
                .when(smsNotificationService).sendAssignment(
                        "+15550000002", "Bob", "Alice"
                );

        assertThrows(IllegalStateException.class, () -> service.executeGiftExchange(12, 7));

        assertTrue(alice.isAssignmentDelivered());
        assertNull(alice.getPhoneNumber());
        assertNull(alice.getAssignedRecipientName());
        assertFalse(bob.isAssignmentDelivered());
        verify(participantRepository).saveAndFlush(alice);

        doNothing().when(smsNotificationService).sendAssignment(
                "+15550000002", "Bob", "Alice"
        );
        service.executeGiftExchange(12, 7);

        assertTrue(bob.isAssignmentDelivered());
        assertNull(bob.getPhoneNumber());
        assertNull(bob.getAssignedRecipientName());
        verify(smsNotificationService, times(1)).sendAssignment(
                "+15550000001", "Alice", "Bob"
        );
        verify(smsNotificationService, times(2)).sendAssignment(
                "+15550000002", "Bob", "Alice"
        );
        verify(preparationService, times(2)).prepare(12, 7);
    }

    private Participant participant(String name, String phoneNumber, String assignedRecipientName) {
        Participant participant = new Participant();
        participant.setName(name);
        participant.setPhoneNumber(phoneNumber);
        participant.setAssignedRecipientName(assignedRecipientName);
        return participant;
    }
}
