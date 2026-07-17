package app.shipment.Model;

import app.courier.Model.Courier;
import app.courier.Model.CourierType;
import jakarta.persistence.*;
import app.order.Model.Order;
import lombok.*;
import app.user.Model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String trackingNumber;
    private String originAddress;
    private String destinationAddress;
    private double weight;
    private String senderName;
    private String receiverName;
    private String userEmail;
    private Integer rating;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDate estimatedDeliveryDate;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Enumerated(EnumType.STRING)
    private CourierType courierType;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
