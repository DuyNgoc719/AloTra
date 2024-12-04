package spring.alotra.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import spring.alotra.entity.User;
import spring.alotra.service.UserServiceImpl;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserServiceImpl service;

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

}
