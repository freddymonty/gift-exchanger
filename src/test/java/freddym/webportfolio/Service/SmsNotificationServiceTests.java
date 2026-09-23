package freddym.webportfolio.Service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SmsNotificationServiceTests {

    @Test
    void messagesAreBrandedAndContainRequiredKeywords() {
        TelnyxSmsService telnyxSmsService = mock(TelnyxSmsService.class);
        when(telnyxSmsService.getFromNumber()).thenReturn("+18885550123");
        SmsNotificationService service = new SmsNotificationService(
                telnyxSmsService,
                "Freddy's Gift Exchange",
                "support@freddymcode.com",
                "https://www.freddymcode.com"
        );

        service.sendOptInConfirmation("+15550000001");
        ArgumentCaptor<String> confirmation = ArgumentCaptor.forClass(String.class);
        verify(telnyxSmsService).sendSms(
                org.mockito.ArgumentMatchers.eq("+15550000001"),
                confirmation.capture()
        );

        assertTrue(confirmation.getValue().startsWith("Freddy's Gift Exchange:"));
        assertTrue(confirmation.getValue().contains("STOP"));
        assertTrue(confirmation.getValue().contains("HELP"));
        assertTrue(service.getHelpMessage().contains("support@freddymcode.com"));
        assertEquals("+18885550123", service.getFromNumber());
    }
}
