package football.manager.controller;

import football.manager.dao.PlayerDAO;
import football.manager.dao.TeamDAO;
import football.manager.model.Player;
import football.manager.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/player")
@Validated
public class PlayerController {

    @Autowired
    private PlayerDAO playerDAO;

    @Autowired
    private TeamDAO teamDAO;

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        playerDAO.delete(id);
        return "redirect:/team/";
    }

    @GetMapping("/release/{id}")
    public String release(@PathVariable Long id) {
        playerDAO.kickPlayer(id);
        return "redirect:/team/";
    }

    @GetMapping("/sell/{id}")
    public String sellPage(@PathVariable Long id, Model model) {
        Player player = playerDAO.getPlayerById(id);
        List<Team> teams = teamDAO.index();
        model.addAttribute("player", player);
        model.addAttribute("teams", teams);
        return "sell-player";
    }

    @PostMapping("/sell")
    public String sellPlayer(@RequestParam("playerId") Long playerId,
                             @RequestParam("newTeamId") Long newTeamId,
                             @RequestParam("price") Long price,
                             RedirectAttributes redirectAttributes) {
        boolean result = playerDAO.sellPlayer(playerId, newTeamId, price);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully sold!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be sold.");
        }

        return "redirect:/team/";
    }

    @PostMapping("/transfer")
    public String transferPlayer(@RequestParam("playerId") Long playerId,
                                 @RequestParam("teamId") Long teamId) {
        playerDAO.addToTeam(playerId, teamId);
        return "redirect:/team/";
    }

    @GetMapping("/free-agents")
    public String freeAgents(Model model) {
        List<Player> players = teamDAO.getPlayers(null);
        List<Team> teams = teamDAO.index();
        model.addAttribute("teams", teams);
        model.addAttribute("players", players);
        return "free-agents";
    }

    @PostMapping("/add")
    public String addPlayer(@RequestParam("name") String name,
                            @RequestParam("age") int age,
                            @RequestParam("position") String position,
                            @RequestParam("experience") int month_experience,
                            @RequestParam("team") Long teamId) {
        System.out.println("Received Data: " + name + ", " + age + ", " + position + ", " + month_experience + ", " + teamId);

        playerDAO.add(name, teamId, age, position, month_experience);
        return "redirect:/team/";
    }


    @GetMapping("/new")
    public String newPlayer(Model model) {
        List<Team> teams = teamDAO.index();
        model.addAttribute("teams", teams);
        return "player";
    }
}
