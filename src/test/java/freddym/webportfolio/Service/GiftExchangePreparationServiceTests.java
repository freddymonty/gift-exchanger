package freddym.webportfolio.Service;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Repository.SessionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GiftExchangePreparationServiceTests {

    @Test
    void assignmentsAreSavedBeforeDeliveryBegins() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        GiftExchangePreparationService service = new GiftExchangePreparationService(sessionRepository);
        Participant alice = participant("Alice", "+15550000001");
        Participant bob = participant("Bob", "+15550000002");
        Session session = new Session();
        session.setParticipants(List.of(alice, bob));
        when(sessionRepository.findOwnedByIdForUpdate(12, 7)).thenReturn(Optional.of(session));

        service.prepare(12, 7);

        assertTrue(session.isExecuted());
        assertEquals("Bob", alice.getAssignedRecipientName());
        assertEquals("Alice", bob.getAssignedRecipientName());
        assertFalse(alice.isAssignmentDelivered());
        assertFalse(bob.isAssignmentDelivered());
        verify(sessionRepository).save(session);
    }

    @Test
    void partialDeliveryReusesTheSavedAssignments() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        GiftExchangePreparationService service = new GiftExchangePreparationService(sessionRepository);
        Participant alice = participant("Alice", null);
        alice.setAssignmentDelivered(true);
        Participant bob = participant("Bob", "+15550000002");
        bob.setAssignedRecipientName("Alice");
        Session session = new Session();
        session.setExecuted(true);
        session.setParticipants(List.of(alice, bob));
        when(sessionRepository.findOwnedByIdForUpdate(12, 7)).thenReturn(Optional.of(session));

        service.prepare(12, 7);

        assertNull(alice.getAssignedRecipientName());
        assertEquals("Alice", bob.getAssignedRecipientName());
        verify(sessionRepository, never()).save(session);
    }

    private Participant participant(String name, String phoneNumber) {
        Participant participant = new Participant();
        participant.setName(name);
        participant.setPhoneNumber(phoneNumber);
        participant.setSmsConsent(true);
        return participant;
    }
}
