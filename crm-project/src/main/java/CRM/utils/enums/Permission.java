package CRM.utils.enums;

import javax.persistence.*;


public enum Permission {
    USER("USER"),
    LEADER("LEADER"),
    ADMIN("ADMIN");

    @Id
    private String name;

    Permission(String user) {
    }

    public String getName() {
        return name;
    }
}
