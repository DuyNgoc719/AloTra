package spring.alotra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.alotra.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(Long orderId);
    List<Order> findByCustomerId(Long customerId);
}
