/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson05
 * @date: 3/4/2025
 * @time: 07:46 PM
 * @package: com.example.Lesson05.controller
 */

package com.example.Lesson05.controller;

import com.example.Lesson05.entity.Info;
import lombok.Generated;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }

   @GetMapping("/profile")
   public String profile(Model model) {
        List<Info> profile = new ArrayList<>();
        profile.add(new Info("Dat Le", "xeng", "datle24034@gmail.com", "https://www.facebook.com/atle.631578/"));
        profile.add(new Info("Uyen Le", "gacon", "unin310102@gmail.com", "https://www.facebook.com/iuck.631578/"));
        model.addAttribute("DatProfile", profile);
        return "profile";
   }

   @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "DatLe::Home");
        return "home";
   }

   @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "DatLe::About");
        return "about";
   }

   @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "DatLe::Hãy liên hệ với chúng tôi");
        return "contact";
   }
}
