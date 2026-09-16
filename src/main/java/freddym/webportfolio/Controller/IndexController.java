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
import java.time.LocalDateTime;

import java.text.Format;
import java.text.SimpleDateFormat;
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
            @RequestParam String sessionName) {
        Session session = sessionRepository.findById(id).orElseThrow();

        session.setSessionName(sessionName);
        sessionRepository.save(session);


        return "redirect:/executeSession/" + id;
    }

    @Transactional
    @PostMapping("/removeParticipant/{sessionId}/{participantId}")
    public String removeParticipant(@PathVariable Integer sessionId, @PathVariable Integer participantId){
        Participant participant = participantRepository.findById(participantId).orElseThrow();

        if(!participant.getSession().getId().equals(sessionId)){
            throw new IllegalArgumentException("Participant does not belong to the specified session.");
        }

        participantRepository.delete(participant);
        return "redirect:/editSession/" + sessionId;
    }


    @PostMapping("/executeSession/{id}")
    public String executeSession(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws IllegalAccessException {

        try {
            giftExchangeService.executeGiftExchange(id);
            redirectAttributes.addFlashAttribute("success", "Gift exchange executed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/home";
        }

    @GetMapping("/registerParticipant/{id}")
    public String registerParticipantPage(@PathVariable Integer id, Model model){
        Session session = sessionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Gift exchange session not found."));
        model.addAttribute("session", session);
        return "participantRegistrationPage";
    }

    @Transactional
    @PostMapping("/registerParticipant/{id}")
    public String registerParticipant(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam(
                    required = false,
                    defaultValue = ""
            ) String phoneNumber,
            @RequestParam(
                    required = false,
                    defaultValue = "false"
            ) boolean smsConsent,
            RedirectAttributes redirectAttributes
    ) {

        Session session = sessionRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Gift exchange session not found."
                        )
                );

        if (session.isExecuted()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Registration for this session is closed."
            );

            return "redirect:/registerParticipant/" + id;
        }

        if (smsConsent &&
                (phoneNumber == null || phoneNumber.isBlank())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Please enter a mobile number if you would like SMS notifications."
            );

            return "redirect:/registerParticipant/" + id;
        }

        Participant participant = new Participant();

        participant.setName(name.trim());
        participant.setSession(session);


        if (smsConsent) {

            participant.setPhoneNumber(
                    phoneNumber.trim()
            );

            participant.setSmsConsent(true);

            participant.setSmsConsentTimestamp(
                    LocalDateTime.now()
            );

        } else {

            participant.setPhoneNumber(null);
            participant.setSmsConsent(false);
            participant.setSmsConsentTimestamp(null);
        }

        participantRepository.save(participant);

        redirectAttributes.addFlashAttribute(
                "success",
                "You have successfully joined the gift exchange."
        );

        // Send participant to their private edit page
        return "redirect:/registerParticipant/" + id;
    }

    @GetMapping("/PrivacyPolicy")
    public String privacyPolicyPage() {
        return "privacyPolicyPage";
    }

    @GetMapping("/TermsOfService")
    public String termsOfServicePage() {
        return "termsOfServicePage";
    }


}


