package football.manager.controllers;

import football.manager.models.Team;
import football.manager.repositories.TeamRepository;
import football.manager.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/team")
@Validated
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("teams", teamRepository.index());
        return "index";
    }

    @GetMapping("/{teamId}")
    public String showPlayers(@PathVariable("teamId") Long teamId, Model model) {
        model.addAttribute("team", teamRepository.getTeamById(teamId));
        model.addAttribute("teams", teamRepository.index());
        model.addAttribute("players", teamRepository.getPlayers(teamId));
        return "team-players";
    }

    @PostMapping("/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam("money") Long money,
                      @RequestParam("percent") double percent,
                      @RequestParam("photo") MultipartFile photo,
                      RedirectAttributes redirectAttributes) {

        try {
            byte[] photoBytes = null;
            if (photo != null && !photo.isEmpty()) {
                photoBytes = photo.getBytes();
            }
            boolean result = teamRepository.add(name, money, percent, photoBytes);

            if (result) {
                redirectAttributes.addFlashAttribute("message", "Team successfully added!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Team could not be added.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error processing photo: " + e.getMessage());
        }

        return "redirect:/team/";
    }

    @GetMapping("/new")
    public String newTeam() {
        return "team";
    }

    @GetMapping("/delete/{Id}")
    public String delete(@PathVariable("Id") Long teamId, RedirectAttributes redirectAttributes) {
        boolean result = teamRepository.delete(teamId);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "Team successfully deleted!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Team could not be deleted.");
        }
        return "redirect:/team/";
    }

    @GetMapping("/update/{id}")
    public String updateTeam(@PathVariable Long id, Model model) {
        Team team = teamRepository.getTeamById(id);
        model.addAttribute("photoBase64", team.getPhotoBase64());
        model.addAttribute("team", team);
        return "update-team";
    }


    @PostMapping("/update")
    public String updateTeam(@RequestParam("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("money") int money,
                             @RequestParam("percent") double percent,
                             @RequestParam(value = "photo", required = false) MultipartFile photo,
                             RedirectAttributes redirectAttributes) {
        try {
            byte[] photoBytes = null;
            if (photo != null && !photo.isEmpty()) {
                photoBytes = photo.getBytes();
            }
            boolean result = teamRepository.update(name, money, percent, id, photoBytes);

            if (result) {
                redirectAttributes.addFlashAttribute("message", "Team successfully updated!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Team could not be updated.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error processing photo: " + e.getMessage());
        }

        return "redirect:/team/";
    }
}
