package ir.shelmos.drive.controllers;

import java.util.random.RandomGenerator;

import ir.shelmos.drive.models.Photo;
import ir.shelmos.drive.repositories.PhotosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private PhotosRepository photosRepository;

    @GetMapping("/dice")
    public int dice() {
        return RandomGenerator.getDefault().nextInt(1, 7);
    }
}