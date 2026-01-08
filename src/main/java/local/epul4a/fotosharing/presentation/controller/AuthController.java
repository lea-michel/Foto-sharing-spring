package local.epul4a.fotosharing.presentation.controller;

import jakarta.validation.Valid;
import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.presentation.dto.UserRegistrationDTO;
import local.epul4a.fotosharing.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserRegistrationDTO user = new UserRegistrationDTO();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserRegistrationDTO user, BindingResult bindingResult, Model model){
        User existing = userService.findByEmail(user.getEmail());
        if(existing != null){
            bindingResult.rejectValue("email", null, "Il y a déjà un compte enregistré avec cet email");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }
}
