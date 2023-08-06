package ir.shelmos.drive.controllers;

import ir.shelmos.drive.models.Photo;
import ir.shelmos.drive.services.PhotosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class DownloadController {

    @Autowired
    private PhotosServices photosServices;

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> get(@PathVariable int id) {
        Photo photo = photosServices.get(id);
        if (photo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.valueOf(photo.getContentType()));
        var contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(photo.getFileName())
                .build();
        headers.setContentDisposition(contentDisposition);
        return new ResponseEntity<>(photo.getData(),headers,HttpStatus.OK);
    }

}
