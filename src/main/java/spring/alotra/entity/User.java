package spring.alotra.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="firstName" , nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "cardId")
    private Integer cardId;

    @Column(nullable = false, length = 500)
    private String email;

    @Column(nullable = false, length = 10)
    private String phone;

    @Column(name = "isEmailActive")
    private Boolean isEmailActive;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", nullable = false, length = 10)
    private UserRole userRole;

    @OneToOne
    @JoinColumn(name = "addressId", referencedColumnName = "id")
    private UserAddresses address;

    @Column(length = 500)
    private String avatar;

    @Column(length = 255)
    private String cover;

    @Column(name = "userPoint")
    private Integer userPoint;

    @Column(name = "eWallet", precision = 10, scale = 2)
    private BigDecimal eWallet;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}