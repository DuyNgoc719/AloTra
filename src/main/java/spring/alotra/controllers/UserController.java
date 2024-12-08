package spring.alotra.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import spring.alotra.config.JwtUtil;
import spring.alotra.entity.Product;
import spring.alotra.entity.User;
import spring.alotra.repository.PasswordResetRepository;
import spring.alotra.service.EmailService;
import spring.alotra.service.ProductService;
import spring.alotra.service.UserServiceImpl;

import java.io.IOException;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserServiceImpl service;
    private final JwtUtil jwtUtil;
    public UserController(JwtUtil jwtUtil) {this.jwtUtil = jwtUtil;}
    @Autowired
    UserServiceImpl userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PasswordResetRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "form/register";
    }

    @PostMapping("/register" )
    public ModelAndView processRegisterForm(@ModelAttribute("user") User user){
        ModelAndView mav = new ModelAndView();
        service.registerUser(user);
        mav.setViewName("redirect:/login");
        return mav;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        return "form/login";
    }

    @PostMapping("/login")
    public ModelAndView processLoginForm(@RequestParam String username, @RequestParam String password, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        var resp = service.login(username, password);
        if (resp.containsKey("access_token")) {
            String role = (String) resp.get("role");
            String token = (String) resp.get("access_token");
            Jwt jwt = jwtUtil.decode(token);
            String usernameString = jwt.getSubject();
            User user = userService.findUserByUsername(username);
            session.setAttribute("user", user);
            session.setAttribute("access_token", token);
            System.out.println(role);
            if (role.equals("ADMIN")) {
                mav.setViewName("redirect:/admin/");
            } else {
                mav.setViewName("redirect:/home");
            }
        } else {
            mav.setViewName("redirect:/login");
        }
        return mav;
    }
    @GetMapping("home")
    public String showHomePage(Model model){
        return "form/home";
    }
    @GetMapping("menu")
    public String showMenuPage(Model model){
        return "form/menu";
    }

<<<<<<< HEAD
=======
    @GetMapping("itemDrink")
    public String showItemDrinkPage(Model model){
        return "item-drink";
    }

    @GetMapping("asd")
    public String showAsdPage(Model model){
        return "asd";
    }
>>>>>>> 2ae07abf677d712ef8580f875064008b3c87ee31
    @GetMapping("account")
    public String showAccountPage(Model model, HttpSession session)
    {
        String token = (String) session.getAttribute("access_token");
        Jwt jwt = jwtUtil.decode(token);
        String username = jwt.getSubject();
        User user = userService.findUserByUsername(username);
        session.setAttribute("current_user", user);
        model.addAttribute("user", user);
        return "form/account";
    }
    @PostMapping("account")
    public String updateAccount(@ModelAttribute("user") User newUser,
                                @RequestParam("profilePicture") MultipartFile file,
                                HttpSession session, Model model) {
        String token = (String) session.getAttribute("access_token");
        Jwt jwt = jwtUtil.decode(token);
        String username = jwt.getSubject();

        User user = userService.findUserByUsername(username);

        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPhone(newUser.getPhone());
        user.setAddress(newUser.getAddress());

        if (!file.isEmpty()) {
            try {
                String encodedImage = userService.encodeImage(file);  // Mã hóa ảnh
                user.setAvatar(encodedImage);  // Cập nhật avatar
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userService.saveUser(user);
        session.setAttribute("current_user", user);
        model.addAttribute("user", user);
        return "redirect:/account";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/login");
    }
    @GetMapping("/forgot-password")
    public String resetPassword(Model model) {
        return "form/forgotPass1";
    }

    @PostMapping("/check-user")
    public ModelAndView processResetPasswordForm(@RequestParam String email, HttpSession session) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return new ModelAndView("redirect:/forgot-password");
        }
        emailService.sendResetToken(email);
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestParam String newPassword) {
        boolean result = emailService.resetPassword(token, newPassword);
        if (result) {
            return ResponseEntity.ok("Đặt lại mật khẩu thành công.");
        } else {
            return ResponseEntity.badRequest().body("Token không hợp lệ hoặc đã hết hạn.");
        }
    }
    @GetMapping("aboutus")
    public String showAboutUsPage(Model model) {return "aboutus";}
    @GetMapping("us1")
    public String showUs1Page(Model model) {return "us1";}
    @GetMapping("us2")
    public String showUs2Page(Model model) {return "us2";}
    @GetMapping("us3")
    public String showUs3Page(Model model) {return "us3";}
    @GetMapping("blog")
    public String showBlogPage(Model model) {return "blog-list";}
    @GetMapping("drink-now")
    public String showDrinkNowPage(Model model) {return "drinkNow";}
}
