package CRM.entity;

import CRM.entity.DTO.AttributeDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ToString
@Entity
@Table(name = "statuses")
public class Status extends Attribute {

    public static Set<Status> defaultStatuses(){
        Set<Status> defaultStatus = new HashSet<>();
        defaultStatus.add(Attribute.createStatusAttribute("Open", ""));
        defaultStatus.add(Attribute.createStatusAttribute("In Progress", ""));
        defaultStatus.add(Attribute.createStatusAttribute("Stuck", ""));
        defaultStatus.add(Attribute.createStatusAttribute("Done", ""));
        return defaultStatus;
    }
}
