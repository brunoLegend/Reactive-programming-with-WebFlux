package is.server.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class RelationshipService {

    @Autowired
    private RelationshipRepository relationshipRepository;

    public Flux<Long> getRelationshipProfessors(Long id) {
        return relationshipRepository.getRelationshipProfessors(id)
                .doOnError(throwable -> {
                    System.out.println("Aluno não tem relações");
                });
    }

    public Flux<Long> getRelationshipStudents(Long id) {
        return relationshipRepository.getRelationshipStudents(id)
                .doOnError(throwable -> {
                    System.out.println("Professor não tem relações");
                });
    }

    public Mono<Relationship> createUpdateRelationship(Relationship r) {
        return relationshipRepository.save(r)
                .doOnSuccess(relationship -> System.out.println("Inserted relationship " + relationship.getProfId() + " " + relationship.getStudentId() + " with success."))
                .onErrorResume(throwable -> {
                    System.out.println("Relação já existe");
                    return Mono.empty();
                });
    }

    public Mono<Void> deleteRelationship(Long id) {
        return relationshipRepository.deleteById(id)
                .doOnSuccess(s -> System.out.println("Deleted " + s))
                .onErrorResume(throwable -> {
                    System.out.println("Relação não existe");
                    return Mono.empty();
                });
    }
}
