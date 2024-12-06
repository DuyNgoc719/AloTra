package spring.alotra.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.alotra.entity.Cart;
import spring.alotra.entity.User;
import spring.alotra.service.CartService;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("")
    public String viewCart(Model model,HttpSession session) {

        User user = (User) session.getAttribute("user");

        List<Cart> cartItems = cartService.getCartItems(user.getId());
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrices())
                .sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "form/cart";
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long productId, HttpSession session, @RequestParam(defaultValue = "1") int quantity) {
        User user = (User) session.getAttribute("user");
        cartService.addToCart(productId,quantity, user.getId());
        return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng.");
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cartItemId") Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(@RequestParam("cartItemId") Long cartItemId,
                                 @RequestParam("quantity") int quantity)
    {
        System.out.println("quantity: " + quantity);
        cartService.updateQuantity(cartItemId, quantity);
        return "redirect:/cart";
    }


}
