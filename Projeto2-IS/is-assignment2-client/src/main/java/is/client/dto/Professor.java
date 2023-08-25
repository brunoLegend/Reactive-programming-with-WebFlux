package is.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Classe Professor
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Professor {
    private Long id;
    private String name;

    public Professor(String name) {
        this.name = name;
    }
}
