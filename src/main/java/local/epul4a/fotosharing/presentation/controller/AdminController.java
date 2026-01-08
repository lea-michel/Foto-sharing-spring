package local.epul4a.fotosharing.presentation.controller;

import local.epul4a.fotosharing.presentation.dto.UserSummaryDTO;
import local.epul4a.fotosharing.service.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")  // Seulement les ADMIN
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // ========== PAGE PRINCIPALE ADMIN ==========
    @GetMapping
    public String adminDashboard(Model model) {
        List<UserSummaryDTO> users = userService.getAllUsersSummary();
        model.addAttribute("users", users);
        return "admin/dashboard";
    }

    // ========== GESTION DES UTILISATEURS ==========
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserSummaryDTO> users = userService.getAllUsersSummary();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // ========== ACTIVER/DÉSACTIVER UN UTILISATEUR ==========
    @PostMapping("/users/{userId}/toggle")
    public String toggleUserStatus(
            @PathVariable Long userId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.toggleUserStatus(userId);

            UserSummaryDTO user = userService.getUserSummary(userId);
            String status = user.getEnabled() ? "activé" : "désactivé";

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Utilisateur " + user.getUsername() + " " + status + " avec succès"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "erreur",
                    "Erreur lors de la modification : " + e.getMessage()
            );
        }

        return "redirect:/admin/users";
    }
}
