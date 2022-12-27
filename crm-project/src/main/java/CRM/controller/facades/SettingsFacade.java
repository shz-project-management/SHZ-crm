package CRM.controller.facades;

import CRM.entity.DTO.SettingsDTO;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.SettingUpdateRequest;
import CRM.entity.response.Response;
import CRM.service.SettingsService;
import CRM.utils.Validations;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
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
    public Response getAllUserSettingsInBoard(ObjectsIdsRequest objectsIdsRequest) {
        try {
            Validations.validateIDs(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());

            return Response.builder()
                    .data(SettingsDTO.createUserSettingsList(settingsService.getAllUserSettingsInBoard(objectsIdsRequest)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException | AccountNotFoundException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Changes the user settings in a board.
     *
     * @param settingUpdateRequest request containing the user and board ids and the new settings
     * @return a response object with the updated settings for the user in the board
     * @throws IllegalArgumentException if the user or board id is invalid
     * @throws NoSuchElementException   if the user or board does not exist
     * @throws AccountNotFoundException if the user does not have access to the board
     * @throws NullPointerException     if an error occurs while processing the request
     */
    public Response changeUserSettingsInBoard(SettingUpdateRequest settingUpdateRequest) {
        try {
            Validations.validateIDs(settingUpdateRequest.getUserId(), settingUpdateRequest.getBoardId());
            return Response.builder()
                    .data(SettingsDTO.createUserSettingsList(settingsService.changeUserSettingsInBoard(settingUpdateRequest)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException | AccountNotFoundException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }
}
