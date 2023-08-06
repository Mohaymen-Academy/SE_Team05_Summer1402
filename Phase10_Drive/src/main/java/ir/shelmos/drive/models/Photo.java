package ir.shelmos.drive.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Table("PHOTOS")
public class Photo {
    @Id
    private long id;

    @NotEmpty
    private String fileName;
    private String contentType;

    @JsonIgnore
    private byte[] data;

}
