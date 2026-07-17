package app.Web;

import app.courier.Model.Courier;
import app.courier.Model.CourierType;
import app.courier.Repository.CourierRepository;
import app.shipment.Model.Shipment;
import app.shipment.Model.ShipmentStatus;
import app.shipment.Repository.ShipmentRepository;
import app.user.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PriceController {

    private final ShipmentRepository shipmentRepository;
    private final CourierRepository courierRepository;

    public PriceController(ShipmentRepository shipmentRepository, CourierRepository courierRepository) {
        this.shipmentRepository = shipmentRepository;
        this.courierRepository = courierRepository;
    }

    @GetMapping("/price")
    public String showPricePage() {
        return "price";
    }

    @PostMapping("/price")
    public String calculatePrice(@RequestParam String fromCity,
                                 @RequestParam String toCity,
                                 @RequestParam String receiverName,
                                 @RequestParam String shipmentType,
                                 Model model) {

        double baseDistance = estimateDistance(fromCity, toCity);
        double baseRate = 1.5;

        double multiplier = switch (shipmentType.toLowerCase()) {
            case "standard" -> 1.0;
            case "express" -> 1.5;
            case "fragile" -> 1.8;
            default -> 1.0;
        };

        double totalPrice = baseDistance * baseRate * multiplier;
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;

        model.addAttribute("fromCity", sanitizeInput(fromCity));
        model.addAttribute("toCity", sanitizeInput(toCity));
        model.addAttribute("receiverName", sanitizeInput(receiverName));
        model.addAttribute("shipmentType", sanitizeInput(shipmentType));
        model.addAttribute("distance", String.format("%.0f", baseDistance));
        model.addAttribute("price", String.format("%.2f", totalPrice));

        return "price";
    }

    @PostMapping("/price/couriers")
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

    @PostMapping("/price/confirm")
    public String confirmShipment(@RequestParam String fromCity,
                                  @RequestParam String toCity,
                                  @RequestParam String shipmentType,
                                  @RequestParam String price,
                                  @RequestParam String receiverName,
                                  @RequestParam String courierType,
                                  HttpSession session,
                                  Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "You must be logged in to view your orders.");
            return "login";
        }

        try {
            Shipment shipment = new Shipment();
            shipment.setSenderName(fromCity);
            shipment.setReceiverName(receiverName);
            shipment.setDestinationAddress(toCity);
            shipment.setTrackingNumber("TRK" + System.currentTimeMillis());
            shipment.setStatus(ShipmentStatus.CREATED);
            shipment.setCourierType(CourierType.valueOf(courierType));
            shipment.setUser(user);

            String loggedUser = (String) session.getAttribute("loggedUser");
            shipment.setUserEmail(loggedUser != null ? loggedUser : "guest");
            shipmentRepository.save(shipment);

            Courier courier = new Courier();
            courier.setName(courierType);
            courier.setBasePrice(Double.parseDouble(price));
            courier.setEstimatedTime("2-3 days");
            courier.setCourierType(CourierType.valueOf(courierType));

            courierRepository.save(courier);
            shipment.setCourier(courier);

            model.addAttribute("success","Tracking Number:  " + shipment.getTrackingNumber());
            model.addAttribute("fromCity", fromCity);
            model.addAttribute("toCity", toCity);
            model.addAttribute("shipmentType", shipmentType);
            model.addAttribute("price", price);
            model.addAttribute("receiverName", receiverName);

            return "confirm";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create shipment. Please try again.");
            return "price";
        }
    }

    private double estimateDistance(String from, String to) {
        if (from.equalsIgnoreCase(to)) return 5;
        int seed = Math.abs(from.hashCode() - to.hashCode());
        return 50 + (seed % 400);
    }

    private String sanitizeInput(String input) {
        return input != null ? input.replaceAll("[<>\"'&]", "") : "";
    }

}
