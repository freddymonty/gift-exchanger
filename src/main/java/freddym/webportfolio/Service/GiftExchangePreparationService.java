package freddym.webportfolio.Service;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GiftExchangePreparationService {

    private final SessionRepository sessionRepository;

    public GiftExchangePreparationService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void prepare(Integer sessionId, Integer ownerId) {
        Session session = sessionRepository.findOwnedByIdForUpdate(sessionId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
        List<Participant> participants = new ArrayList<>(session.getParticipants());

        if (session.isExecuted()) {
            boolean retryable = !participants.isEmpty()
                    && participants.stream().allMatch(participant ->
                    participant.isAssignmentDelivered() || participant.getAssignedRecipientName() != null)
                    && participants.stream().anyMatch(participant -> !participant.isAssignmentDelivered());

            if (retryable) {
                return;
            }
            throw new IllegalStateException("Session is already executed");
        }

        if (participants.size() < 2) {
            throw new IllegalStateException("Session must have at least two participants");
        }

        for (Participant participant : participants) {
            if (!participant.isSmsConsent()) {
                throw new IllegalStateException(
                        participant.getName() + " has not opted in to SMS notifications."
                );
            }
            if (participant.getPhoneNumber() == null || participant.getPhoneNumber().isBlank()) {
                throw new IllegalStateException(
                        participant.getName() + " does not have a mobile phone number."
                );
            }
        }

        Collections.shuffle(participants);
        for (int i = 0; i < participants.size(); i++) {
            Participant giver = participants.get(i);
            Participant receiver = participants.get((i + 1) % participants.size());
            giver.setAssignedRecipientName(receiver.getName());
            giver.setAssignmentDelivered(false);
        }

        // Closing registration and saving every assignment happen in the same transaction.
        // SMS delivery starts only after this method commits successfully.
        session.setExecuted(true);
        sessionRepository.save(session);
    }
}
