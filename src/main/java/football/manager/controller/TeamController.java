package football.manager.controller;

import football.manager.dao.TeamDAO;
import football.manager.model.Player;
import football.manager.model.Team;
import football.manager.model.request.AddTeamRequest;
import football.manager.model.request.UpdateTeamRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        for (Player player : teamDAO.getPlayers(teamId)) {
            System.out.println(player.getExperience());
        }
        return "team-players";
    }

    @PostMapping("/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam("money") Long money,
                      @RequestParam("percent") double percent,
                      RedirectAttributes redirectAttributes) {
        boolean result = teamDAO.add(name, money, percent);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Team successfully added!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Team could not be added.");
        }
        return "redirect:/team/";
    }

    @GetMapping("/new")
    public String newTeam() {
        return "team";
    }

    @GetMapping("/delete/{Id}")
    public String delete(@PathVariable("Id") Long teamId,  RedirectAttributes redirectAttributes) {
        boolean result = teamDAO.delete(teamId);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "Team successfully deleted!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Team could not be deleted.");
        }
        return "redirect:/team/";
    }

    @GetMapping("/update/{id}")
    public String updatePlayer(@PathVariable Long id, Model model) {
        model.addAttribute("team", teamDAO.getTeamById(id));
        return "update-team";
    }

    @PostMapping("/update")
    public String updatePlayer(@RequestParam("id") Long id,
                               @RequestParam("name") String name,
                               @RequestParam("money") int money,
                               @RequestParam("percent") double percent,
                               RedirectAttributes redirectAttributes) {
        boolean result = teamDAO.update(name, money, percent, id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "Team successfully updated!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Team could not be updated.");
        }
        return "redirect:/team/";
    }


    @PostMapping("/add/postman")
    public String addTeamPostman(@RequestBody AddTeamRequest request) {
        teamDAO.add(request.getName(), request.getMoney(), request.getPercent());
        return "redirect:/team/";
    }

    @PostMapping("/update/postman")
    public String updateTeamPostman(@RequestBody UpdateTeamRequest request) {
        teamDAO.update(request.getName(), request.getMoney(), request.getPercent(), request.getId());
        return "redirect:/team/";
    }
}

