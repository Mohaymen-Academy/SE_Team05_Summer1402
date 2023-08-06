package ir.shelmos.drive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import ir.shelmos.drive.models.Photo;
import ir.shelmos.drive.services.PhotosServices;

import java.io.IOException;

@RestController
public class PhotosController {

    @Autowired
    private PhotosServices photosServices;

    @GetMapping(value = "/photos/{id}")
    public Photo get(@PathVariable int id) {
        Photo photo = photosServices.get(id);
        if (photo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return photo;
    }

    @GetMapping(value = "/photos")
    public Iterable<Photo> getAll() {
        return photosServices.getAll();
    }

    @PostMapping(value = "/photos")
    public Photo create(@RequestPart("data") MultipartFile file) throws IOException {
        return photosServices.create(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }

    @DeleteMapping(value = "/photos/{id}")
    public void delete(@PathVariable int id) {
        photosServices.delete(id);
    }

}