package CRM.entity.DTO;

import CRM.entity.*;
import lombok.*;

import java.util.*;


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

    public static <T> List<AttributeDTO> getListOfAttributesFromDB(Set<T> attributes){
        List<AttributeDTO> attributeDTOList = new ArrayList<>();
        for (T attribute: attributes) {
            attributeDTOList.add(createAttributeDTO((Attribute) attribute));
        }
        attributeDTOList.sort(Comparator.comparingLong(AttributeDTO::getId));

        return attributeDTOList;
    }
}

