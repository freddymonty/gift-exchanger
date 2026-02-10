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
public class IndexController {

    private final UserBean userBean;
    private final UserRepository userRepository;

    public IndexController( UserBean userBean, UserRepository userRepository) {
        this.userBean = userBean;
        this.userRepository = userRepository;
    }
    @GetMapping("/")
    public String index(Model model) {
        //create root user is not already
        if(userRepository.findUserByUsername("root") == null){
            User root = new User();
            root.setUsername("root");
            root.setPassword("1234");
            userRepository.save(root);

        }
        model.addAttribute("user", userBean.getUser());
        return "homePage";
    }

    @GetMapping("/home")
    public String home(Model model) {
        //create root user is not already
        if(userRepository.findUserByUsername("root") == null){
            User root = new User();
            root.setUsername("root");
            root.setPassword("1234");
            userRepository.save(root);

        }
        model.addAttribute("user", userBean.getUser());
        return "homePage";
    }

}
