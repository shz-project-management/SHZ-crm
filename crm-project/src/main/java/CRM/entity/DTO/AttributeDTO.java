package CRM.entity.DTO;

import CRM.entity.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttributeDTO {

    private Long id;
    private Long belongsToBoard;
    private String name;
    private String description;

    public static AttributeDTO createAttributeDTO(Attribute attribute){
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setId(attributeDTO.getId());
        attributeDTO.setBelongsToBoard(attribute.getBoard().getId());
        attributeDTO.setName(attribute.getName());
        attributeDTO.setDescription(attributeDTO.getDescription());
        return attributeDTO;
    }
}
