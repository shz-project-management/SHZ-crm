package CRM.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "comments")
public class Comment extends SharedContent {
}
