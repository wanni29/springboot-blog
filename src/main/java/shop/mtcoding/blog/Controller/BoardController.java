package shop.mtcoding.blog.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    @GetMapping({ "/", "/board" })
    public String index() {
        return "index";
    }

    @GetMapping("/board/saveForm") // 사용자가 요청하는 주소
    public String saveForm() {
        return "board/saveForm"; // 뷰를 찾는거
    }

    @GetMapping("/board/1")
    public String detailForm() {
        return "board/detail";
    }

}
