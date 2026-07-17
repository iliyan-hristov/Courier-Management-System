package app.Web;

import app.order.Service.OrderService;
import app.order.Model.Order;
import app.shipment.Model.Shipment;
import app.shipment.Model.ShipmentStatus;
import app.shipment.Service.ShipmentService;
import app.user.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping
    public String adminHome(HttpSession session, Model model) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                String username = user.getUsername();
                if (username != null) {

                    username = username.replaceAll("[<>\"'&]", "");
                }
                model.addAttribute("username", username);
            }
            return "admin";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/shipments-list")
    public String statistics(
            @RequestParam(value = "courier", required = false) String courier,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            String username = user.getUsername();
            if (username != null) {

                username = username.replaceAll("[<>\"'&]", "");
            }
            model.addAttribute("username", username);
        }

        List<Order> orders = orderService.getAllOrders();

        if (courier != null && !courier.isEmpty()) {
            orders = orders.stream()
                    .filter(o -> courier.equalsIgnoreCase(o.getCourier()))
                    .collect(Collectors.toList());
        }
        switch (sortBy.toLowerCase()) {
            case "customer":
                orders.sort((o1, o2) -> o1.getCustomer().compareToIgnoreCase(o2.getCustomer()));
                break;
            case "courier":
                orders.sort((o1, o2) -> o1.getCourier().compareToIgnoreCase(o2.getCourier()));
                break;
            case "total":
                orders.sort((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal())); // Descending
                break;
            default:

                break;
        }

        model.addAttribute("orders", orders);
        model.addAttribute("selectedCourier", courier);
        model.addAttribute("sortBy", sortBy);

        return "shipments-list";
    }

    @PostMapping("/orders/{id}/accept")
    public String acceptOrder(@PathVariable UUID id) {
        Order order = orderService.findById(id);
        if (order != null && "Pending".equals(order.getStatus())) {
            order.setStatus("Accepted");
            orderService.saveOrder(order);

            for (Shipment shipment : order.getShipments()) {
                shipment.setStatus(ShipmentStatus.IN_TRANSIT);
                shipmentService.save(shipment);
            }
        }
        return "redirect:/admin/shipments-list";
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/shipments-list";
    }



}
