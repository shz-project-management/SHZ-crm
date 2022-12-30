package CRM.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * A class that represents a response object with a message, status, data, and status code.
 *
 * @param <T> The type of the data field in the response object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@Builder
public class Response<T> {
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("data")
    private T data;
    @JsonProperty("statusCode")
    private Integer statusCode;
}