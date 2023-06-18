package scopeland.libraryapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import scopeland.libraryapp.entities.dtos.UserDto;

/*
 * Web Controller for endpoints to hit web pages
 */
@Controller
public class HomeController {

    public HomeController() {

    }

    /*
     * Library Catalogue Home page
     */
    @RequestMapping(value = "/")
    public String index(Model model) {
        return "bookList";
    }

    /*
     * Library Catalogue Add Book page
     */
    @RequestMapping(value = "/addBook")
    public String addBook(Model model) {
        return "bookAdd";
    }

    /*
     * Library Catalogue Edit Book page
     * - update
     * - delete
     * - basic borrow/return
     */
    @RequestMapping(value = "/editBook")
    public String editBook(Model model) {
        return "bookEdit";
    }

    /*
     * Page for registering Users
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    /*
     * Page for logging in
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
