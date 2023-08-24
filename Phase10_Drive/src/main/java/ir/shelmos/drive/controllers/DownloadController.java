package ir.shelmos.drive.controllers;

import ir.shelmos.drive.models.File;
import ir.shelmos.drive.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class DownloadController {

    private final FileService fileServices;

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> get(@PathVariable int id) {
        File file = fileServices.get(id);

        if (file == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        ContentDisposition contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(file.getFileName())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(file.getData(), headers, HttpStatus.OK);
    }
}
