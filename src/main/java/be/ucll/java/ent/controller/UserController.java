package be.ucll.java.ent.controller;

import be.ucll.java.ent.domain.Role;
import be.ucll.java.ent.domain.UserDTO;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unlike other classes, this class must have an instance PER LOGGED IN USER or browser session
 * It therefore specifies a scope.
 *   Either you want to see a login page per browser session AND per tab within the browser: @UIScope
 *   Either you just want to see the login page per browser session: @VaadinSessionScope
 */
@VaadinSessionScope // STATEFULL CONTROLLER!
@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    // Statefull data. Verschillend per browser sessie
    private UserDTO loggedInUser;

    private static List<UserDTO> users;

    static {
        users = new ArrayList<>(2);
        users.add(new UserDTO("admin", "StubsAdminUCLL#123", "System", "Administrator", Arrays.asList(Role.USER, Role.ADMIN)));
        users.add(new UserDTO("user", "StubsUserUCLL#123", "Some", "User", null));
    }

    public UserDTO authenticateUser(UserDTO unauthenticateduser) {
        for (UserDTO user : users) {
            if (user.getUserid().equalsIgnoreCase(unauthenticateduser.getUserid()) &&
                    user.getPassword().equals(unauthenticateduser.getPassword())) {
                logger.info("User succesfully authenticated as '" + (user != null && user.getFullName() != null ? user.getFullName() : "<unknown>") + "'");
                return user;
            }
        }
        return null;
    }

    public boolean isUserSignedIn() {
        return loggedInUser != null;
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