package freddym.webportfolio.Repository;

import freddym.webportfolio.Model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

List<Participant> findBySessionId(Integer sessionId);

    @Transactional
    void deleteBySessionId(Integer id);
}
