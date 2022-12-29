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

    public static Attribute createAttribute(String name, String description){
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setDescription(Objects.requireNonNullElse(description, ""));
        return attribute;
    }

    public static Status createStatusAttribute(String name, String description) {
        Status status = new Status();
        status.setId(0L);
        status.setName(name);
        status.setDescription(description);
        return status;
    }


    public static Type createTypeAttribute(String name, String description) {
        Type type = new Type();
        type.setId(0L);
        type.setName(name);
        type.setDescription(description);
        return type;
    }
}
