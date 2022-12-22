package CRM.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    public static Attribute createAttribute(String name, String description, Class clz){
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setDescription(Objects.requireNonNullElse(description, ""));
        return attribute;
    }
}
