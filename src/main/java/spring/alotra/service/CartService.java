package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.alotra.entity.Cart;
import spring.alotra.entity.Product;
import spring.alotra.entity.User;
import spring.alotra.repository.CartRepository;
import spring.alotra.repository.UserRepository;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductService productService;

    public void addToCart(Long productId, int quantity, Long userId) {
        Product product = productService.findById(productId).orElseThrow();
        User user = userService.findUserById(userId);

        Cart cartItem = cartRepository.findByUser(user).stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst().orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartRepository.save(cartItem);
        } else {
            Cart newCartItem = new Cart();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setUser(user);
            cartRepository.save(newCartItem);
        }
    }


    public List<Cart> getCartItems(Long userId) {
        User user = userService.findUserById(userId);
        return cartRepository.findByUser(user);
    }

    public void removeFromCart(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public void updateQuantity(Long productId, int quantity) {
        Cart cart = cartRepository.findById(productId).orElseThrow();
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }
}
