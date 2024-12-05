package spring.alotra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.alotra.entity.Category;
import spring.alotra.entity.Product;
import spring.alotra.repository.CategoryRepository;
import spring.alotra.service.CategoryService;
import spring.alotra.service.ProductService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping("/product")
    public String showProduct(Model model) {
        List<Product> products = productService.getAll();
        model.addAttribute("products",products);
        model.addAttribute("categories",categoryService.getCategory());
        for (Product product : products) {
            System.out.println(product);
        }
        return "admin/product";
    }

    @PostMapping("add-product")
    public String addProduct(@ModelAttribute Product product ) {
        productService.addProduct(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/update-product")
    public String showEditProductForm(Model model) {
        List<Category> categories = categoryService.getCategory();

            model.addAttribute("categories", categories);

        return "admin/product";
    }

    @PostMapping("/update-product")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam Double prices,
                                @RequestParam Double discountPercentages,
                                @RequestParam String images,
                                @RequestParam Category category,
                                Model model) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(name);
            product.setDescription(description);
            product.setPrices(prices);
            product.setDiscountPercentages(discountPercentages);
            product.setImages(images);
            product.setCategory(category);

            productService.saveProduct(product);
            return "redirect:/admin/product";
        }
        return "redirect:/admin/product";
    }

    @PostMapping("delete-product")
    public String deleteProduct(@RequestParam Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/product";
    }
}
