package nl.abnamro.assessment.recipe.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * @author Orhan Polat
 */
@Getter
@EqualsAndHashCode
public class Message {

    private final String code;
    private final String message;
    private final Severity severity;

    public Message(String code, String message, Severity severity) {
        this.code = code;
        this.message = message;
        this.severity = severity;
    }

    @Override
    public String toString(){
        return "{" + "code : '" + code + '\'' + ", field:'" + ", message:'" + message + '\'' + ", severity : " + severity + '}';
    }
}
