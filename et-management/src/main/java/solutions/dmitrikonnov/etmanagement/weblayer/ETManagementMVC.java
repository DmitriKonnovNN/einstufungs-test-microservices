package solutions.dmitrikonnov.etmanagement.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller ("api/v2.0.0/management")
@AllArgsConstructor
public class ETManagementMVC {

    @GetMapping("/")
    public String greetUser(){
        return "index";
    }

    @GetMapping("/login")
    public String getLoginView (){
        return "login";
    }

    @GetMapping("/verwaltung")
    public String greetManagement(){
        return "/management";
    }
}
