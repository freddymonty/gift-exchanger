package freddym.webportfolio.Controller;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Model.User;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.ParticipantRepository;
import freddym.webportfolio.Repository.SessionRepository;
import freddym.webportfolio.Repository.UserRepository;
import freddym.webportfolio.Service.GiftExchangeService;
import freddym.webportfolio.Service.SmsNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestClientException;
import java.time.LocalDateTime;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final UserBean userBean;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ParticipantRepository participantRepository;
    private final GiftExchangeService giftExchangeService;
    private final SmsNotificationService smsNotificationService;


    public IndexController(
            UserBean userBean,
            UserRepository userRepository,
            SessionRepository sessionRepository,
            ParticipantRepository participantRepository,
            GiftExchangeService giftExchangeService,
            SmsNotificationService smsNotificationService
    ) {
        this.userBean = userBean;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
        this.giftExchangeService = giftExchangeService;
        this.smsNotificationService = smsNotificationService;
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
        Session session = requireOwnedSession(id);
        model.addAttribute("user", userBean.getUser());
        model.addAttribute("session", session);
        return "executeSessionPage";
    }


    @GetMapping("/editSession/{id}")
    public String editSessionPage(@PathVariable Integer id, Model model){
        Session session = requireOwnedSession(id);
        model.addAttribute("user", userBean.getUser());
        model.addAttribute("session", session);
        return "editSessionPage";
    }

    @Transactional
    @PostMapping("/editSession/{id}")
    public String updateSession(
            @PathVariable Integer id,
            @RequestParam String sessionName) {
        Session session = requireOwnedSession(id);

        session.setSessionName(sessionName);
        sessionRepository.save(session);


        return "redirect:/executeSession/" + id;
    }

    @Transactional
    @PostMapping("/removeParticipant/{sessionId}/{participantId}")
    public String removeParticipant(@PathVariable Integer sessionId, @PathVariable Integer participantId){
        requireOwnedSession(sessionId);
        Participant participant = participantRepository.findById(participantId).orElseThrow();

        if(!participant.getSession().getId().equals(sessionId)){
            throw new IllegalArgumentException("Participant does not belong to the specified session.");
        }

        participantRepository.delete(participant);
        return "redirect:/editSession/" + sessionId;
    }


    @PostMapping("/executeSession/{id}")
    public String executeSession(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws IllegalAccessException {
        User owner = requireLoggedInUser();
        requireOwnedSession(id, owner);

        try {
            giftExchangeService.executeGiftExchange(id, owner.getId());
            redirectAttributes.addFlashAttribute("success", "Gift exchange executed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/executeSession/" + id;
        }

    private User requireLoggedInUser() {
        User user = userBean.getUser();
        if (user == null || user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required.");
        }
        return user;
    }

    private Session requireOwnedSession(Integer sessionId) {
        return requireOwnedSession(sessionId, requireLoggedInUser());
    }

    private Session requireOwnedSession(Integer sessionId, User owner) {
        return sessionRepository.findByIdAndUserId(sessionId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registerParticipant/{id}")
    public String registerParticipantPage(@PathVariable Integer id, Model model){
        Session session = sessionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Gift exchange session not found."));
        model.addAttribute("session", session);
        model.addAttribute("smsBrandName", smsNotificationService.getBrandName());
        model.addAttribute("smsFromNumber", smsNotificationService.getFromNumber());
        return "participantRegistrationPage";
    }

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

        participantRepository.saveAndFlush(participant);

        if (smsConsent) {
            try {
                smsNotificationService.sendOptInConfirmation(participant.getPhoneNumber());
            } catch (RestClientException exception) {
                logger.warn("Participant {} registered, but the SMS confirmation could not be sent.",
                        participant.getId(), exception);
                redirectAttributes.addFlashAttribute(
                        "warning",
                        "You joined successfully, but we could not send the confirmation text. "
                                + "Please verify your mobile number or contact support."
                );
            }
        }

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
