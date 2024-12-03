package spring.alotra.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.alotra.config.JwtUtil;
import spring.alotra.entity.User;
import spring.alotra.service.UserService;
import spring.alotra.service.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final JwtUtil jwtUtil;

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
        System.out.println("Token"+token);
        Jwt jwt = jwtUtil.decode(token);
        String username = jwt.getSubject();
        System.out.println("username"+username);
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "admin/infor-account";
    }



}
