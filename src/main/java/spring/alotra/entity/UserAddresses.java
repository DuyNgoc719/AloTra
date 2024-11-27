package spring.alotra.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "useraddresses")
@Data
@Entity
public class UserAddresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String address;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserAddresses user;

}
