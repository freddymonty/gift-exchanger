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
public class GiftExchangeService {
    private final SessionRepository sessionRepository;
    private final TelnyxSmsService telnyxSmsService;

    public GiftExchangeService(SessionRepository sessionRepository, TelnyxSmsService telnyxSmsService) {
        this.sessionRepository = sessionRepository;
        this.telnyxSmsService = telnyxSmsService;
    }

    @Transactional
    public void executeGiftExchange(Integer sessionId) throws IllegalAccessException {
        // find session
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new IllegalAccessException("Session not found: " + sessionId));

        // prevent executing 2x
        if(session.isExecuted()){
            throw new IllegalStateException("Session is already executed");
        }

        List<Participant> participants = new ArrayList<>(session.getParticipants());

        if(participants.isEmpty() || participants.size() < 2){
            throw new IllegalAccessException("Session has no participants");
        }

        // make sure every phone is valid and consented
        for (Participant participant : participants) {

            if (!participant.isSmsConsent()) {
                throw new IllegalStateException(
                        participant.getName()
                                + " has not opted in to SMS notifications."
                );
            }

            if (participant.getPhoneNumber() == null ||
                    participant.getPhoneNumber().isBlank()) {

                throw new IllegalStateException(
                        participant.getName()
                                + " does not have a mobile phone number."
                );
            }
        }

        // ranomize participants

        Collections.shuffle(participants);

        // send everyone a sms

        for(int i = 0; i < participants.size(); i++){
            Participant giver =  participants.get(i);
            Participant receiver = participants.get((i + 1) % participants.size());

            String message = "Hi " + giver.getName() + "! " + "You are giving a gift to " + receiver.getName() + ". \uD83E\uDD2B\uD83E\uDD2B\uD83E\uDD2B";

            telnyxSmsService.sendSms(giver.getPhoneNumber(), message);
        }

        // finally once all telnyx requests go thru, erase numbers

        for (Participant participant : participants) {
            participant.setPhoneNumber(null);
        }

        // set execute to true
        session.setExecuted(true);
        sessionRepository.save(session);
        System.out.println("Gift exchange success.");
    }
}