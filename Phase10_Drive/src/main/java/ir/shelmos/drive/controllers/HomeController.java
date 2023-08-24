package ir.shelmos.drive.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;

@RestController
public class HomeController {

    @GetMapping("/dice")
    public int dice() {
        return new Random().nextInt(1, 7);
    }
}
