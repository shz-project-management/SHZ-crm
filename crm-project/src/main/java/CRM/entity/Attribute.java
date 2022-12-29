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

    public static <T extends Attribute> T createAttribute(String name, String description){
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setDescription(Objects.requireNonNullElse(description, ""));
        return (T) attribute;
    }

    public static Status createStatusAttribute(String name, String description) {
        Status status = new Status();
        status.setName(name);
        status.setDescription(description);
        return status;
    }


    public static Type createTypeAttribute(String name, String description) {
        Type type = new Type();
        type.setName(name);
        type.setDescription(description);
        return type;
    }
}
