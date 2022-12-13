package CRM.entity;

import javax.persistence.*;

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

}
