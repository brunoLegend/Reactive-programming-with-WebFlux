package is.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Relationship {
    long id;
    long studentId;
    long profId;

    public Relationship(long studentId, long profId) {
        this.profId = profId;
        this.studentId = studentId;
    }
}
