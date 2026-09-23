package freddym.webportfolio.Controller;

import freddym.webportfolio.Model.Session;
import freddym.webportfolio.Model.User;
import freddym.webportfolio.Model.UserBean;
import freddym.webportfolio.Repository.ParticipantRepository;
import freddym.webportfolio.Repository.SessionRepository;
import freddym.webportfolio.Repository.UserRepository;
import freddym.webportfolio.Service.GiftExchangeService;
import freddym.webportfolio.Service.SmsNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IndexControllerAuthorizationTests {

    private UserBean userBean;
    private SessionRepository sessionRepository;
    private IndexController controller;

    @BeforeEach
    void setUp() {
        userBean = new UserBean();
        sessionRepository = mock(SessionRepository.class);
        controller = new IndexController(
                userBean,
                mock(UserRepository.class),
                sessionRepository,
                mock(ParticipantRepository.class),
                mock(GiftExchangeService.class),
                mock(SmsNotificationService.class)
        );
    }

    @Test
    void sessionManagementRequiresLogin() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.executeSessionPage(12, new ConcurrentModel())
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void anotherUsersSessionIsNotExposed() {
        loginAs(7);
        when(sessionRepository.findByIdAndUserId(12, 7)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.editSessionPage(12, new ConcurrentModel())
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void ownerCanOpenTheirSession() {
        User owner = loginAs(7);
        Session session = new Session();
        session.setId(12);
        session.setUser(owner);
        when(sessionRepository.findByIdAndUserId(12, 7)).thenReturn(Optional.of(session));
        ConcurrentModel model = new ConcurrentModel();

        String view = controller.executeSessionPage(12, model);

        assertEquals("executeSessionPage", view);
        assertSame(session, model.getAttribute("session"));
        verify(sessionRepository).findByIdAndUserId(12, 7);
    }

    private User loginAs(Integer userId) {
        User user = new User();
        user.setId(userId);
        userBean.setUser(user);
        return user;
    }
}
