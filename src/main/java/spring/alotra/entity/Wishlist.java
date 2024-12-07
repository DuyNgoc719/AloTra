package spring.alotra.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlistId")
    private int wishlistId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "wishlist_product",
            joinColumns = @JoinColumn(name = "wishlistId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private List<Product> products;

    @OneToOne
    @JoinColumn(name = "userId",  referencedColumnName = "userId")
    private User user;

}
