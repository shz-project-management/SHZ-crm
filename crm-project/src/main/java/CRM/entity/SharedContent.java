package CRM.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class SharedContent {
    @Id
    @GeneratedValue
//    @GenericGenerator(
//            name = "sequence-generator",
//            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
//            @org.hibernate.annotations.Parameter(name = "initial_value", value = "0")
//    })
    private Long id;
    private Long parentId;
    private Long userCreatorId;
    private LocalDateTime creationDate;
    private String title;
    private String description;

}
