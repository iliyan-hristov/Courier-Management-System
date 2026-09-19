package app.Web;

import app.courier.Model.CourierType;
import app.courier.Repository.CourierRepository;
import app.order.Model.Order;
import app.order.Service.OrderService;
import app.shipment.Model.ShipmentStatus;
import app.user.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import app.shipment.Model.Shipment;
import app.shipment.Service.ShipmentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/ship")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final OrderService orderService;
    private final CourierRepository courierRepository;

    public ShipmentController(ShipmentService shipmentService, OrderService orderService, CourierRepository courierRepository) {
        this.shipmentService = shipmentService;
        this.orderService = orderService;
        this.courierRepository = courierRepository;
    }

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentService.findAll();
    }

    @GetMapping("/ship")
    public String showShipPage() {
        return "ship";
    }

    @PostMapping("/ship")
    @Transactional
    public String calculateShipping(
            @RequestParam String fromCity,
            @RequestParam String toCity,
            @RequestParam String receiverName,
            @RequestParam String shipmentType,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        double price = 0.0;
        if (shipmentType.equalsIgnoreCase("Standard")) {
            price = 20.0;
        } else if (shipmentType.equalsIgnoreCase("Express")) {
            price = 35.0;
        } else if (shipmentType.equalsIgnoreCase("Fragile")) {
            price = 40.0;
        }

        if (!fromCity.equalsIgnoreCase(toCity)) {
            price += Math.random() * 10;
        }

        model.addAttribute("fromCity", fromCity);
        model.addAttribute("toCity", toCity);
        model.addAttribute("receiverName", receiverName);
        model.addAttribute("shipmentType", shipmentType);
        model.addAttribute("price", String.format("%.2f", price));

        return "ship";
    }

    @PostMapping("/price/confirm")
    public String confirmCourierOrder(
            @RequestParam String fromCity,
            @RequestParam String toCity,
            @RequestParam String receiverName,
            @RequestParam String courierType,
            @RequestParam String price,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {

            Double parsedPrice = Double.parseDouble(price.replace(",", "."));

            Order order = Order.builder()
                    .customer(user.getUsername())
                    .courier(getCourierName(courierType))
                    .status("Pending")
                    .total(parsedPrice)
                    .build();
            Order savedOrder = orderService.saveOrder(order);

            Shipment shipment = Shipment.builder()
                    .user(user)
                    .receiverName(receiverName)
                    .originAddress(fromCity)
                    .destinationAddress(toCity)
                    .status(ShipmentStatus.CREATED)
                    .createdAt(LocalDateTime.now())
                    .trackingNumber("TRK" + System.currentTimeMillis())
                    .courierType(CourierType.valueOf(courierType.toUpperCase()))
                    .order(savedOrder)
                    .build();
            shipmentService.save(shipment);

            return "redirect:/orders";
        } catch (Exception e) {
            return "redirect:/ship/ship?error=true";
        }
    }


    private String getCourierName(String courierType) {
        return switch (courierType) {
            case "EXPRESS" -> "FastShip";
            case "STANDARD" -> "SkyExpress";
            case "ECONOMY" -> "EcoCourier";
            default -> "courier.unknown";
        };
    }

    @PatchMapping("/shipments/{id}/status")
    public ResponseEntity<Shipment> changeStatus(@PathVariable UUID id, @RequestBody ShipmentStatus status) {

        Shipment updated = shipmentService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

}
