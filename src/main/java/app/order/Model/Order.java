package app.order.Model;

import app.shipment.Model.Shipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String customer;

    private String courier;

    private String status;

    private Double total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shipment> shipments = new ArrayList<>();

    private Long deliveryTimeMinutes;

    public double getPrice() {
        return total;
    }

    public long getDeliveryTimeMinutes() {
        return deliveryTimeMinutes;
    }

    public String getReceiverName() {
        return shipments.isEmpty() ? "N/A" : shipments.get(0).getReceiverName();
    }

    public String getTrackingNumber() {
        return shipments.isEmpty() ? "N/A" : shipments.get(0).getTrackingNumber();
    }


}
