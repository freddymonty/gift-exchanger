package freddym.webportfolio.Repository;


import freddym.webportfolio.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUsername(String username);


    User findUserByUsernameAndPassword(String username, String password);
}
