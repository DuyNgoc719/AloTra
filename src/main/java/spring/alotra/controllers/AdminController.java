package spring.alotra.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import spring.alotra.config.JwtUtil;
import spring.alotra.entity.Order;
import spring.alotra.entity.User;
import spring.alotra.service.OrderService;
import spring.alotra.service.UserServiceImpl;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderService orderService;


    public AdminController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    UserServiceImpl userService;


    @GetMapping("")
    public String showDashBoard() {
        return "admin/dashboard";
    }
    @GetMapping("information")
    public String showInformationAccount(Model model, HttpSession session) {
        String token = (String) session.getAttribute("access_token");
        Jwt jwt = jwtUtil.decode(token);
        String username = jwt.getSubject();
        User user = userService.findUserByUsername(username);
        session.setAttribute("current_user", user);
        model.addAttribute("user", user);
        return "admin/infor-account";
    }

    @PostMapping("update-account")
    public ModelAndView updateAccount(@ModelAttribute("user") User newUser,
                                      @RequestParam("profilePicture") MultipartFile file,
                                      Model model, HttpSession session) {
        ModelAndView mav = new ModelAndView("redirect:/admin/information");
        User user = (User) session.getAttribute("current_user");

        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPhone(newUser.getPhone());

        if (!file.isEmpty()) {
            try {
                String encodedImage = userService.encodeImage(file);
                user.setAvatar(encodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userService.saveUser(user);
        model.addAttribute("user", user);
        session.setAttribute("current_user", user);
        return mav;
    }


    @GetMapping("change-pass")
    public String showChangePassword(Model model, HttpSession session) {
        return "admin/change-password";
    }

    @PostMapping("change-pass")
    public String changePassword(@RequestParam("current-password") String password,
                                 @RequestParam("new-password") String new_password,
                                 @RequestParam("re-password") String re_password,
                                 Model model, HttpSession session)
    {
        User currentUser = (User) session.getAttribute("current_user");
        System.out.println("asd"+currentUser.getUsername());
        if (!passwordEncoder.matches(password, currentUser.getPassword())) {
            model.addAttribute("error","Sai mật khẩu");
            return "admin/change-password";
        }

        if (!new_password.equals(re_password)) {
            model.addAttribute("error","Mật khẩu nhập lại đã sai");
            return "admin/change-password";
        }

        String encodedPassword = passwordEncoder.encode(new_password);
        currentUser.setPassword(encodedPassword);
        System.out.println(currentUser.getUsername());
        userService.saveUser(currentUser);
        session.setAttribute("success","Đổi mật khẩu thành công");
        session.setAttribute("current_user", currentUser);
        return "redirect:/admin/information";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("orders")
    public String showOrders(Model model, HttpSession session) {


        List<Order> orders= orderService.findAll();

        model.addAttribute("orders", orders);

        return "admin/order-customer";
    }

    @PostMapping("orders/accept/{orderId}")
    public String accpetOrder(@PathVariable Long orderId, Model model, HttpSession session) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isPresent()) {
            Order order_change = order.get();
            order_change.setOrderStatus("Completed");
            orderService.save(order_change);
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, Model model, HttpSession session) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isPresent()) {
            Order order_change = order.get();
            order_change.setOrderStatus("Cancelled");
            orderService.save(order_change);
        }
        return "redirect:/admin/orders";
    }


}
