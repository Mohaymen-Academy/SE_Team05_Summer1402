package ir.shelmos.drive.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("PHOTOS")
public class Photo {
    @Id
    private int id;

    @NotEmpty
    private String fileName;
    private String contentType;

    @JsonIgnore
    private byte[] data;

    public Photo() {
    }

    public int getId() {
        return this.id;
    }

    public @NotEmpty String getFileName() {
        return this.fileName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFileName(@NotEmpty String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonIgnore
    public void setData(byte[] data) {
        this.data = data;
    }

}
