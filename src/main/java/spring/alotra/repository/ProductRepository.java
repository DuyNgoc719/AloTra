package spring.alotra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.alotra.entity.Product;

<<<<<<< HEAD
import org.springframework.data.domain.Pageable;
import java.util.List;
=======
import java.util.Optional;
>>>>>>> 2ae07abf677d712ef8580f875064008b3c87ee31

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByPricesLessThanEqual(Double price, Pageable pageable);
}


