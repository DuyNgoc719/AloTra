package spring.alotra.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="User")
@Data
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false,unique = true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable=false,unique=true)
    private String email;

    @Column(nullable=false,unique=true)
    private String phone;

    @Column(nullable=false)
    private String firstName;

    @Column(nullable=false)
    private String lastName;
}
