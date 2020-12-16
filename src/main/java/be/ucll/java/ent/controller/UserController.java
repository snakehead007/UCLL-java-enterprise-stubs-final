package be.ucll.java.ent.controller;

import be.ucll.java.ent.domain.UserDTO;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Unlike other classes, this class must have an instance PER LOGGED IN USER or browser session
 * It therefore specifies a scope.
 *   Either you wannt to see a login page per browser session AND per tab within the browser: @UIScope
 *   Either you just want to see the login page per browser session: @VaadinSessionScope
 */
@VaadinSessionScope
@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserDTO loggedInUser;

    private static List<UserDTO> users;

    static {
        users = new ArrayList<UserDTO>(2);
        users.add(new UserDTO("admin", "admin", "System", "Administrator", true));
        users.add(new UserDTO("user", "user", "Some", "User", false));
    }

    public UserDTO authenticateUser(UserDTO unauthenticateduser) {
        for (UserDTO user : users) {
            if (user.getUserid().equalsIgnoreCase(unauthenticateduser.getUserid()) &&
                    user.getPassword().equals(unauthenticateduser.getPassword())) {
                logger.info("User succesfully authenticated as '" + user.getFullName() + "'");
                return user;
            }
        }
        return null;
    }

    public boolean isUserSignedIn() {
        if (loggedInUser != null) return true;
        return false;
    }

    public UserDTO getUser() {
        return loggedInUser;
    }

    public void setUser(UserDTO user){
        loggedInUser = user;
    }

    public void reset() {
        loggedInUser = null;
    }
}