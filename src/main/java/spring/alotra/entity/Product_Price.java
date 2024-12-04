package spring.alotra.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "product_price")
public class Product_Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime effectiveDate;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
