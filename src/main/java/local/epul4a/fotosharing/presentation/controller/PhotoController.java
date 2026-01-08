package local.epul4a.fotosharing.presentation.controller;

import jakarta.validation.Valid;
import local.epul4a.fotosharing.presentation.dto.*;
import local.epul4a.fotosharing.service.photo.PhotoService;
import local.epul4a.fotosharing.service.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/photo")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/upload")
    public String showUploadNewPhotoForm(Model model) {
        PhotoUploadDTO photoUploadDTO = new PhotoUploadDTO();
        model.addAttribute("photoUploadDTO", photoUploadDTO);
        return "photo/upload";
    }

    @PostMapping("/save")
    public String uploadPhoto(
            @Valid @ModelAttribute("photoUploadDTO") PhotoUploadDTO dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            return "photo/upload";
        }

        photoService.upload(dto, principal.getUser().getId());

        redirectAttributes.addFlashAttribute(
                "message",
                "Image uploadée avec succès"
        );

        return "redirect:/index";

    }


    @GetMapping("/{id}")
    public String showPhoto(@PathVariable Long id,
                            Model model,
                            @AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal != null ? principal.getUser().getId() : null;

        PhotoDetailsDTO photo = photoService.getPhotoDetails(id, userId);
        PhotoPermissionsDTO permissions = photoService.getUserPermissions(id, userId);
        List<CommentDTO> comments = photoService.getComments(id);

        model.addAttribute("photo", photo);
        model.addAttribute("permissions", permissions);
        model.addAttribute("comments", comments);

        return "photo/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = principal.getUser().getId();

        // Vérification via DTO
        PhotoPermissionsDTO permissions = photoService.getUserPermissions(id, userId);
        if (!permissions.isCanAdmin()) {
            redirectAttributes.addFlashAttribute("erreur", "Vous n'avez pas le droit de modifier cette photo");
            return "redirect:/photo/" + id;
        }

        // Récupération des données via DTO
        PhotoDetailsDTO photo = photoService.getPhotoDetails(id, userId);

        PhotoUpdateDTO updateDTO = new PhotoUpdateDTO();
        updateDTO.setTitle(photo.getTitle());
        updateDTO.setDescription(photo.getDescription());
        updateDTO.setVisibility(photo.getVisibility());

        model.addAttribute("photoUpdateDTO", updateDTO);
        model.addAttribute("photoId", id);

        return "photo/edit";
    }

    @PostMapping("/{id}/update")
    public String updatePhoto(
            @PathVariable Long id,
            @Valid @ModelAttribute("photoUpdateDTO") PhotoUpdateDTO dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("photoId", id);
            return "photo/edit";
        }

        // Service gère la vérification et la logique
        photoService.updatePhoto(id, dto, principal.getUser().getId());

        redirectAttributes.addFlashAttribute("message", "Photo modifiée avec succès");
        return "redirect:/photo/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deletePhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes
    ) {
        // Service gère la vérification et la logique
        photoService.deletePhoto(id, principal.getUser().getId());

        redirectAttributes.addFlashAttribute("message", "Photo supprimée avec succès");
        return "redirect:/index";
    }

    // ========== PARTAGE ==========
    @GetMapping("/{id}/share")
    public String showShareForm(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = principal.getUser().getId();

        // Vérification via DTO
        PhotoPermissionsDTO permissions = photoService.getUserPermissions(id, userId);
        if (!permissions.isOwner()) {
            redirectAttributes.addFlashAttribute("erreur", "Seul le propriétaire peut partager cette photo");
            return "redirect:/photo/" + id;
        }

        PhotoDetailsDTO photo = photoService.getPhotoDetails(id, userId);
        model.addAttribute("photo", photo);
        model.addAttribute("shareRequest", new ShareRequestDTO());

        return "photo/share";
    }

    @PostMapping("/{id}/share")
    public String sharePhoto(
            @PathVariable Long id,
            @Valid @ModelAttribute ShareRequestDTO shareRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            Long userId = principal.getUser().getId();
            PhotoDetailsDTO photo = photoService.getPhotoDetails(id, userId);
            model.addAttribute("photo", photo);
            model.addAttribute("erreur", "Veuillez corriger les erreurs dans le formulaire");
            return "photo/share";
        }

        shareRequest.setPhotoId(id);

        // Service gère la logique
        shareRequest.setPhotoId(id);

        try {
            photoService.sharePhoto(shareRequest, principal.getUser().getId());
            redirectAttributes.addFlashAttribute("message", "Photo partagée avec succès");
            return "redirect:/photo/" + id;

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/photo/" + id + "/share";
        }

    }

    @PostMapping("/{photoId}/unshare/{userId}")
    public String unsharePhoto(
            @PathVariable Long photoId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes
    ) {
        // Service gère la vérification et la logique
        photoService.unsharePhoto(photoId, userId, principal.getUser().getId());

        redirectAttributes.addFlashAttribute("message", "Partage retiré avec succès");
        return "redirect:/photo/" + photoId;
    }

    // ========== COMMENTAIRES ==========
    @PostMapping("/{id}/comment")
    public String addComment(
            @PathVariable Long id,
            @RequestParam String content,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes
    ) {
        CommentCreateDTO commentDTO = new CommentCreateDTO();
        commentDTO.setPhotoId(id);
        commentDTO.setContent(content);

        // Service gère la vérification et la logique
        photoService.addComment(commentDTO, principal.getUser().getId());

        redirectAttributes.addFlashAttribute("message", "Commentaire ajouté");
        return "redirect:/photo/" + id;
    }


}
