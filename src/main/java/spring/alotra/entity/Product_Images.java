package spring.alotra.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name ="product_images")
public class Product_Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
