package CRM.entity;

import lombok.*;

import javax.persistence.*;


@ToString
@Entity
@Table(name = "statuses")
public class Status extends Attribute {

    public static Status createStatus(Attribute attribute){
        Status status = new Status();
        status.setName(attribute.getName());
        status.setDescription(attribute.getDescription());
        status.setBoard(attribute.getBoard());
        return status;
    }
}
