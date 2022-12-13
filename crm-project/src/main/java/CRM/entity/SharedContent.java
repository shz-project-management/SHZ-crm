package CRM.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime creationDate;
    private String title;
    private String description;

    public User getCreator() {
        return user;
    }

    public void setCreator(User creator) {
        this.user = creator;
    }

//    public Item getItem() {
//        return item;
//    }
//
//    public void setItem(Item item) {
//        this.item = item;
//    }

}
