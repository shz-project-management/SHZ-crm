package CRM.entity.requests;

import CRM.entity.Item;
import CRM.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class CommentRequest extends SharedContentRequest{
}
