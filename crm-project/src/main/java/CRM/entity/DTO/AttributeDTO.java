package CRM.entity.DTO;

import CRM.entity.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


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
        attributeDTO.setId(attribute.getId());
        attributeDTO.setName(attribute.getName());
        attributeDTO.setDescription(attribute.getDescription());
        return attributeDTO;
    }

    public static List<AttributeDTO> createListOfAttributesDTO(List<Attribute> attributes){
        List<AttributeDTO> attributeDTOS = new ArrayList<>();
        for (Attribute attribute: attributes) {
            AttributeDTO attributeDTO = new AttributeDTO();
            attributeDTO.setId(attribute.getId());
            attributeDTO.setName(attribute.getName());
            attributeDTO.setDescription(attribute.getDescription());
            attributeDTOS.add(attributeDTO);
        }
        return attributeDTOS;
    }
}

