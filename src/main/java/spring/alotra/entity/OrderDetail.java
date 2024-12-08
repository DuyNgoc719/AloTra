package spring.alotra.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "OrderDetails")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice;

    @Transient
    public Double getTotalPrice() {
        return this.unitPrice * this.quantity;
    }
}