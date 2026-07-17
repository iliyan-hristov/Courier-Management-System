package app.Web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/couriers")
public class CourierController {

    @PostMapping
    public String showCourierPage(@RequestParam String fromCity,
                                  @RequestParam String toCity,
                                  @RequestParam String receiverName,
                                  @RequestParam String shipmentType,
                                  Model model) {

        model.addAttribute("fromCity", fromCity);
        model.addAttribute("toCity", toCity);
        model.addAttribute("receiverName", receiverName);
        model.addAttribute("shipmentType", shipmentType);

        return "couriers";
    }

}
