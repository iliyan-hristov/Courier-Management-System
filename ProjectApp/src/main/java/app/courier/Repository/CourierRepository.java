package app.courier.Repository;

import app.courier.Model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CourierRepository extends JpaRepository<Courier, UUID> {
    

    Courier findByName(String name);
}
