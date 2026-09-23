package freddym.webportfolio.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService {

    private final TelnyxSmsService telnyxSmsService;
    private final String brandName;
    private final String supportEmail;
    private final String websiteUrl;

    public SmsNotificationService(
            TelnyxSmsService telnyxSmsService,
            @Value("${app.messaging.brand-name}") String brandName,
            @Value("${app.messaging.support-email}") String supportEmail,
            @Value("${app.messaging.website-url}") String websiteUrl
    ) {
        this.telnyxSmsService = telnyxSmsService;
        this.brandName = brandName;
        this.supportEmail = supportEmail;
        this.websiteUrl = websiteUrl;
    }

    public void sendOptInConfirmation(String phoneNumber) {
        telnyxSmsService.sendSms(
                phoneNumber,
                brandName + ": You opted in for gift exchange texts. Up to 2 messages per session. "
                        + "Message and data rates may apply. Reply STOP to opt out or HELP for help."
        );
    }

    public void sendAssignment(String phoneNumber, String participantName, String recipientName) {
        telnyxSmsService.sendSms(
                phoneNumber,
                brandName + ": Hi " + participantName + ", your gift recipient is " + recipientName
                        + ". Keep it secret! Reply STOP to opt out or HELP for help."
        );
    }

    public String getHelpMessage() {
        return brandName + ": For help, contact " + supportEmail + " or visit " + websiteUrl
                + ". Reply STOP to opt out.";
    }

    public String getBrandName() {
        return brandName;
    }

    public String getFromNumber() {
        return telnyxSmsService.getFromNumber();
    }
}
