package spring.alotra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.alotra.entity.Product;
import spring.alotra.service.CategoryService;
import spring.alotra.service.ProductService;
import org.springframework.data.domain.Pageable;

@Controller
public class ItemDrinkController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Controller
    public static class ProductController {

        @Autowired
        private ProductService productService;

        @GetMapping("/itemDrink")
        public String getProducts(
                @RequestParam(value = "price", required = false, defaultValue = "1000000") double price,
                @RequestParam(value = "page", defaultValue = "0") int page,
                Model model) {

            Pageable pageable = (Pageable) PageRequest.of(page, 24);
            Page<Product> products = productService.findProductsByPriceLessThanEqual(price, pageable);

            Filter filter = new Filter(price);

            model.addAttribute("products", products);
            model.addAttribute("filter", filter);
            return "form/item-drink";
        }
    }



}
