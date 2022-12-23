package CRM.entity.DTO;

import CRM.entity.Item;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO extends SharedContentDTO {

    private AttributeDTO status;
    private AttributeDTO type;
    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
    private Long boardId;

    public static ItemDTO getSharedContentFromDB(Item item) {
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setImportance(item.getImportance());
        if (item.getParentItem() != null)
            itemDTO.setParentItem(ItemDTO.getSharedContentFromDB(item.getParentItem()));
        itemDTO.setUser(UserDTO.getUserFromDB(item.getUser()));
        itemDTO.setTitle(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setType(AttributeDTO.createAttributeDTO(item.getType()));
        itemDTO.setStatus(AttributeDTO.createAttributeDTO(item.getStatus()));
        itemDTO.setBoardId(item.getSection().getId());
        itemDTO.setCreationDate(item.getCreationDate());

        return itemDTO;
    }
}
