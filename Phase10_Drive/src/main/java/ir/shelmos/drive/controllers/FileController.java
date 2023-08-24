package ir.shelmos.drive.controllers;

import ir.shelmos.drive.models.File;
import ir.shelmos.drive.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileServices;

    @GetMapping(value = "/files/{id}")
    public File get(@PathVariable int id) {
        File file = fileServices.get(id);
        if (file == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return file;
    }

    @GetMapping(value = "/files")
    public Iterable<File> getAll() {
        return fileServices.getAll();
    }

    @PostMapping(value = "/files")
    public File create(@RequestPart("data") MultipartFile file) throws IOException {
        return fileServices.create(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }

    @DeleteMapping(value = "/files/{id}")
    public void delete(@PathVariable int id) {
        fileServices.delete(id);
    }
}
