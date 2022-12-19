package CRM.entity.DTO;

import java.time.LocalDateTime;

public class ItemDTO {
    private ItemDTO parentItem;
    private UserDTO user;
    private LocalDateTime creationDate;
    private String title;
    private String description;
    private AttributeDTO status;
    private AttributeDTO type;
    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
}
