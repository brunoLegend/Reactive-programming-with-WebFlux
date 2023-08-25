package is.client;

import is.client.dto.Professor;
import is.client.dto.Relationship;
import is.client.dto.Student;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class IsAssignment2ClientApplication {
    public static void main(String[] args) throws InterruptedException {
        // Inicializa o webclient
        AssignmentClient client = new AssignmentClient();
/*
        // cria 200 estudantes e 200 profs
        for (int i = 0; i < 200; i++) {
            long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            int upper = 180;
            int lower = 0;
            int rand = (int)(Math.random() * (upper-lower+1));
            int result = rand - rand%6 + lower;
            Random r = new Random();
            float random = 0 + r.nextFloat() * (20);
            Student s = new Student(RandomStringUtils.random(20, true, false), randomDate, result, random);
            client.createUpdateStudent(s).subscribe();

            Professor p = new Professor(RandomStringUtils.random(20, true, false));
            client.createUpdateProfessor(p).subscribe();
            Thread.sleep(5);
        }

        // cria relacoes
        for (int i = 1; i <= 40; i++) {
            for (int j = 1; j <= 40; j++) {
                client.createUpdateRelationship(new Relationship(i, j)).subscribe();
            }
        }
        */

        // Cria os ficheiros para os vários requisitos
        File file1 = createFile("output/namesAndBirthdays.txt");
        File file2 = createFile("output/numberOfStudents.txt");
        File file3 = createFile("output/numberOfActiveStudents.txt");
        File file4 = createFile("output/numberOfCompletedCourses.txt");
        File file5 = createFile("output/studentsLastYearGraduation.txt");
        File file6 = createFile("output/avgAndStdDeviation.txt");
        File file7 = createFile("output/avgAndStdDeviationFinishedGrad.txt");
        File file8 = createFile("output/nameOfEldestStudent.txt");
        File file9 = createFile("output/avgProfessorsPerStudent.txt");
        File file10 = createFile("output/studentsPerProfessor.txt");
        File file11 = createFile("output/completeStudentData.txt");

        // Executa as funções dos vários requisitos

        client.testRetry().subscribe();

        client.getStudentNamesBirthdays(file1).subscribe();
        client.getNumberStudents(file2).subscribe();
        client.getActiveStudents(file3).subscribe();
        client.getCompletedCourses(file4).subscribe();
        client.getLastYearStudents(file5).subscribe();
        client.getAvgStdGrades(file6).subscribe();
        client.getAvgStdFinished(file7).subscribe();
        client.getEldestStudent(file8).subscribe();
        client.avgProfs(file9).subscribe();
        client.getStudentsPerProfessor(file10).subscribe();
        client.getStudentsWithProfessors(file11).subscribe();

        Thread.sleep(15000);
    }

    // Função auxiliar para criar ficheiro, se não existir
    public static File createFile(String filename) {
        File myObj = null;
        try {
            myObj = new File(String.valueOf(filename));
            if (myObj.createNewFile()) {
                //System.out.println("File created: " + myObj.getName());
            } else {
                //System.out.println("File already exists.");
                FileWriter fileWriter = new FileWriter(String.valueOf(filename), false);
                fileWriter.write("\nNova Execução\n");
                fileWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return myObj;
    }
}
