package app.order.Service;

import app.order.Repository.OrderRepository;
import app.order.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        sendOrderToExternalApi(savedOrder);
        return savedOrder;
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Value("${external.api.base-url}")
    private String apiBaseUrl;

    public void sendOrderStatistics(UUID orderId, double price, long deliveryTimeMinutes) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("price", price);
        payload.put("deliveryTimeMinutes", deliveryTimeMinutes);

        restTemplate.postForEntity(
                apiBaseUrl + "/api/statistics/events",
                payload,
                Void.class
        );
    }

    public void sendOrderToExternalApi(Order order) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("totalPrice", order.getTotal());
            payload.put("estimatedDeliveryTime", order.getDeliveryTimeMinutes());

            restTemplate.postForEntity(
                    apiBaseUrl + "/api/orders",
                    payload,
                    Void.class
            );
        } catch (Exception e) {
            System.err.println("Failed to send order to external API: " + e.getMessage());
        }
    }

    public void completeOrder(UUID id) {
        Order order = findById(id);
        if (order != null) {
            sendOrderStatistics(order.getId(),
                    order.getPrice(),
                    order.getDeliveryTimeMinutes());
        }

    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }
}
