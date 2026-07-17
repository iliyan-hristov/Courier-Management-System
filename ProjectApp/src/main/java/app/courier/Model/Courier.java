package app.courier.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private double basePrice;
    
    @Column(nullable = false)
    private String estimatedTime;

    private double rating;

    @Enumerated(EnumType.STRING)
    private CourierType courierType;

}
