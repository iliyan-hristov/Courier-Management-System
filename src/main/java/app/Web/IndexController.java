package app.Web;

import app.Web.Dto.LoginRequest;
import app.Web.Dto.RegisterRequest;
import app.user.Model.User;
import app.user.Model.UserRole;
import app.user.Service.UserService;
import app.utils.LoginUtility;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getLoginPage() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        return new ModelAndView("register", "registerRequest", new RegisterRequest());
    }

    @PostMapping("/register")
    public String register(@Valid RegisterRequest registerRequest, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = userService.register(registerRequest);
        session.setAttribute("user", user);
        redirectAttributes.addFlashAttribute("successfulRegistration");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(name = "loginAttemptMessage", required = false) String message,
                                     @RequestParam(name = "error", required = false) String errorMessage,
                                     HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        modelAndView.addObject("loginAttemptMessage", message);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            modelAndView.addObject("username", user.getUsername());
        }
        LoginUtility.handleLoginMessages(modelAndView, errorMessage, session);
        return modelAndView;
    }

    @PostMapping("/login")
    public String processLogin(LoginRequest loginRequest, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = userService.login(loginRequest);
        if (user == null) {
            model.addAttribute("error", "Invalid login");
            return "login";
        }

        session.setAttribute("user", user);

        if (user.getRole() == UserRole.ADMIN) {
            return "redirect:/admin";
        } else if (user.getRole() == UserRole.CUSTOMER) {
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}


