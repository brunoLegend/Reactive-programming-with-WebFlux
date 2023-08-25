package is.server.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("students")
public class Student {
    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("birthday")
    private LocalDate birthday;

    @Column("credits")
    private int credits;

    @Column("avg_grade")
    private float avgGrade;
}
