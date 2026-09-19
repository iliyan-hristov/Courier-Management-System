package app.shipment.Service;

import app.shipment.Model.ShipmentStatus;
import app.shipment.Repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.shipment.Model.Shipment;
import java.util.List;
import java.util.UUID;


@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }


    public Shipment updateStatus(UUID shipmentId, ShipmentStatus newStatus) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setStatus(newStatus);
        return shipmentRepository.save(shipment);
    }


    public Shipment save(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }


}
