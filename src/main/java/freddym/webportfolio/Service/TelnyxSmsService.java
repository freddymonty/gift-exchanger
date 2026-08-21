package freddym.webportfolio.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class TelnyxSmsService {

    private final RestClient restClient;
    private final String fromNumber;

    public TelnyxSmsService(@Value("${telnyx.api-key}") String apiKey, @Value("${telnyx.from-number}")String fromNumber) {
        this.fromNumber = fromNumber;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telnyx.com/v2")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }

    public void sendSms(String to, String text){
        Map<String, String> requestBody = Map.of("from", fromNumber, "to", to, "text", text);
        restClient.post().uri("/messages").body(requestBody).retrieve().toBodilessEntity();
    }
}

