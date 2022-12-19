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

    public static ItemDTO getItemFromDB(Item item) {
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setImportance(item.getImportance());
        if (item.getParentItem() != null)
            itemDTO.setParentItem(ItemDTO.getItemFromDB(item.getParentItem()));
        itemDTO.setUser(UserDTO.getUserFromDB(item.getUser()));
        itemDTO.setTitle(item.getTitle());
        itemDTO.setDescription(itemDTO.getDescription());
        itemDTO.setType(AttributeDTO.createAttributeDTO(item.getStatus()));
        itemDTO.setStatus(AttributeDTO.createAttributeDTO(item.getStatus()));
        itemDTO.setBoardId(item.getBoard().getId());

        return itemDTO;
    }
}
