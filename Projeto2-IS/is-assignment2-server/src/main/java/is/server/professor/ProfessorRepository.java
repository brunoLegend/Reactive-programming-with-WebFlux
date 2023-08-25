package is.server.professor;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends ReactiveCrudRepository<Professor, Long> {
}
