package ru.university.jewelry.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.university.jewelry.model.Order;
import ru.university.jewelry.model.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByStatusInAndCreatedAtBetween(Collection<OrderStatus> statuses,
            LocalDateTime from, LocalDateTime to);
}
