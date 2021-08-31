package com.seanlindev.springframework.repositories;

import com.seanlindev.springframework.model.entities.OrderEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    @Query(value = "select * from orders o where o.owner_id = :ownerId", nativeQuery = true)
    List<OrderEntity> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Modifying
    @Query(value = "insert into order_participant (order_id, user_id) values (:orderId, :userId)", nativeQuery = true)
    int addNewParticipantForOrder(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "update orders o set quantity = :quantity where o.id = :orderId", nativeQuery = true)
    void updateOrderQuantity(@Param("orderId") Long orderId, @Param("quantity") Integer quantity);

    OrderEntity findByOrderId(String orderId);

    @Query(value = "select * from orders o where o.order_id = :orderId for update", nativeQuery = true)
    OrderEntity findByOrderIdForUpdate(String orderId);

    @Query(value = "select * from orders o where o.order_id = :orderId for share", nativeQuery = true)
    OrderEntity findByOrderIdForShare(String orderId);
}
