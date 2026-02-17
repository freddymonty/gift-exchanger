package freddym.webportfolio.Controller;

import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Model.User;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.SessionRepository;
import freddym.webportfolio.Repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

    private final UserBean userBean;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public IndexController( UserBean userBean, UserRepository userRepository, SessionRepository sessionRepository) {
        this.userBean = userBean;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
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

    @GetMapping("createSession")
    public String createSession(Model model){
        model.addAttribute("user", userBean.getUser());
        return "createSessionPage";
    }

    @PostMapping("createNewSession")
    public String createNewSession(@RequestParam("sessionName") String sessionName, @RequestParam List<String> participants, Model model){
        // create new session
        User user = userBean.getUser();
        Session newSession = new Session();
        Format f = new SimpleDateFormat("MM/dd/yy");
        String strDate = f.format(new Date());
        newSession.setDateCreated(strDate);
        newSession.setSessionName(sessionName);
        newSession.setUser(user.getId() == null ? userRepository.findUserByUsername(user.getUsername()) : user);
        newSession.setParticipants(participants);
        sessionRepository.save(newSession);

        model.addAttribute("user", userBean.getUser());
        return "homePage";
    }

}
