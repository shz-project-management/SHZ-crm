package CRM.entity.DTO;


import CRM.entity.Board;
import CRM.entity.Item;
import CRM.entity.Section;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

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

    public static SectionDTO createSectionDTO(Section section) {
        SectionDTO sectionDTO = new SectionDTO();

        sectionDTO.setId(section.getId());
        sectionDTO.setName(section.getName());
        sectionDTO.setDescription(section.getDescription());

        if (section.getItems().size() > 0)
            sectionDTO.setItems(ItemDTO.getItemsDTOList(section.getItems().stream().filter(item -> item.getParentItem() == null).collect(Collectors.toSet())));

        return sectionDTO;
    }

    public static List<SectionDTO> getSectionsDTOList(Set<Section> sections) {
        List<SectionDTO> sectionDTOList = new ArrayList<>();
        for (Section section : sections) {
            sectionDTOList.add(createSectionDTO(section));
        }
        Collections.sort(sectionDTOList, Comparator.comparingLong(SectionDTO::getId));

        return sectionDTOList;
    }
}
