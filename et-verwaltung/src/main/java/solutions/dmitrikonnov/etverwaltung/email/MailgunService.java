package solutions.dmitrikonnov.etverwaltung.email;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class MailgunService implements EmailSender{

    @Value("${mailgun.api.privatekey}")
    private final String apiKey;

    @Override
    @Async
    public void send(String to, String email) {
        MailgunMessagesApi mgmApi = MailgunClient.config(apiKey).createApi(MailgunMessagesApi.class);

        Message msg = Message.builder()
                .from("registration@mail.dmitrikonnov.solutions")
                .to(to)
                .subject("Confirm your email")
                .text(email)
                .build();

        var response = sendWithResponse(mgmApi, msg);
        log.info("response id = " + response.getId() +" response message = " + response.getMessage());

    }

    public MessageResponse sendWithResponse( MailgunMessagesApi mgmApi, Message msg){
        return mgmApi.sendMessage("mail.dmitrikonnov.solutions", msg);
    }
}
