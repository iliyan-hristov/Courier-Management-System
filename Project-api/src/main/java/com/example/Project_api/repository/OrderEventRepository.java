package com.example.Project_api.repository;

import com.example.Project_api.model.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface OrderEventRepository extends JpaRepository <OrderEvent, UUID> {

}
