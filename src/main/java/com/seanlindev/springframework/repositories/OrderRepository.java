package com.seanlindev.springframework.repositories;

import com.seanlindev.springframework.model.entities.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    @Query(value = "select * from orders o where o.owner_id = :ownerId", nativeQuery = true)
    List<OrderEntity> findAllByOwnerId(@Param("ownerId") Long ownerId);

    OrderEntity findByOrderId(String orderId);
}
