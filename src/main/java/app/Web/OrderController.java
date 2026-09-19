package app.Web;

import app.order.Service.OrderService;
import app.shipment.Model.Shipment;
import app.shipment.Repository.ShipmentRepository;
import app.shipment.Service.ShipmentService;
import app.user.Model.User;
import app.order.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    private final ShipmentRepository shipmentRepository;
    @Autowired
    private ShipmentService shipmentService;

    public OrderController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping("/orders")
    public String viewUserOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
             return "redirect:/";
        }

        List<Shipment> userShipments = shipmentRepository.findByUser(user);
        model.addAttribute("shipments", userShipments);
        model.addAttribute("username", user.getUsername());

        return "orders";
    }

    @PostMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable UUID id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        shipmentRepository.deleteById(id);
        return "redirect:/orders";
    }

    @GetMapping("/orders/rate/{id}")
    public String showRatingPage(@PathVariable UUID id, Model model) {
        Shipment shipment = shipmentRepository.findById(id).orElse(null);
        if (shipment == null) {
            model.addAttribute("error", "Shipment not found.");
            return "orders";
        }
        model.addAttribute("shipment", shipment);
        return "rating";
    }

    @PostMapping("/orders/rate/{id}")
    public String saveRating(@PathVariable UUID id, @RequestParam int rating) {
        shipmentRepository.findById(id).ifPresent(s -> {
            s.setRating(rating);
            shipmentRepository.save(s);
        });
        return "redirect:/orders";
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @PostMapping("/complete/{id}")
    public String completeOrder(@PathVariable UUID id) {
        orderService.completeOrder(id);
        return "redirect:/orders";
    }

}
