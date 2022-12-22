package CRM.entity;

import lombok.*;

import javax.persistence.*;


@ToString
@Entity
@Table(name = "types")
public class Type extends Attribute{

    public static Type createType(Attribute attribute){
        Type type = new Type();
        type.setName(attribute.getName());
        type.setDescription(attribute.getDescription());
        return type;
    }
}
