package CRM.entity;

import CRM.entity.DTO.AttributeDTO;
import CRM.entity.DTO.UserDTO;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.RegisterUserRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@MappedSuperclass
public class SharedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "parent_item")
    @JsonIgnore
    private Item parentItem;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User user;

    private LocalDateTime creationDate = LocalDateTime.now();

    private String name;
    private String description;

    public static SharedContent createSharedContentItemForTests(){
        Item resItem = new Item();
        resItem.setId(1L);
        resItem.setUser(User.newUser(new RegisterUserRequest()));
        resItem.setName("name");
        resItem.setDescription("desc");
        resItem.setType(Type.createTypeAttribute("name", "desc"));
        resItem.setStatus(Status.createStatusAttribute("name", "desc"));
        resItem.setSection(Section.createSection(new AttributeRequest(1L,"name", "desc")));
        resItem.setCreationDate(LocalDateTime.now());
        resItem.setId(1L);
        resItem.setImportance(1);
        resItem.setDueDate(LocalDateTime.now());

        return resItem;
    }

    public static SharedContent createSharedContentCommentForTests(){
        Comment resComment = new Comment();
        resComment.setId(1L);
        resComment.setUser(User.newUser(new RegisterUserRequest()));
        resComment.setName("name");
        resComment.setDescription("desc");
        resComment.setParentItem((Item) createSharedContentItemForTests());
        resComment.setCreationDate(LocalDateTime.now());
        resComment.setId(1L);

        return resComment;
    }
}
