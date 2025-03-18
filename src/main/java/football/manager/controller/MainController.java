package football.manager.controller;

import football.manager.dao.TeamDAO;
import football.manager.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private TeamDAO teamDAO;

    @GetMapping("/")
    public String redirectToMainPage(Model model) {
        model.addAttribute("teams", teamDAO.index());
        List<Team> teams = teamDAO.index();
        teamDAO.setPlayersForAllTeams(teams);
        System.out.println(teams.get(0).getPlayers());
        System.out.println(teams.get(0).getName());
        System.out.println(teams.get(0).getId());
        return "main";
    }
}
