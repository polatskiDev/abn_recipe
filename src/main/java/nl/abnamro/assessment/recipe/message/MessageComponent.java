package nl.abnamro.assessment.recipe.message;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Orhan Polat
 */
@AllArgsConstructor
@Component
public class MessageComponent {

    private final MessageSource messageSource;

    public Message getErrorMessage(String code) {
        return new Message(code, messageSource.getMessage(code, new String[] {}, Locale.getDefault()), Severity.error);
    }

    public Message getInfoMessage(String code) {
        return new Message(code, messageSource.getMessage(code, new String[] {}, Locale.getDefault()), Severity.info);
    }
}
