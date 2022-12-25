package CRM.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ToString
@Entity
@Table(name = "types")
public class Type extends Attribute{

    public static Set<Type> defaultTypes(){
        Set<Type> defaultStatus = new HashSet<>();
        defaultStatus.add(Attribute.createTypeAttribute("Item", ""));
        defaultStatus.add(Attribute.createTypeAttribute("Sub-Item", ""));
        defaultStatus.add(Attribute.createTypeAttribute("Bug", ""));
        defaultStatus.add(Attribute.createTypeAttribute("Test", ""));
        return defaultStatus;
    }
}
