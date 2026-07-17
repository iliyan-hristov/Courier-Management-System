package app.utils;

import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.ModelAndView;

@UtilityClass
public class LoginUtility {

    public static void handleLoginMessages(ModelAndView modelAndView, String errorMessage, HttpSession session) {
        if (modelAndView == null || session == null) {
            return;
        }
        
        try {
            String inactiveUserMessage = (String) session.getAttribute("inactiveUserMessage");

            if (inactiveUserMessage != null) {
                modelAndView.addObject("inactiveAccountMessage", inactiveUserMessage);
                session.removeAttribute("inactiveUserMessage");
            } else if (errorMessage != null) {
                modelAndView.addObject("errorMessage", "Invalid username or password");
            }
        } catch (Exception e) {

        }
    }

}
