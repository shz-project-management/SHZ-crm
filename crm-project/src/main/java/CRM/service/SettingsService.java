package CRM.service;

import CRM.entity.UserSetting;
import CRM.repository.BoardRepository;
import CRM.repository.UserRepository;
import CRM.repository.UserSettingRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SettingsService {

    private static Logger logger = LogManager.getLogger(SettingsService.class.getName());
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserSettingRepository userSettingRepository;

    /**
     * This method is used to retrieve user's notification settings in a specified board.
     *
     * @param userId The id of the user whose settings are to be retrieved.
     * @param boardId The id of the board whose user's settings belong to.
     * @return A Response object containing all the retrieved settings or an error message if the user does not belong to that board.
     * @throws NoSuchElementException   if the user does not belong to that board.
     */
    public List<UserSetting> getAllUserSettingsInBoard(Long userId, Long boardId) throws AccountNotFoundException {
        if(!Validations.checkIfUserExistsInBoard(userId, boardId, userRepository, boardRepository))
            throw new NoSuchElementException(ExceptionMessage.USER_DOES_NOT_EXISTS_IN_BOARD.toString());
        return userSettingRepository.getUserSettingsInBoard(userId, boardId);
    }

    public Boolean changeUserSettingInBoard(Long userId, Long boardId, Long settingId, Boolean shouldBeActive){
        // make sure there is such a user in board in the db -> checkIfExists
        // if so, change the setting to the new setting. else, throw NoSuchElement exception
        return null;
    }
}
