package is.server.student;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositry extends ReactiveCrudRepository<Student, Long> {
}

