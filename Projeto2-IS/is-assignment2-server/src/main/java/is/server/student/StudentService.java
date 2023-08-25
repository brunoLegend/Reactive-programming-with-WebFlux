package is.server.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepositry studentRepository;

    public Flux<Student> listAllStudents() {
        return studentRepository.findAll()
                .doOnError(throwable-> System.out.println("eer" + throwable));
    }

    public Mono<Student> insertUpdateStudent(Student s) {
        System.out.println("pronto para adicionar " + s.getName() + " data " + s.getBirthday());
        return studentRepository.save(s)
                .doOnSuccess(student -> System.out.println("Insert " + student.getName() + " id " + student.getId() +" success"))
                .onErrorResume(throwable -> {
                    System.out.println("erro a adicionar ou criar estudante");
                    return Mono.empty();
                });
    }

    public Mono<Student> listStudentByID(long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    System.out.println(student.getName());
                    return student;
                })
                .doOnError(throwable -> System.out.println("eer" + throwable));
    }

    // TODO
    // verificar que nao esta associado a ninguem
    public Mono<Void> deleteStudent(Long id) {
        System.out.println("estou aqui");
        return studentRepository.deleteById(id)
                .doOnError(throwable -> System.out.println("asdasda " + throwable));
    }
}
