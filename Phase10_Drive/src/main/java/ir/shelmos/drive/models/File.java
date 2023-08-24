package ir.shelmos.drive.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("FILES")
@AllArgsConstructor
@Getter
@Builder
public class File {

    @Id
    @JsonIgnore
    private final int id;

    @NotEmpty
    private final String fileName;

    private final String contentType;

    @JsonIgnore
    private final byte[] data;
}
