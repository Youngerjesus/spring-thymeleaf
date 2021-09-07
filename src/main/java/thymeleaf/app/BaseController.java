package thymeleaf.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/base")
public class BaseController {

    @GetMapping
    public String textBasic(Model model) {
        model.addAttribute("data", "hello spring");
        return "index";
    }
}
