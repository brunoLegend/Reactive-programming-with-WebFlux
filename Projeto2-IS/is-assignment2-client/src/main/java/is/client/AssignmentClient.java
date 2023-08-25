package is.client;

import is.client.dto.Professor;
import is.client.dto.Relationship;
import is.client.dto.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import javax.management.relation.Relation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class AssignmentClient {
    private final WebClient webClient;

    // Cria WebClient no locahost, porto 8080
    @Autowired
    public AssignmentClient() {
        this.webClient = WebClient.create("http://localhost:8080");
    }

    // ######################################################################################################### //
    //                                          Operações CRUD                                                   //
    // ######################################################################################################### //

    /***
     * Envia GET ao servidor para obter todos os estudantes
     * @return devolve fluxo com estudantes
     */
    public Flux<Student> getStudents()  {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class);
    }

    /***
     * Envia GET ao servidor para obter todos os professores
     * @return devolve fluxo com professores
     */
    public Flux<Professor> getProfessors()  {
        return this.webClient.get().uri("api/professors")
                .retrieve()
                .bodyToFlux(Professor.class);
    }

    /***
     * Envia POST ao servidor contendo um estudante para ser inserido ou atualizado na base de dados
     * @param s Estudante a ser inserido na base de dados
     * @return Devolve Mono com o estudante inserido na base de dados
     */
    public Mono<Student> createUpdateStudent(Student s)  {
        return this.webClient.post().uri("api/student")
                .bodyValue(s)
                .retrieve()
                .bodyToMono(Student.class);
    }

    /***
     * Envia POST ao servidor contendo um professor para ser inserido ou atualizado na base de dados
     * @param p Professor a ser inserido na base de dados
     * @return Devolve Mono com o professor introduzido na base de dados
     */
    public Mono<Professor> createUpdateProfessor(Professor p)  {
        return this.webClient.post().uri("api/professor")
                .bodyValue(p)
                .retrieve()
                .bodyToMono(Professor.class);
    }

    /***
     * Envia GET para obter um estudante com o id fornecido
     * @param id Id do estudante e ser obtido
     * @return Devolve Mono com o estudante contido na base de dados
     */
    public Mono<Student> getStudentById(long id)  {
        return this.webClient.get().uri("api/student/{id}",id)
                .retrieve()
                .bodyToMono(Student.class);
    }

    /***
     * Envia DELETE ao servidor para eliminar um estudante com o id fornecido
     * @param id Id do estudante a ser eliminado
     * @return Devolve Mono contendo Void
     */
    public Mono<Void> deleteStudentById(Long id) {
        return this.webClient.delete().uri("api/student/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /***
     * Envia GET para obter um professor com o id fornecido
     * @param id Id do professor a ser obtido
     * @return Devolve Mono o professor contido na base de dados
     */
    public Mono<Professor> getProfessorById(long id) {
        return this.webClient.delete().uri("api/professor/{id}", id)
                .retrieve()
                .bodyToMono(Professor.class);
    }

    /***
     * Envia DELETE ao servidor para eliminar um professor com o id fornecido
     * @param id Id do professor a ser eliminado
     * @return Devolve Mono contendo Void
     */
    public Mono<Void> deleteProfessorById(Long id) {
        return this.webClient.delete().uri("api/professor/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /***
     * Envia POST ao servidor contendo uma relação para ser inserida ou atualizada na base de dados
     * @param relationship Relação a ser inserida na base de dados
     * @return Devolve Mono com a relação inserida na base de dados
     */
    public Mono<Relationship> createUpdateRelationship(Relationship relationship) {
        return this.webClient.post().uri("api/relationship")
                .bodyValue(relationship)
                .retrieve()
                .bodyToMono(Relationship.class);
    }

    /***
     * Envia DELETE ao servidor para eliminar uma relação com o id fornecido
     * @param id Id da relação a ser eliminada
     * @return Devolve Mono contendo Void
     */
    public Mono<Void> deleteRelationshopById(Long id) {
        return this.webClient.delete().uri("api/relationhip/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /***
     * Envia GET para obter ids dos professores associados ao estudante com o id fornecido
     * @param studentId Id do estudante
     * @return Devolve Flux com os ids dos professores
     */
    public Flux<Long> getRelationshipProfessors(Long studentId) {
        return this.webClient.get().uri("api/relationship/professors/{studentId}", studentId)
                .retrieve()
                .bodyToFlux(Long.class);
    }

    /***
     * Envia GET para obter ids dos estudantes associados ao professor com o id fornecido
     * @param professorId Id do professor
     * @return Devolve Flux com os ids dos estudantes
     */
    public Flux<Long> getRelationshipStudents(Long professorId) {
        return this.webClient.get().uri("api/relationship/students/{professorsId}", professorId)
                .retrieve()
                .bodyToFlux(Long.class);
    }

    // funçao para restabelecer ligaçao ao servidor
    public Flux<Student> testRetry() {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .doAfterRetry(retrySignal -> {
                            System.out.println("Retried " + retrySignal.totalRetries());
                        }))
                .onErrorResume(throwable -> {
                    System.out.println("Server is off!");
                    System.exit(-1);
                    return Flux.empty();
                })
                .doOnComplete(() -> System.out.println("Connected to server!"));
    }

    // ######################################################################################################### //
    //                                              Requisitos                                                   //
    // ######################################################################################################### //

    /***
     * 1 - Names and birthdates of all students.
     * Escreve para o ficheiro de output os nomes e datas de nascimento de todos os estudantes.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Flux, com objetos do tipo Student
     */
    public Flux<Student> getStudentNamesBirthdays(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3))
                        .jitter(0.1)
                        .doAfterRetry(retrySignal -> {
                            System.out.println("Retried " + retrySignal.totalRetries());
                        }))
                .map(student -> {
                    String name = student.getName();
                    String birthday = student.getBirthday().toString();
                    String content = name + " " + birthday;
                    writeToFile(file, content);
                    return student;
                });
    }

    /***
     * 2 - Total number of students.
     * Calcula o número total de estudantes e escreve o resultado para o ficheiro de output.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Mono com um Long, com o número total de estudantes
     */
    public Mono<Long> getNumberStudents(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .count()
                .map(total -> {
                    writeToFile(file, String.valueOf(total));

                    return total;
                });
    }

    /***
     * 3 - Total number of students that are active (have less than 180 credits).
     * Calcula o número total de estudantes ativos e escreve o resultado para o ficheiro de output.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Mono com um Long, com o número de estudantes ativos
     */
    public Mono<Long> getActiveStudents(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .filter(student -> student.getCredits() < 180)     // filtra os estudantes que têm menos de 180 creditos
                .count()
                .map(total -> {
                    writeToFile(file, total + " active Students");
                    return total;
                });
    }

    /***
     * 4 - Total number of courses completed for all students.
     * Calcula o número total de cadeiras completas por aluno.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Flux, com objetos do tipo Student
     */
    public Flux<Student> getCompletedCourses(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .map(student -> {
                    Integer numCourses = 0;

                    if (student.getCredits() != 0) {
                        // O número de cadeiras completadas é dado pela divisão de créditos por 6
                        numCourses = student.getCredits() / 6;
                    } else {
                        numCourses = 0;
                    }

                    writeToFile(file, student.getName() + " " + String.valueOf(numCourses));
                    return student;
                });
    }

    /***
     * 5 - Data of students that are in the last year of graduation, sorted by closest to completion.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Flux, com objetos do tipo Student
     */
    public Flux<Student> getLastYearStudents(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .filter(student -> student.getCredits() < 180 && student.getCredits() >= 120)
                .sort((a,b) -> {
                        if (a.getCredits() > b.getCredits()) {
                            return -1;
                        } else if (a.getCredits() == b.getCredits()) {
                            return 0;
                        } else return 1;
                    })
                .map(student-> {
                    writeToFile(file, student.toString());
                    return student;
                });
    }

    /***
     * 6 - Average and Standard Deviations of all student grades.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Mono, com um ArrayList que contém a média e o desvio padrão das notas de todos os alunos
     */
    public Mono<ArrayList<Float>> getAvgStdGrades(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .map(Student::getAvgGrade)
                .collectList()
                .flatMap(studentGradeList -> {
                    ArrayList<Float> result = new ArrayList<>();
                    ArrayList<Float> studentGrades = new ArrayList<>(studentGradeList);
                    float mean = 0;
                    float sum = 0;
                    float stdiv = 0;

                    // calcula média de notas
                    for (float grade : studentGrades) {
                        sum += grade;
                    }
                    mean = sum / (float) studentGrades.size();
                    result.add(mean);

                    // calcula desvio padrão
                    for (float grade : studentGrades) {
                        stdiv += Math.pow((grade - mean), 2);
                    }
                    stdiv = (float) Math.sqrt(stdiv / (float) studentGrades.size());
                    result.add(stdiv);

                    writeToFile(file, "Media: " + mean + "\nDesvio Padrão: " + stdiv);
                    return Mono.just(result);
                });
    }

    /***
     * 7 - Average and stadard deviations of students who have finished their graduation (180 credits).
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Mono, com um ArrayList que contém a média e o desvio padrão das notas de todos os alunos que terminaram a licenciatura
     */
    public Mono<ArrayList<Float>> getAvgStdFinished(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .filter(student -> student.getCredits() == 180)
                .map(Student::getAvgGrade)
                .collectList()
                .flatMap(studentGradeList -> {
                    ArrayList<Float> result = new ArrayList<>();
                    ArrayList<Float> studentGrades = new ArrayList<>(studentGradeList);
                    float mean = 0;
                    float sum = 0;
                    float stdiv = 0;

                    // calcula média de notas
                    for (float grade : studentGrades) {
                        sum += grade;
                    }
                    mean = sum / (float) studentGrades.size();
                    result.add(mean);

                    // calcula desvio padrão
                    for (float grade : studentGrades) {
                        stdiv += Math.pow((grade - mean), 2);
                    }
                    stdiv = (float) Math.sqrt(stdiv / (float) studentGrades.size());
                    result.add(stdiv);

                    writeToFile(file, "Media (licenciados): " + mean + "\nDesvio Padrão: " + stdiv);
                    return Mono.just(result);
                });
    }

    /***
     * 8 - Name of the eldest student.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Mono, com uma String com o nome do estudante mais velho
     */
    public Mono<String> getEldestStudent(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .sort((a, b) -> {                                       // Organiza-se o fluxo de estudantes por idades
                    if (a.getBirthday().isBefore(b.getBirthday())) {
                        return -1;
                    } else if (a.getBirthday().equals(b.getBirthday())) {
                        return 0;
                    }
                    else return 1;
                })
                .elementAt(0)           // acede-se ao estudante mais velho
                .map(Student::getName)
                .map(studentName -> {
                    writeToFile(file, studentName);
                    return studentName;
                });
    }

    /***
     * 9 - Average number of professors per student.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Mono, com um Float com o número médio de professores por estudante
     */
    public Mono<Float> avgProfs(File file) {
        // Obtém o número de estudantes
        Mono<Long> studentCount = this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .count();

        // Obtém o número de professores associados a todos os estudantes
        Mono<Long> profCount = this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .flatMap(student -> this.webClient.get().uri("api/relationship/professors/" + student.getId())
                        .retrieve()
                        .bodyToFlux(Long.class)
                        .count())
                .reduce((a, b) -> a + b);

        // Através dos dois fluxo, calcula a média de profs por estudante
        return Mono.zip(studentCount, profCount, (students, profs) -> (float) profs / students)
                .map(s -> {
                    writeToFile(file, String.valueOf(s));
                    return s;
                });
    }

    /***
     * 10 - Name and number of students per professor, sorted in descending order.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Fluxo com um objeto composto, que guarda o nome de todos os estudantes que dado professor está associado
     */
    public Flux<ProfessorWithStudents> getStudentsPerProfessor(File file) {
        return this.webClient.get().uri("api/professors")
                .retrieve()
                .bodyToFlux(Professor.class)
                .map(ProfessorWithStudents::new)
                .flatMap((professorWithStudents) -> {
                    return getStudentsId(professorWithStudents.getP())
                            .doOnNext((c) -> professorWithStudents.addStudent(c))
                            .thenMany(Flux.just(professorWithStudents));
                })
                .sort((a, b) -> {
                    if (a.getStudentNumber() > b.getStudentNumber()) {
                        return -1;
                    } else if (a.getStudentNumber() < b.getStudentNumber()) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .distinct()
                .map(s -> {
                    writeToFile(file, s.toString());
                    return s;
                });
    }

    // Retorna os nomes dos estudantes associados ao professor p, através do id do professor
    private Flux<String> getStudentsId(Professor p) {
        //vai procurar os ids dos estudantes do professor p
        return this.webClient.get().uri("api/relationship/students/" + p.getId())
                .retrieve()
                .bodyToFlux(Long.class)
                //vai buscar os nomes dos professores desse estudante conforme os seus ids
                .flatMap(id-> getStudentName(id))
                .doOnError(e-> System.out.println("erro em getstudentesId " + e));
    }

    // Retorna o nome de um estudante, atraves do seu id
    public Flux<String> getStudentName(Long id){
        return this.webClient.get().uri("api/student/" + id)
                .retrieve()
                .bodyToFlux(Student.class)
                .map(Student::getName)
                .doOnError((d) -> System.out.println("erro " + d));
    }

    /***
     * 11 - Complete data of all students, by adding the name of their professors.
     * @param file Ficheiro para onde o output será escrito
     * @return Devolve um Fluxo com um objeto composto, que guarda os dados do estudante junto com o nome de todos os professores a que este esta associado
     */
    public Flux<StudentWithProfs> getStudentsWithProfessors(File file) {
        return this.webClient.get().uri("api/students")
                .retrieve()
                .bodyToFlux(Student.class)
                .map(StudentWithProfs::new)
                .flatMap((studentWithProfs) -> {
                    return getProfsId(studentWithProfs.getS())
                            .doOnNext(studentWithProfs::setProfs)
                            .thenMany(Flux.just(studentWithProfs));
                })
                .map(s -> {
                    writeToFile(file, s.toString());
                    return s;
                });
    }

    // Retorna os nomes dos professores associados ao estudante s, através do id do estudante
    private Flux<String> getProfsId(Student s) {
        // vai procurar os ids dos professores do estudante s
        return this.webClient.get().uri("api/relationship/professors/" + s.getId())
                .retrieve()
                .bodyToFlux(Long.class)
                //vai buscar os nomes dos professores desse estudante conforme os seus ids
                .flatMap(id -> getProfessorName(id));
    }

    // Retorna o nome de um professor, atraves do seu id
    public Flux<String> getProfessorName(Long id) {
        return this.webClient.get().uri("api/professor/" + id)
                .retrieve()
                .bodyToFlux(Professor.class)
                .map(Professor::getName);
    }

    // Função para escrever para ficheiros
    public void writeToFile(File filename, String Content) {
        try {
            FileWriter myWriter = new FileWriter(filename, true);
            myWriter.write(Content+"\n");
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

// Classe auxiliar para associar professores a um estudante
class StudentWithProfs {
    Student s;
    ArrayList<String> profs = new ArrayList<>();

    public StudentWithProfs(Student s) {
        this.s = s;
    }

    public void setProfs(String p) {
        this.profs.add(p);
    }

    public Student getS() {
        return s;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name=" + s.getName() + " id=" + s.getId() + " birthday=" + s.getBirthday() + " credits=" + s.getCredits() + " grade=" + s.getAvgGrade() + ", profs=" + profs +
                '}';
    }
}

// Classe auxiliar para associar estudantes a um professor
class ProfessorWithStudents {
    Professor p;
    ArrayList<String> students;

    public ProfessorWithStudents(Professor p) {
        this.p = p;
        this.students = new ArrayList<>();
    }

    public Professor getP() {
        return p;
    }

    public void addStudent(String s) {
        students.add(s);
    }

    public int getStudentNumber() {
        return students.size();
    }

    @Override
    public String toString() {
        return "Professor{" +
                "name=" + p.getName() + " id=" + p.getId() + " students=" + students + " count=" + students.size() +
                '}';
    }
}