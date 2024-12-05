package spring.alotra.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import spring.alotra.config.JwtUtil;
import spring.alotra.entity.User;
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

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "register";
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
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView processLoginForm(@RequestParam String username, @RequestParam String password, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        var resp = service.login(username, password);
        if (resp.containsKey("access_token")) {
            String role = (String) resp.get("role");
            String token = (String) resp.get("access_token");
            session.setAttribute("access_token", token);
            System.out.println(role);
            if (role.equals("ADMIN")) {
                mav.setViewName("redirect:/admin/");
            } else {
                mav.setViewName("redirect:/home");
            }
        } else {
            mav.setViewName("login");
        }
        return mav;
    }
    @GetMapping("home")
    public String showHomePage(Model model){
        return "home";
    }
    @GetMapping("menu")
    public String showMenuPage(Model model){
        return "menu";
    }

    @GetMapping("itemDrink")
    public String showItemDrinkPage(Model model){
        return "item-drink";
    }
    @GetMapping("asd")
    public String showAsdPage(Model model){
        return "asd";
    }
    @GetMapping("account")
    public String showAccountPage(Model model, HttpSession session)
    {
        String token = (String) session.getAttribute("access_token");
        Jwt jwt = jwtUtil.decode(token);
        String username = jwt.getSubject();
        User user = userService.findUserByUsername(username);
        session.setAttribute("current_user", user);
        model.addAttribute("user", user);
        return "account";
    }
    @PostMapping("account")
    public String updateAccount(@ModelAttribute("user") User newUser,
                                @RequestParam("profilePicture") MultipartFile file,
                                HttpSession session, Model model) {
        // Lấy thông tin người dùng từ session
        String token = (String) session.getAttribute("access_token");
        Jwt jwt = jwtUtil.decode(token);
        String username = jwt.getSubject();

        // Tìm người dùng trong cơ sở dữ liệu theo username
        User user = userService.findUserByUsername(username);

        // Cập nhật các thông tin người dùng từ form
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPhone(newUser.getPhone());
        user.setAddress(newUser.getAddress());

        // Xử lý ảnh đại diện nếu người dùng tải lên ảnh mới
        if (!file.isEmpty()) {
            try {
                String encodedImage = userService.encodeImage(file);  // Mã hóa ảnh
                user.setAvatar(encodedImage);  // Cập nhật avatar
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Lưu thông tin người dùng vào cơ sở dữ liệu
        userService.saveUser(user);
        // Cập nhật thông tin người dùng trong session
        session.setAttribute("current_user", user);
        // Thêm thông tin người dùng vào model để có thể hiển thị trong view
        model.addAttribute("user", user);
        // Chuyển hướng người dùng về trang tài khoản sau khi cập nhật thành công
        return "redirect:/account";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/login");
    }
    @GetMapping("/reset-password")
    public String resetPassword(Model model) {return "forgotPass1";}
    @GetMapping("/verify-code")
    public String verifyCode(Model model) {return "forgotPass2";}
    @GetMapping("/set-password")
    public String setPassword(Model model) {return "forgotPass3";}
}
