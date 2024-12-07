package spring.alotra.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String images;

    @Column(name = "prices")
    private Double prices;

    @Column(name = "reviews")
    private String reviews;

    @Column(name = "discount_percentages")
    private Double discountPercentages;

    @Column(name = "discount_start_dates")
    private Date discountStartDates;

    @Column(name = "discount_end_dates")
    private Date discountEndDates;

}
