package CRM.entity;

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
}
