package freddym.webportfolio.Controller;


import freddym.webportfolio.Model.User;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final UserRepository userRepository;
    private final UserBean userBean;

    public LoginController(UserBean userBean, UserRepository userRepository) {
        this.userBean = userBean;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        // look up user
        User user = userRepository.findUserByUsernameAndPassword(username,password);

        if(user == null){
            model.addAttribute("error", "Wrong Username or Password.");
            return "loginPage";
        }

        userBean.setUser(user);
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        userBean.logout();
        return "redirect:/";
    }

    @GetMapping("/createAccount")
    public String createAccount(){
        return "createAccountPage";
    }

    @PostMapping("/newAccount")
    public String newAccount(@RequestParam("username") String username, @RequestParam("password") String password, Model model){

        // if acc alr exists dont make a copy
        if(userRepository.findUserByUsername(username) != null){
            model.addAttribute("error", "Email already registered.");
            return "createAccountPage";
        }

        // create new user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        userRepository.save(newUser);
        userBean.setUser(newUser);

        return "redirect:/home";

    }

}
