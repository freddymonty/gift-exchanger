package freddym.webportfolio.Repository;

import freddym.webportfolio.Model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
}
