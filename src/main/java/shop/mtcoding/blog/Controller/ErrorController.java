package shop.mtcoding.blog.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 파일명은 숫자로 시작할수 없다
@Controller
public class ErrorController {

    @GetMapping("/40x")
    public String ex40() {
        return "error/ex40";
    }

}
