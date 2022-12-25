package CRM.controller.facades;

import CRM.entity.DTO.BoardDTO;
import CRM.entity.DTO.SettingsDTO;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.SettingsService;
import CRM.utils.Common;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
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
     * @param objectsIdsRequest contains the ids of the user and board whose settings we want to retrieve and from.
     * @return A Response object containing all the retrieved settings or an error message if the user/board is not found or the id is invalid.
     * @throws IllegalArgumentException if the specified user/board id is invalid.
     * @throws NullPointerException     if the specified user/board id is null.
     * @throws NoSuchElementException   if the user with the specified id is not found/user does not belong to that board.
     */
    public Response getAllUserSettingsInBoard(ObjectsIdsRequest objectsIdsRequest){
        try {
            Validations.validateIDs(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
            return new Response.Builder()
                    .data(SettingsDTO.createUserSettingsList(settingsService.getAllUserSettingsInBoard(objectsIdsRequest)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException | AccountNotFoundException e) {
            return new Response.Builder()
                    .statusCode(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(500)
                    .build();
        }
    }

    public Response changeUserSettingInBoard(Long userId, Long boardId, Long settingId, Boolean shouldBeActive){
        // validate the parameters using Validations.validate function
        // call settingsService and ask to change the setting for the user in the board
        return null;
    }
}
