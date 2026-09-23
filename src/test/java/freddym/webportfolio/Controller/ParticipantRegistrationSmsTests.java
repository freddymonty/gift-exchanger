package freddym.webportfolio.Controller;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.ParticipantRepository;
import freddym.webportfolio.Repository.SessionRepository;
import freddym.webportfolio.Repository.UserRepository;
import freddym.webportfolio.Service.GiftExchangeService;
import freddym.webportfolio.Service.SmsNotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParticipantRegistrationSmsTests {

    @Test
    void consentingParticipantIsSavedBeforeConfirmationIsSent() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        ParticipantRepository participantRepository = mock(ParticipantRepository.class);
        SmsNotificationService smsNotificationService = mock(SmsNotificationService.class);
        IndexController controller = new IndexController(
                new UserBean(),
                mock(UserRepository.class),
                sessionRepository,
                participantRepository,
                mock(GiftExchangeService.class),
                smsNotificationService
        );
        Session session = new Session();
        session.setId(12);
        when(sessionRepository.findById(12)).thenReturn(Optional.of(session));
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String view = controller.registerParticipant(
                12,
                "Alice",
                "+15550000001",
                true,
                redirectAttributes
        );

        ArgumentCaptor<Participant> participant = ArgumentCaptor.forClass(Participant.class);
        verify(participantRepository).saveAndFlush(participant.capture());
        verify(smsNotificationService).sendOptInConfirmation("+15550000001");
        assertTrue(participant.getValue().isSmsConsent());
        assertEquals("redirect:/registerParticipant/12", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("success"));
    }
}
