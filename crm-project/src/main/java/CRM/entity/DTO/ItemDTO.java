package CRM.entity.DTO;

import CRM.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Set;

public class ItemDTO {
    private ItemDTO parentItem;
    private UserDTO user;
    private LocalDateTime creationDate;
    private String title;
    private String description;
//    private BoardDTO board;
    private AttributeDTO status;
    private AttributeDTO type;
    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
}
