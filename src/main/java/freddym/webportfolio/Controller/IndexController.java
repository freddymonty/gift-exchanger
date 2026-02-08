package freddym.webportfolio.Controller;

import freddym.webportfolio.Model.User;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private final UserBean userBean;
    private final UserRepository userRepository;

    public IndexController( UserBean userBean, UserRepository userRepository) {
        this.userBean = userBean;
        this.userRepository = userRepository;
    }
    @GetMapping("/")
    public String index() {
        //create root user is not already
        if(userRepository.findUserByUsername("root") == null){
            User root = new User();
            root.setUsername("root");
            root.setPassword("1234");
            userRepository.save(root);
        }
        return "homePage";
    }

    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password){
        // look up user
        User user = userRepository.findUserByUsernameAndPassword(username,password);
        if(user == null){
            return "loginPage";
        }
        // set session user and return
        userBean.setUser(user);
        return "redirect:/";
    }
}
