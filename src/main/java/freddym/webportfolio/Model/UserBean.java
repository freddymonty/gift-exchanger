package freddym.webportfolio.Model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserBean {
    private User user;

    public boolean isLoggedIn() {
        return user != null;
    }

    public void login(User user) {
        this.user = user;
    }

    public void logout() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }

        public void setUser(User user) {
            this.user = user;
        }
}
