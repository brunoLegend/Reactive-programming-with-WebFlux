package is.server.relationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("relationships")
public class Relationship {
    @Id
    @Column("id")
    private Long id;

    @Column("s_id")
    private Long studentId;

    @Column("p_id")
    private Long profId;
}
