package CRM.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@Builder
public class Response {
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("data")
    private Object data;
    @JsonProperty("statusCode")
    private Integer statusCode;
}