package football.manager.controllers;

import football.manager.repositories.PlayerRepository;
import football.manager.repositories.TeamRepository;
import football.manager.models.Player;
import football.manager.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/player")
@Validated
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Player> players = playerRepository.index();
        model.addAttribute("players", players);
        return "players";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean result = playerRepository.delete(id);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully deleted!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be deleted.");
        }

        return "redirect:/team/";
    }

    @GetMapping("/release/{id}")
    public String release(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean result = playerRepository.kickPlayer(id);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully released!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be released.");
        }

        return "redirect:/team/";
    }

    @GetMapping("/sell/{id}")
    public String sellPage(@PathVariable Long id, Model model) {
        Player player = playerRepository.getPlayerById(id);
        if (player == null) {
            // Можна обробити випадок відсутності гравця
            return "redirect:/team/";
        }
        List<Team> teams = teamRepository.index();
        model.addAttribute("player", player);
        model.addAttribute("teams", teams);
        return "sell-player";
    }

    @PostMapping("/sell")
    public String sellPlayer(@RequestParam("playerId") Long playerId,
                             @RequestParam("newTeamId") Long newTeamId,
                             RedirectAttributes redirectAttributes) {
        Player player = playerRepository.getPlayerById(playerId);
        if (player == null) {
            redirectAttributes.addFlashAttribute("message", "Player not found.");
            return "redirect:/team/";
        }

        Long price = (long) ((player.getExperience() * 100000) / player.getAge());
        boolean result = playerRepository.sellPlayer(playerId, newTeamId, price);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully sold!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be sold.");
        }

        return "redirect:/team/";
    }

    @PostMapping("/transfer")
    public String transferPlayer(@RequestParam("playerId") Long playerId,
                                 @RequestParam("teamId") Long teamId,
                                 RedirectAttributes redirectAttributes) {
        boolean result = playerRepository.addToTeam(playerId, teamId);

        if (result) {
            redirectAttributes.addFlashAttribute("message", "Player successfully transferred!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Player could not be transferred.");
        }

        return "redirect:/player/free-agents";
    }

    @GetMapping("/free-agents")
    public String freeAgents(Model model) {
        List<Player> players = teamRepository.getPlayers(null);
        List<Team> teams = teamRepository.index();
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
                            @RequestParam("photo") MultipartFile photo,
                            RedirectAttributes redirectAttributes) {

        try {
            byte[] photoBytes = null;
            if (photo != null && !photo.isEmpty()) {
                photoBytes = photo.getBytes();
            }
            boolean result = playerRepository.add(name, teamId, age, position, experience, photoBytes);

            if (result) {
                redirectAttributes.addFlashAttribute("message", "Player successfully added!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Player could not be added.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error processing photo: " + e.getMessage());
        }

        return "redirect:/player/";
    }

    @GetMapping("/new")
    public String newPlayer(Model model) {
        List<Team> teams = teamRepository.index();
        model.addAttribute("teams", teams);
        return "player";
    }

    @GetMapping("/update/{id}")
    public String updatePlayer(@PathVariable Long id, Model model) {
        Player player = playerRepository.getPlayerById(id);
        if (player == null) {
            // Можна обробити відсутність гравця
            return "redirect:/player/";
        }
        List<Team> teams = teamRepository.index();
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
                               @RequestParam(value = "photo", required = false) MultipartFile photo,
                               RedirectAttributes redirectAttributes) {

        try {
            byte[] photoBytes = null;
            if (photo != null && !photo.isEmpty()) {
                photoBytes = photo.getBytes();
            }
            boolean result = playerRepository.update(name, age, position, experience, teamId, id, photoBytes);

            if (result) {
                redirectAttributes.addFlashAttribute("message", "Player successfully updated!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Player could not be updated.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error processing photo: " + e.getMessage());
        }

        return "redirect:/player/";
    }

    @GetMapping("/info/{id}")
    public String info(@PathVariable Long id, Model model) {
        Player player = playerRepository.getPlayerById(id);
        if (player == null) {
            return "fragments/not-found :: content";
        }
        if(player.getTeam_id() == null){
            model.addAttribute("teamPhoto", null);
        } else {
            Team team = (player.getTeam_id() != null) ? teamRepository.getTeamById(player.getTeam_id()) : null;
            model.addAttribute("teamPhoto", team.getPhotoBase64());
        }
        model.addAttribute("player", player);
        return "player-info-fragment :: content";
    }
}
