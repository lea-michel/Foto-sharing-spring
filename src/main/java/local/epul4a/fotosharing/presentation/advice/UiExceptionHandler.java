package local.epul4a.fotosharing.presentation.advice;

import local.epul4a.fotosharing.service.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class UiExceptionHandler {

    //exception  levee par Spring lorsqu'un fichier est trop volumineux
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(
            MaxUploadSizeExceededException ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(
                "erreur",
                "Le fichier dépasse la taille maximale autorisée (10 MB)"
        );
        return "redirect:/photo/upload";
    }

    @ExceptionHandler(FileSizeExceededException.class)
    public String handleFileTooLarge(
            FileSizeExceededException ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        return "redirect:/photo/upload";
    }

    @ExceptionHandler(InvalidMimeTypeException.class)
    public String handleInvalidMime(
            InvalidMimeTypeException ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        return "redirect:/photo/upload";
    }

    @ExceptionHandler(FileStorageException.class)
    public String handleStorageError(
            FileStorageException ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(
                "erreur",
                "Erreur lors de l’upload");
        return "redirect:/photo/upload";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex,
                                     RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("erreur", "Accès Interdit : " + ex.getMessage());
        return "redirect:/index";
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public String handlePhotoNotFound(PhotoNotFoundException ex,
                                      RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
        return "redirect:/index";
    }

    // Exception générique
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, RedirectAttributes redirectAttributes) {
        ex.printStackTrace();
        redirectAttributes.addFlashAttribute("erreur", "Une erreur inattendue est survenue");
        return "redirect:/index";
    }
}
