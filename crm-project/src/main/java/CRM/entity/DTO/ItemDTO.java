package CRM.entity.DTO;

import CRM.entity.Item;
import CRM.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO extends SharedContentDTO {

    private Long id;
    private AttributeDTO status;
    private AttributeDTO type;
    private Long sectionId;
    private UserDTO assignedToUser;
    private LocalDateTime dueDate;
    private Integer importance;
    private Long boardId;
    private List<ItemDTO> subItems;

    public static ItemDTO getSharedContentFromDB(Item item) {
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setUser(UserDTO.createUserDTO(item.getUser()));
        itemDTO.setTitle(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setType(item.getType() == null ? null : AttributeDTO.createAttributeDTO(item.getType()));
        itemDTO.setStatus(item.getStatus() == null ? null : AttributeDTO.createAttributeDTO(item.getStatus()));
        itemDTO.setSectionId(item.getSection().getId());
        itemDTO.setCreationDate(item.getCreationDate());
        itemDTO.setId(item.getId());
        itemDTO.setImportance(item.getImportance());
        itemDTO.setDueDate(item.getDueDate());

        if(item.getAssignedToUser() != null){
            itemDTO.setAssignedToUser(UserDTO.createUserDTO(item.getAssignedToUser())); // FIXME: don't return the creator user -> return the assigned user
        }

        if (item.getItems().size() > 0)
            itemDTO.setSubItems(ItemDTO.getItemsDTOList(item.getItems()));

        return itemDTO;
    }

    public static ItemDTO getParentItem(Item item){
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setUser(UserDTO.createUserDTO(item.getUser()));
        itemDTO.setTitle(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setType(item.getType() == null ? null : AttributeDTO.createAttributeDTO(item.getType()));
        itemDTO.setStatus(item.getStatus() == null ? null : AttributeDTO.createAttributeDTO(item.getStatus()));
        itemDTO.setSectionId(item.getSection().getId());
        itemDTO.setCreationDate(item.getCreationDate());
        itemDTO.setId(item.getId());
        itemDTO.setImportance(item.getImportance());
        itemDTO.setDueDate(item.getDueDate());

        if(item.getAssignedToUser() != null){
            itemDTO.setAssignedToUser(UserDTO.createUserDTO(item.getAssignedToUser())); // FIXME: don't return the creator user -> return the assigned user
        }

        return itemDTO;
    }

    public static List<ItemDTO> getItemsDTOList(Set<Item> items) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for (Item item : items) {
            itemDTOList.add(getSharedContentFromDB(item));
        }

        itemDTOList.sort(Comparator.comparingLong(ItemDTO::getId));

        return itemDTOList;
    }
}
