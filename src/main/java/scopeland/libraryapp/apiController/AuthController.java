package scopeland.libraryapp.apiController;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import scopeland.libraryapp.entities.User;
import scopeland.libraryapp.entities.dtos.UserDto;
import scopeland.libraryapp.service.UserService;

@RestController
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /*
     * Registers a User
     * 
     * @param userDto - The user details to be registered
     * 
     * @param result - the result used to store errors
     * 
     * @param model - The model we can send back to the page
     * 
     * @return - The book that was updated
     */
    @PostMapping("/register/save")
    public ModelAndView registration(@Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {

        // Check if user already exists
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        // If errors return to register page and pass through errors which validate UI
        if (result.hasErrors()) {
            return new ModelAndView("/register").addObject("user", userDto);
        }

        // If no errors save and take to login page
        userService.saveUser(userDto);
        return new ModelAndView("/login").addObject("registerSuccess");
    }

    /*
     * List all existing users
     * 
     * @param model - The model we can send back to the page
     * 
     * @return - the name of the page to be shown
     */
    @GetMapping("api/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
