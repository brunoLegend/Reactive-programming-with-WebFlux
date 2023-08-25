package is.server.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    @GetMapping(value = "/relationship/professors/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getRelationshipProfessors(@PathVariable Long id) {
        System.out.println("Recebido GET em /api/relationship/professors/" + id);
        return relationshipService.getRelationshipProfessors(id);
    }

    @GetMapping(value = "/relationship/students/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getRelationshipStudents(@PathVariable Long id) {
        System.out.println("Recebido GET em /api/relationship/students/" + id);
        return relationshipService.getRelationshipStudents(id);
    }

    @PostMapping(value = "/relationship")
    public Mono<Relationship> createUpdateStudent(@RequestBody Relationship r) {
        System.out.println("Recebido POST em /api/relationship com a relação entre " + r.getStudentId() + " e " + r.getProfId());
        return relationshipService.createUpdateRelationship(r);
    }

    @DeleteMapping(value = "/relationship/{id}")
    public Mono<Void> deleteRelationship(@PathVariable Long id) {
        System.out.println("Recebido DELETE em api/relationship/" + id);
        return relationshipService.deleteRelationship(id);
    }
}
