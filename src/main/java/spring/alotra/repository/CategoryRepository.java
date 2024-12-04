package spring.alotra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.alotra.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    List<Category> findByNameContaining(String name);

    List<Category> findByNameContainingIgnoreCase(String name);

    List<Category> id(Long id);
}
