package CRM.controller.facades;

import CRM.entity.DTO.BoardDTO;
import CRM.entity.DTO.SettingsDTO;
import CRM.entity.response.Response;
import CRM.service.SettingsService;
import CRM.utils.Common;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class SettingsFacade {

    private static Logger logger = LogManager.getLogger(SettingsFacade.class.getName());

    @Autowired
    private SettingsService settingsService;

    /**
     * This method is used to retrieve user's notification settings in a specified board.
     *
     * @param userId The id of the user whose settings are to be retrieved.
     * @param boardID The id of the board whose user's settings belong to.
     * @return A Response object containing all the retrieved settings or an error message if the user/board is not found or the id is invalid.
     * @throws IllegalArgumentException if the specified user/board id is invalid.
     * @throws NullPointerException     if the specified user/board id is null.
     * @throws NoSuchElementException   if the user with the specified id is not found/user does not belong to that board.
     */
    public Response getAllUserSettingsInBoard(Long userId, Long boardID){
        try {
            Validations.validate(userId, Regex.ID.getRegex());
            Validations.validate(boardID, Regex.ID.getRegex());
            List<SettingsDTO> settingsDTO = SettingsDTO.createUserSettingsList(settingsService.getAllUserSettingsInBoard(userId, boardID));
            return Common.buildSuccessResponse(settingsDTO, HttpStatus.OK, SuccessMessage.FOUND.toString());
        } catch (IllegalArgumentException | NoSuchElementException | AccountNotFoundException e) {
            return Common.buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return Common.buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Response changeUserSettingInBoard(Long userId, Long boardId, Long settingId, Boolean shouldBeActive){
        // validate the parameters using Validations.validate function
        // call settingsService and ask to change the setting for the user in the board
        return null;
    }
}
