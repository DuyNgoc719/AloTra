package spring.alotra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import spring.alotra.entity.UsersEntity;
import spring.alotra.service.ServiceImpl;


@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private ServiceImpl service;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new UsersEntity());
        return "register";
    }

    @PostMapping("/register" )
    public ModelAndView processRegisterForm(@ModelAttribute("user") UsersEntity user){
        ModelAndView mav = new ModelAndView();
        String response = service.registerUser(user);
        mav.setViewName("redirect:/auth/login");
        return mav;
    }

    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView processLoginForm(@RequestParam String username, @RequestParam String password){
        ModelAndView mav = new ModelAndView();
        var resp = service.login(username, password);
        if (resp.containsKey("access_token")){
            mav.setViewName("redirect:/homepage");
        } else {
            mav.setViewName("redirect:/login");
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


}
