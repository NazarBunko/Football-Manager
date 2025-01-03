package football.manager.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class MainController {
    @GetMapping("/")
    public String redirectToMainPage() {
        return "/team/index";
    }
}
