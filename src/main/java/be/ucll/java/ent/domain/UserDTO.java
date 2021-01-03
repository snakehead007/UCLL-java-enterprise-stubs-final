package be.ucll.java.ent.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable {

    private final String userid;
    private final String password;
    private String firstName;
    private String lastName;
    private List<Role> roles;

    public UserDTO(@NotNull String userid, @NotNull String password) {
        this.userid = userid;
        this.password = password;
        firstName = "Ongeïdentificeerde";
        lastName = "gebruiker";
        roles = new ArrayList<>(1);
        roles.add(Role.USER);
    }

    public UserDTO(@NotNull String userid, @NotNull String password, String firstName, String lastName, List<Role> roles) {
        this.userid = userid;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;

        if (roles == null){
            roles = new ArrayList<>(1);
            roles.add(Role.USER);
        } else {
            this.roles = roles;
            if (!roles.contains(Role.USER)) this.roles.add(Role.USER);
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return roles != null && roles.contains(Role.ADMIN);
    }

    public boolean equals(Object o) {
        if (o instanceof UserDTO) {
            return ((UserDTO) o).getUserid().equals(getUserid());
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }
}