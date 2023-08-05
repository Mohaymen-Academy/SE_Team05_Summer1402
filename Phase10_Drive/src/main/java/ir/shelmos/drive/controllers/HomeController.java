package ir.shelmos.drive.controllers;

import java.util.random.RandomGenerator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/dice")
    public int dice() {
        return RandomGenerator.getDefault().nextInt(1, 7);
    }
}