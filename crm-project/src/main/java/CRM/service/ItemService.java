package CRM.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class ItemService {
    private static Logger logger = LogManager.getLogger(ItemService.class.getName());

    @Autowired


    /**
     * getUser called from userController to send back to client an UserRes which is a response
     * with name mail and id.
     *
     * @param itemId - user id in database.
     * @return - entity of UserODT that contains name, email and id.
     */
    public Item find(Long itemId) throws AccountNotFoundException {
        // Ask for the repo to find the item, by the given id input
        // Since the repo returns an option, check if this option is not empty

        // If it is empty, throw "AccountNotFound"

        // Return the user back to the controller
        return null;
    }
}
