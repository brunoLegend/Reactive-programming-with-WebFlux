package is.server.professor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public Flux<Professor> listAllProfessors (){
        return professorRepository.findAll()
                .doOnError(throwable -> System.out.println("eer" + throwable));
    }

    public Mono<Professor> insertUpdateProfessor(Professor p) {
        System.out.println("pronto para adicionar " + p.getName());
        return professorRepository.save(p)
                .doOnSuccess(professor -> System.out.println("Inserted " + professor.getName() + " with success"));
    }

    public Mono<Void> deleteProfessor(Long id) {
        System.out.println("A apagar o professor " + id);
        return professorRepository.deleteById(id)
                .doOnError(throwable -> System.out.println("errossssss"));
    }

    public Mono<Professor> listProfessorById(long id) {
        return professorRepository.findById(id)
                .map(professor -> {
                    System.out.println(professor.getName());
                    return professor;
                })
                .doOnError(throwable -> System.out.println("eer" + throwable));
    }
}
