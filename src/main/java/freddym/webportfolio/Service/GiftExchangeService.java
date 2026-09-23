package freddym.webportfolio.Service;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftExchangeService {
    private final GiftExchangePreparationService preparationService;
    private final ParticipantRepository participantRepository;
    private final SmsNotificationService smsNotificationService;

    public GiftExchangeService(
            GiftExchangePreparationService preparationService,
            ParticipantRepository participantRepository,
            SmsNotificationService smsNotificationService
    ) {
        this.preparationService = preparationService;
        this.participantRepository = participantRepository;
        this.smsNotificationService = smsNotificationService;
    }

    public void executeGiftExchange(Integer sessionId, Integer ownerId) {
        preparationService.prepare(sessionId, ownerId);

        List<Participant> participants = participantRepository.findBySessionId(sessionId);
        for (Participant participant : participants) {
            if (participant.isAssignmentDelivered()) {
                continue;
            }

            smsNotificationService.sendAssignment(
                    participant.getPhoneNumber(),
                    participant.getName(),
                    participant.getAssignedRecipientName()
            );

            // Persist progress after each accepted message. A retry skips this participant.
            participant.setAssignmentDelivered(true);
            participant.setPhoneNumber(null);
            participant.setAssignedRecipientName(null);
            participantRepository.saveAndFlush(participant);
        }
    }
}
