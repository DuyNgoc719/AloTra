package spring.alotra.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="product_reviews")
public class Product_Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String reviewerName;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
