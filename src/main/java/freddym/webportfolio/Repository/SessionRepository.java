package freddym.webportfolio.Repository;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Query("SELECT s FROM Session s WHERE s.user.id = :userId")
    List<Session> findAllSessionsByUserId(Integer userId);



}
