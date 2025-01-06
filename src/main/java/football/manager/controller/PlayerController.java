package football.manager.controller;

import football.manager.dao.PlayerDAO;
import football.manager.dao.TeamDAO;
import football.manager.model.Player;
import football.manager.model.Team;
import football.manager.model.request.SellPlayerRequest;
import football.manager.model.request.TransferRequest;
import football.manager.model.request.UpdatePlayerRequest;
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
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean result = playerDAO.delete(id);


        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully deleted!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be deleted.");
        }

        return "redirect:/team/";
    }

    @GetMapping("/release/{id}")
    public String release(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean result = playerDAO.kickPlayer(id);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully released!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be released.");
        }

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
                             RedirectAttributes redirectAttributes) {
        Player player = playerDAO.getPlayerById(playerId);
        Long price = (long) ((player.getExperience() * 100000) / player.getAge());
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
                                 @RequestParam("teamId") Long teamId, RedirectAttributes redirectAttributes) {
        boolean result = playerDAO.addToTeam(playerId, teamId);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully transferred!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be transferred.");
        }

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
                            @RequestParam("experience") int experience,
                            @RequestParam("team") Long teamId,
                            RedirectAttributes redirectAttributes) {
        boolean result = playerDAO.add(name, teamId, age, position, experience);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully added!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be added.");
        }

        return "redirect:/team/";
    }

    @GetMapping("/new")
    public String newPlayer(Model model) {
        List<Team> teams = teamDAO.index();
        model.addAttribute("teams", teams);
        return "player";
    }

    @GetMapping("/update/{id}")
    public String updatePlayer(@PathVariable Long id, Model model) {
        Player player = playerDAO.getPlayerById(id);
        List<Team> teams = teamDAO.index();
        model.addAttribute("player", player);
        model.addAttribute("teams", teams);
        return "update-player";
    }

    @PostMapping("/update")
    public String updatePlayer(@RequestParam("id") Long id,
                               @RequestParam("name") String name,
                               @RequestParam("age") int age,
                               @RequestParam("position") String position,
                               @RequestParam("experience") int experience,
                               @RequestParam("team") Long teamId,
                               Model model) {

        boolean result = playerDAO.update(name, age, position, experience, teamId, id);

        if (result) {
            model.addAttribute("message", "Player successfully updated!");
        } else {
            model.addAttribute("message", "Player could not be updated.");
        }

        return "redirect:/team/";
    }



    @PostMapping("/add/postman")
    public String addPlayerPostman(@RequestBody Player player) {
        System.out.println(player);
        playerDAO.add(player.getName(), player.getTeam_id(), player.getAge(), player.getPosition(), player.getExperience());
        return "redirect:/team/";
    }

    @PostMapping("/sell/postman")
    public String sellPlayerPostman(@RequestBody SellPlayerRequest request) {
        playerDAO.sellPlayer(request.getPlayerId(), request.getNewTeamId(), request.getPrice());

        return "redirect:/team/";
    }

    @PostMapping("/transfer/postman")
    public String transferPlayerPostman(@RequestBody TransferRequest request) {
        playerDAO.addToTeam(request.getPlayerId(), request.getTeamId());
        return "redirect:/team/";
    }

    @PostMapping("/update/postman")
    public String updatePlayerPostman(@RequestBody UpdatePlayerRequest request) {
        playerDAO.update(request.getName(), request.getAge(), request.getPosition(), request.getExperience(), request.getTeamId(), request.getId());
        return "redirect:/team/";
    }

}
