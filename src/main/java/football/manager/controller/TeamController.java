package football.manager.controller;

import football.manager.dao.TeamDAO;
import football.manager.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/team")
@Validated
public class TeamController {

    @Autowired
    private TeamDAO teamDAO;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("teams", teamDAO.index());
        return "index";
    }

    @GetMapping("/{teamId}")
    public String showPlayers(@PathVariable("teamId") Long teamId, Model model) {
        model.addAttribute("teams", teamDAO.index());
        model.addAttribute("team", teamDAO.getTeamById(teamId));
        model.addAttribute("players", teamDAO.getPlayers(teamId));
        return "team-players";
    }

    @PostMapping("/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam("money") Long money,
                      @RequestParam("percent") double percent) {
        teamDAO.add(name, money, percent);
        return "redirect:/team/";
    }

    @GetMapping("/new")
    public String newTeam() {
        return "team";
    }

    @GetMapping("/delete/{Id}")
    public String delete(@PathVariable("Id") Long teamId) {
        teamDAO.delete(teamId);
        return "redirect:/team/";
    }
}

