package football.manager.controller;

import football.manager.dao.TeamDAO;
import football.manager.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@Validated
public class TeamController {
    @Autowired
    private TeamDAO teamDAO;
}
