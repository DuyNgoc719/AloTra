package spring.alotra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.alotra.entity.Category;
import spring.alotra.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/")
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("category")
    public String showCategory(Model model) {
        List<Category> categories = categoryService.getCategory();
        model.addAttribute("categories", categories);
        return "admin/category";
    }

    @PostMapping("update-category")
    public String updateCategory(@RequestParam int id,
                                 @RequestParam String name,
                                 @RequestParam String description,
                                 Model model) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isPresent()) {
            Category newCategory = category.get();
            newCategory.setName(name);
            newCategory.setDescription(description);
            categoryService.updateCategory(newCategory);
            return "redirect:/admin/category";
        }
        return "redirect:/admin/category";
    }

    @PostMapping("add-category")
    public String addCategory(@RequestParam String name,
                              @RequestParam String description,
                              Model model) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        categoryService.AddCategory(category);
        return "redirect:/admin/category";
    }

    @PostMapping("delete-category")
    public String deleteCategory(@RequestParam int id) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isPresent()) {
            Category newCategory = category.get();
            System.out.println("id"+newCategory.getId());
            categoryService.deleteCategory(newCategory);
            return "redirect:/admin/category";
        }
        return "redirect:/admin/category";
    }
}
