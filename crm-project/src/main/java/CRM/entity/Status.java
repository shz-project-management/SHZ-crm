package CRM.entity;

import lombok.*;

import javax.persistence.*;


@ToString
@Entity
@Table(name = "statuses")
public class Status extends Attribute {

    // FIXME: this method does not have to be written 3 times
    //  if not in use, remove
    //  if in use, put in Attribute class and just cast it.
    public static Status createStatus(Attribute attribute){
        Status status = new Status();
        status.setName(attribute.getName());
        status.setDescription(attribute.getDescription());
        return status;
    }
}
