package CRM.entity.DTO;


import CRM.entity.Board;
import CRM.entity.Item;
import CRM.entity.Section;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SectionDTO {

    private Long id;
    private String name;
    private String description;
    private List<ItemDTO> items;

    public static SectionDTO createSectionDTO(Section section){
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(section.getId());
        sectionDTO.setItems(ItemDTO.getItemsDTOList(section.getItems()));
        sectionDTO.setName(section.getName());
        sectionDTO.setDescription(section.getDescription());
        return sectionDTO;
    }

    public static List<SectionDTO> getSectionsDTOList(Set<Section> sections){
        List<SectionDTO> sectionDTOList = new ArrayList<>();
        for (Section section: sections) {
            sectionDTOList.add(createSectionDTO(section));
        }
        return sectionDTOList;
    }
}
