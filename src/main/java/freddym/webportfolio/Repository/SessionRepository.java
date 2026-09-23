package freddym.webportfolio.Repository;

import freddym.webportfolio.Model.Participant;
import freddym.webportfolio.Model.Session;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Query("SELECT s FROM Session s WHERE s.user.id = :userId")
    List<Session> findAllSessionsByUserId(Integer userId);

    Optional<Session> findByIdAndUserId(Integer id, Integer userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Session s WHERE s.id = :id AND s.user.id = :userId")
    Optional<Session> findOwnedByIdForUpdate(Integer id, Integer userId);
}
