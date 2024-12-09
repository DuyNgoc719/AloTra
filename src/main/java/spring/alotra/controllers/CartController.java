package spring.alotra.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.alotra.entity.*;
import spring.alotra.service.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ProductService productService;

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

    @GetMapping("/view-customerOrder")
    public String viewCustomerOrder(Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");

        List<Order> orders = orderService.findAll();

        List<Order> orderCustomer = new ArrayList<>();

        for (Order order : orders) {
            if (order.getCustomerId().equals(user.getId())) {
                orderCustomer.add(order);
            }
        }


        model.addAttribute("orders", orderCustomer);
        model.addAttribute("user", user);
        System.out.println("orderCustomer: " + orderCustomer);
        return "form/customerOrder";
    }

    @GetMapping("/orders/{orderId}")
    public String viewOrderDetails(Model model, @PathVariable Long orderId) {
        List<Order> orders = orderService.findAll();
        List<Order> orderCustomer = new ArrayList<>();

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                orderCustomer.add(order);
            }
        }
        model.addAttribute("orders", orderCustomer);

        List<Product> find_products = productService.getAll();

        List<Product> products = new ArrayList<>();

        for (Product product : find_products) {
            for (Order order : orders) {
                List<OrderDetail> orderDetails = order.getOrderDetails();
                for (OrderDetail orderDetail : orderDetails) {
                    if (orderDetail.getProductId().equals(product.getId())) {
                        products.add(product);
                    }
                }
            }

        }
            model.addAttribute("products",products);
        return "form/detailOrderCustomer";
    }

    @PostMapping("/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId) {
        List<Order> orders = orderService.findAll();
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                orderService.delete(order);
            }
        }
        return "redirect:/cart";
    }


}
