package nl.abnamro.assessment.recipe.message;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Orhan Polat
 */
@Data
public class RestResponse<T> {

    private T data;

    private HttpStatus status;

    private List<Message> messages = new ArrayList<>();

    public RestResponse(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
    }

    public static <T> RestResponse<T> of(T t, HttpStatus status) {
        return new RestResponse<>(t, status);
    }

    public static <T> RestResponse<T> of(T t, HttpStatus status, List<Message> messages) {
        RestResponse<T> restResponse = new RestResponse<>(t, status);
        restResponse.setMessages(messages);
        return restResponse;
    }

    public static <T> RestResponse<T> of(T t, HttpStatus status, Message message) {
        RestResponse<T> restResponse = new RestResponse<>(t, status);
        restResponse.getMessages().add(message);
        return restResponse;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
