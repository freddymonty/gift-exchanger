package freddym.webportfolio.Controller;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Model.User;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.ParticipantRepository;
import freddym.webportfolio.Repository.SessionRepository;
import freddym.webportfolio.Repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

    private final UserBean userBean;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ParticipantRepository participantRepository;

    public IndexController(UserBean userBean, UserRepository userRepository, SessionRepository sessionRepository, ParticipantRepository participantRepository) {
        this.userBean = userBean;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
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

    @PostMapping("/createNewSession")
    public String createNewSession(@RequestParam("sessionName") String sessionName, @RequestParam List<String> participantNames, @RequestParam List<String> participantPhoneNumbers, Model model){
        // create new session
        User user = userBean.getUser();

        Session newSession = new Session();
        Format f = new SimpleDateFormat("MM/dd/yy");
        String strDate = f.format(new Date());
        newSession.setDateCreated(strDate);
        newSession.setSessionName(sessionName);
        newSession.setUser(user.getId() == null ? userRepository.findUserByUsername(user.getUsername()) : user);

        List<Participant> participants = new ArrayList<>();
        sessionRepository.save(newSession);
        for(int i = 0; i < participantNames.size(); i++){
            String name = participantNames.get(i);
            String phoneNumber = participantPhoneNumbers.get(i);
            Participant participant = new Participant();
            participant.setName(name);
            participant.setPhoneNumber(phoneNumber);
            participant.setSession(newSession);
            participantRepository.save(participant);
        }
       participantRepository.saveAll(participants);


        model.addAttribute("user", userBean.getUser());
        return "homePage";
    }

    @GetMapping("viewSessions")
    public String viewSessions(Model model){
        User user = userBean.getUser();
        if (user == null) {
            return "redirect:/login";
        }

        List<Session> sessions = sessionRepository.findAllSessionsByUserId(user.getId());

        model.addAttribute("sessions", sessions);
        model.addAttribute("user", userBean.getUser());
        return "viewSessionsPage";
    }

    @GetMapping("/executeSession/{id}")
    public String executeSessionPage(@PathVariable Integer id, Model model){
        Session session = sessionRepository.findById(id).orElse(null);
        if (session == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", userBean.getUser());
        model.addAttribute("session", session);
        return "executeSessionPage";
    }

    @PostMapping("/executeSession/{id}")
    public String executeGiftExchange(@PathVariable Integer id, Model model){
        Session session = sessionRepository.findById(id).orElse(null);

        return "redirect:/executeSession/" + id;
    }

    @GetMapping("/editSession/{id}")
    public String editSessionPage(@PathVariable Integer id, Model model){
        Session session = sessionRepository.findById(id).orElse(null);
        if (session == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", userBean.getUser());
        model.addAttribute("session", session);
        return "editSessionPage";
    }

    @Transactional
    @PostMapping("/editSession/{id}")
    public String updateSession(
            @PathVariable Integer id,
            @RequestParam String sessionName,
            @RequestParam List<Integer> participantIds,
            @RequestParam List<String> participantNames,
            @RequestParam List<String> participantPhoneNumbers
    ) {
        Session session = sessionRepository.findById(id).orElseThrow();

        session.setSessionName(sessionName);
        sessionRepository.save(session);

        // Simple approach: delete and recreate participants
        participantRepository.deleteBySessionId(id);

        for (int i = 0; i < participantNames.size(); i++) {
            Participant participant = new Participant();
            participant.setName(participantNames.get(i));
            participant.setPhoneNumber(participantPhoneNumbers.get(i));
            participant.setSession(session);

            participantRepository.save(participant);
        }

        return "redirect:/executeSession/" + id;
    }
}

