package freddym.webportfolio.Controller;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Model.User;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.ParticipantRepository;
import freddym.webportfolio.Repository.SessionRepository;
import freddym.webportfolio.Repository.UserRepository;
import freddym.webportfolio.Service.GiftExchangeService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    private final GiftExchangeService giftExchangeService;


    public IndexController(UserBean userBean, UserRepository userRepository, SessionRepository sessionRepository, ParticipantRepository participantRepository, GiftExchangeService giftExchangeService) {
        this.userBean = userBean;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
        this.giftExchangeService = giftExchangeService;
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
    public String createNewSession(@RequestParam("sessionName") String sessionName){
        // create new session
        User user = userBean.getUser();

        Session newSession = new Session();
        Format f = new SimpleDateFormat("MM/dd/yy");
        String strDate = f.format(new Date());
        newSession.setDateCreated(strDate);
        newSession.setSessionName(sessionName);
        newSession.setUser(user.getId() == null ? userRepository.findUserByUsername(user.getUsername()) : user);

        newSession.setExecuted(false);
        Session savedSession = sessionRepository.save(newSession);

        return "redirect:/executeSession/" + savedSession.getId();
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

    @PostMapping("/executeSession/{id}")
    public String executeSession(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws IllegalAccessException {

        try {
            giftExchangeService.executeGiftExchange(id);
            redirectAttributes.addFlashAttribute("success", "Gift exchange executed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
//        return "redirect:/executeSession/" + id;
        return "homePage";
        }

    @GetMapping("/registerParticipant/{id}")
    public String registerParticipantPage(@PathVariable Integer id, Model model){
        Session session = sessionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Gift exchange session not found."));
        model.addAttribute("session", session);
        return "participantRegistrationPage";
    }

    @Transactional
    @PostMapping("/registerParticipant/{id}")
    public String registerParticipant(@PathVariable Integer id, @RequestParam String name, @RequestParam(required = false, defaultValue ="") String phoneNumber, @RequestParam(required = false, defaultValue = "false") boolean smsConsent, RedirectAttributes redirectAttributes){
        Session session = sessionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Gift exchange session not found."));

        if(session.isExecuted()){
            redirectAttributes.addFlashAttribute("error", "Gift exchange session already executed!");

            return "redirect:/registerParticipant/" + id;
        }

        if(smsConsent && phoneNumber == null || phoneNumber.isBlank()){
            redirectAttributes.addFlashAttribute("error", "Please enter a valid phone number if you would like to receive SMS notifications.");
            return "redirect:/registerParticipant/" + id;
        }

        Participant participant = new Participant();
        participant.setName(name.trim());
        participant.setSession(session);

        if(smsConsent){
            participant.setPhoneNumber(phoneNumber.trim());

            participant.setSmsConsent(true);

            participant.setSmsConsentTimestamp(LocalDateTime.now());

        } else {
            participant.setPhoneNumber(null);
            participant.setSmsConsent(false);
            participant.setSmsConsentTimestamp(null);
        }

        participantRepository.save(participant);

        redirectAttributes.addFlashAttribute("success", "Participant registered successfully!");

        return "redirect:/registerParticipant/" + id;

    }
}


