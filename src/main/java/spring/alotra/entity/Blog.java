package spring.alotra.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blogId")
    private int blogId;

    @Column(name = "postingDate", columnDefinition = "DATE NOT NULL")
    private LocalDate postingDate;

    @Column(name = "blogTitle", columnDefinition = "NVARCHAR(200) NOT NULL")
    private String blogTitle;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "approval", columnDefinition = "BIT")
    private boolean approval;
}
