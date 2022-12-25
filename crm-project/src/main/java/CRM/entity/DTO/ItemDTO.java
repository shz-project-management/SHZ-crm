package CRM.entity.DTO;

import CRM.entity.Item;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO extends SharedContentDTO {

    private AttributeDTO status;
    private AttributeDTO type;
    private Long section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
    private Long boardId;

    public static ItemDTO getSharedContentFromDB(Item item) {
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setImportance(item.getImportance());
        if (item.getParentItem() != null)
            itemDTO.setParentItem(ItemDTO.getSharedContentFromDB(item.getParentItem()));
        itemDTO.setUser(UserDTO.createUserDTO(item.getUser()));
        itemDTO.setTitle(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setType(AttributeDTO.createAttributeDTO(item.getType()));
        itemDTO.setStatus(AttributeDTO.createAttributeDTO(item.getStatus()));
        itemDTO.setBoardId(item.getSection().getId());
        itemDTO.setSection(item.getSection().getId());
        itemDTO.setCreationDate(item.getCreationDate());

        return itemDTO;
    }

    public static List<ItemDTO> getItemsDTOList(Set<Item> items){
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for (Item item: items) {
            itemDTOList.add(getSharedContentFromDB(item));
        }
        return itemDTOList;
    }
}
