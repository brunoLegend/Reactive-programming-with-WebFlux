package is.server.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/students",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> getAllStudents() {
        System.out.println("Recebido GET em api/students");
        return studentService.listAllStudents();
    }

    @GetMapping(value = "student/{id}")
    public Mono<Student> getStudent(@PathVariable long id) {
        System.out.println("Recebido GET em api/student/" + id);
        return studentService.listStudentByID(id);
    }

    @PostMapping(value = "/student")
    public Mono<Student> createStudent(@RequestBody Student s) {
        System.out.println("Recebido POST em api/student com o estudante " + s.getName());
        return studentService.insertUpdateStudent(s);
    }

    @DeleteMapping(value = "student/{id}")
    public Mono<Void> deleteStudent(@PathVariable Long id) {
        System.out.println("Recebido DELETE em api/student/" + id);
        return studentService.deleteStudent(id);
    }
}