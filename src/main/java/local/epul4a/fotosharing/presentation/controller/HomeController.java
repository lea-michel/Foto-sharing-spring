package local.epul4a.fotosharing.presentation.controller;

import local.epul4a.fotosharing.presentation.dto.PhotoDTO;
import local.epul4a.fotosharing.service.photo.PhotoService;
import local.epul4a.fotosharing.service.security.UserPrincipal;
import local.epul4a.fotosharing.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    private final PhotoService photoService;

    public HomeController(PhotoService photoService, UserService userService) {
        this.photoService = photoService;
    }

    @GetMapping({"/", "/index"})
    public String homePage(Model model,
                           @AuthenticationPrincipal UserPrincipal principal,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<PhotoDTO> photosPage;

        if (principal != null) {
            photosPage = photoService.getVisiblePhotosForUser(
                    principal.getUser().getId(),
                    pageable
            );
        } else {
            photosPage = photoService.getPublicPhotos(pageable);
        }

        model.addAttribute("photosPage", photosPage);
        model.addAttribute("photos", photosPage.getContent());

        return "index";
    }
}
