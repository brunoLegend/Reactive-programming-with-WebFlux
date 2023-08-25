package is.server.relationship;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RelationshipRepository extends ReactiveCrudRepository<Relationship, Long> {
    @Query("""
            SELECT DISTINCT professors.id
            FROM students 
            INNER JOIN relationships
            ON relationships.s_id = students.id 
            INNER JOIN professors 
            ON professors.id = relationships.p_id 
            WHERE students.id=:id
            """
    )
    Flux<Long> getRelationshipProfessors(Long id);

    @Query("""
            SELECT DISTINCT students.id 
            FROM professors 
            INNER JOIN relationships 
            ON relationships.p_id = professors.id
            INNER JOIN students
            ON students.id = relationships.s_id
            WHERE professors.id=:id
            """
    )
    Flux<Long> getRelationshipStudents(Long id);
}
