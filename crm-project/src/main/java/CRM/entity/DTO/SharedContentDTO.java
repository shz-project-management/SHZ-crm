package CRM.entity.DTO;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedContentDTO {
    private ItemDTO parentItem;
    private UserDTO user;
    private LocalDateTime creationDate;
    private String title;
    private String description;
}
