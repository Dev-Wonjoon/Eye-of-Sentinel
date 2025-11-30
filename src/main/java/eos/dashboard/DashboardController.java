package eos.dashboard;

import eos.log.LogResponse;
import eos.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final LogService logService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("indices", logService.getIndexList());

        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false, defaultValue = "docker-logs*") String index,
            Model model
    ) {
        LogResponse response = logService.getDynamicDashboardData(index);

        model.addAttribute("columns", response.getColumns());
        model.addAttribute("logs", response.getLogs());
        model.addAttribute("currentIndex", index);
        model.addAttribute("indices", logService.getIndexList());


        return "dashboard";
    }
}
