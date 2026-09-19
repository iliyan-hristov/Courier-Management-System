package app.Web;

import app.shipment.Model.Shipment;
import app.shipment.Repository.ShipmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class TrackController {

    private final ShipmentRepository shipmentRepository;

    public TrackController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping("/track")
    public String showTrackPage() {
        return "track";
    }

    @PostMapping("/track")
    public String trackShipment(@RequestParam String trackingNumber, Model model) {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a tracking number.");
            return "track";
        }

        Optional<Shipment> shipment = shipmentRepository.findByTrackingNumber(trackingNumber.trim());

        if (shipment.isPresent()) {
            model.addAttribute("shipment", shipment.get());
        } else {
            model.addAttribute("error", "No shipment found with this tracking number.");
        }

        return "track";
    }

}
