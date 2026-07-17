package app.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import app.user.Service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

}
