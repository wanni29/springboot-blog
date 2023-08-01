package shop.mtcoding.blog.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 파일명은 숫자로 시작할수 없다
@Controller
public class ErrorController {

    @GetMapping("/40x")
    public String ex40x() {
        return "error/ex40x";
    }

    @GetMapping("/50x")
    public String ex50x() {
        return "error/ex50x";
    }

    @GetMapping("/exlogin")
    public String exLogin() {
        return "error/exLogin";
    }
}
