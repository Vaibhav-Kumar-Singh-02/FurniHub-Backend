package com.furnihub.repository;

import com.furnihub.entity.Order;
import com.furnihub.entity.Order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_UserIdOrderByCreatedAtDesc(Integer userId);
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    long countByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user")
    List<Order> findAllWithUser();

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.status = :status")
    List<Order> findByStatusWithUser(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.user.userId = :userId")
    List<Order> findByUser_UserIdWithUser(@Param("userId") Integer userId);
}