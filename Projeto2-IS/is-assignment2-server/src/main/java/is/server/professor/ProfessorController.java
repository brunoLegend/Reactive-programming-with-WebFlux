package is.server.professor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping(value = "/professors",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Professor> getAllProfessors(){
        System.out.println("Recebido GET em /api/professors");
        return professorService.listAllProfessors();
    }

    @GetMapping(value = "professor/{id}")
    public Mono<Professor> getProfessorByID(@PathVariable long id){
        System.out.println("Recebido GET em /api/professor/" + id);
        return professorService.listProfessorById(id);
    }

    @PostMapping(value="/professor")
    public Mono<Professor> createProfessor(@RequestBody Professor p) {
        System.out.println("Recebido POST em /api/professor com o Professor " + p.getName());
        return  professorService.insertUpdateProfessor(p);
    }

    @DeleteMapping(value="professor/{id}")
    public Mono<Void> deleteProfessor(@PathVariable Long id) {
        System.out.println("Recebido DELELE em /api/professor/" + id);
        return professorService.deleteProfessor(id);
    }
}
