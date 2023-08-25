package is.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

// Classe Aluno
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Student {
    private Long id;
    private String name;
    private LocalDate birthday;
    private int credits;
    private float avgGrade;

    public Student(String name, LocalDate birthday, int credits, float avgGrade) {
        this.name = name;
        this.birthday = birthday;
        this.credits = credits;
        this.avgGrade = avgGrade;
    }

    // toString organizado
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday.toString() +
                ", credits=" + credits +
                '}';
    }
}
