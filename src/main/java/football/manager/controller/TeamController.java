package football.manager.controller;

import football.manager.dao.TeamDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/team")
@Validated
public class TeamController {

    @Autowired
    private TeamDAO teamDAO;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("teams", teamDAO.index());
        return "index";
    }

    @GetMapping("/{teamId}/players")
    public String showPlayers(@PathVariable("teamId") Long teamId, Model model) {
        model.addAttribute("team", teamDAO.show(teamId));
        model.addAttribute("players", teamDAO.getPlayers(teamId));
        return "team-players";
    }
}

