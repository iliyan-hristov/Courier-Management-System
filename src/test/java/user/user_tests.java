package user;
import app.user.Model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static app.user.Model.UserRole.CUSTOMER;


public class user_tests {


    @Test
    public void test_if_userName_exists_throwException () throws RuntimeException{

        User existedUser = new User(null, "Pepi", "pepi@gmail.com", "1234", CUSTOMER, new ArrayList<>());

        User newUser = new User();
        newUser.setUsername("Pepi");

        Assertions.assertEquals(existedUser.getUsername(), newUser.getUsername());
    }

    @Test
    public void test_if_user_notExist_register(){

        List<User> existedUsers = new ArrayList<>();

        User existedUser1 = new User(null, "Ivan", null, null, null, null);
        existedUsers.add(existedUser1);

        User existedUser2 = new User(null, "Pepi", null, null, null, null);
        existedUsers.add(existedUser2);

        User newUser = new User(null, "Alex", null, null, null, null);

        Assertions.assertNotEquals(existedUsers, newUser.getUsername());

    }

    @Test
    public void test_if_user_not_registered_loginError() throws RuntimeException{

        User registeredUser = new User(null, "Toni", "toni@gmail.com", "1234", null, null);

        User potentialUser = new User(null, "Sandra", "sandra@yahoo.com", null, null, null);

        Assertions.assertNotEquals(registeredUser, potentialUser.getUsername());



    }

    @Test
    public void test_if_username_and_password_match_loginSuccess(){

        User registeredUser = new User(null, "Toni", "toni@gmail.com", "1234", null, null);

        User loginUser = new User(null, "Toni", "toni@gmail.com", "1234", null, null);

        Assertions.assertEquals(registeredUser.getUsername(), loginUser.getUsername());

    }

}
