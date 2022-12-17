package CRM.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String name;
    private String description;

    public static Attribute createAttribute(Board board, String name, String description){
        Attribute attribute = new Attribute();
        attribute.setBoard(board);
        attribute.setName(name);
        attribute.setDescription(Objects.requireNonNullElse(description, ""));
        return attribute;
    }
}
