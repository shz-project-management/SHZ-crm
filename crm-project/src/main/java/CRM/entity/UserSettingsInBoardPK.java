package CRM.entity;

import lombok.Data;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@Setter
public class UserSettingsInBoardPK implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long boardId;
    private Long settingId;
}
