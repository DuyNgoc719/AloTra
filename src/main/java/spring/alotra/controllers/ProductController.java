package spring.alotra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.alotra.entity.Category;
import spring.alotra.entity.Product;
import spring.alotra.repository.CategoryRepository;
import spring.alotra.service.CategoryService;
import spring.alotra.service.ProductService;
import spring.alotra.service.UserService;
import spring.alotra.service.UserServiceImpl;

import java.io.IOException;
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
    private UserServiceImpl userService;


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
                                @RequestParam Category category,
                                Model model) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(name);
            product.setDescription(description);
            product.setPrices(prices);
            product.setDiscountPercentages(discountPercentages);
            product.setCategory(category);

            productService.saveProduct(product);
            return "redirect:/admin/product";
        }
        return "redirect:/admin/product";
    }

    @PostMapping("delete-product")
    public String deleteProduct(@RequestParam int id) {
        System.out.println("al;fhuowf");
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()){
            Product product_delete = product.get();
            System.out.println(product_delete);
            productService.deleteProduct(product_delete);
        }
        return "redirect:/admin/product";
    }

    @GetMapping("information/{id}")
    public String showInformationProductForm(Model model,@PathVariable Long id) {
        Optional<Product> productOpt = productService.findById(id);
        List<Category> categories = categoryService.getCategory();
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            model.addAttribute("product", product);
            model.addAttribute("categories", categories);
        }
        return "admin/infor-product";
    }

    @PostMapping("updateInforProduct")
    public String updateInformationProduct(@ModelAttribute Product product, @RequestParam("productPicture") MultipartFile file,
                                            @RequestParam Category category) {
        if (!file.isEmpty()) {
            try {
                String encodedImage = userService.encodeImage(file);
                product.setImages(encodedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (product.getImages() == null) {
            Optional<Product> productOpt = productService.findById(product.getId());
            if (productOpt.isPresent()) {
                product = productOpt.get();
                product.setImages(product.getImages().toString());
            }
        }
        product.setCategory(category);
        productService.saveProduct(product);
        return "redirect:/admin/product";
    }

}
