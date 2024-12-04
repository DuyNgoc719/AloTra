package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.alotra.entity.Category;
import spring.alotra.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public void AddCategory(Category category) {
        if (!isHaveCategory(category)) {
            categoryRepository.save(category);
        }
    }

    private boolean isHaveCategory(Category category) {
        return categoryRepository.existsByName(category.getName());
    }

    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public List<Category> findCategory(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(int id) {
        return categoryRepository.findById(id);
    }


}
