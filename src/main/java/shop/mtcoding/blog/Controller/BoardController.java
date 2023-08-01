package shop.mtcoding.blog.Controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    // localhost:8080 <= 널값
    // 그래서 RequestParam(defaultValue = "0") 이거를 넣어서 기본값으로 만들어서
    // 널값이 안들어 가게 만든다.
    @GetMapping({ "/", "/board" })
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {

        // 1. 유효성 검사 x
        // 2. 인증 검사 x

        // 값 검증이라는것을 해야한다. 바로 뷰에 뿌리면 안된다.
        // 뭘하든 request에 담는거다.
        List<Board> boardList = boardRepository.findAll(page);
        System.out.println("테스트 : " + boardList.size());
        System.out.println("테스트 : " + boardList.get(0).getTitle());

        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", false);
        // 이거를 트루로 만들수잇음 된다.

        return "index";

    }

    // '글쓰기 페이지 줘'
    @GetMapping("/board/saveForm") // 사용자가 요청하는 주소
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        } else {
            return "board/saveForm";
        }

    }

    // localhost:8080/board/1
    // localhost:8080/board/50
    @GetMapping("/board/{id}")
    public String detailForm(@PathVariable Integer id) {
        return "board/detail";
    }

    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {
        // 유효성 검사와 세션 검사 - 부가로직
        // 유효성 검사
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {
            return "redirect:/40x";
        }
        if (writeDTO.getContent() == null || writeDTO.getContent().isEmpty()) {
            return "redirect:/40x";
        }

        // 세션 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        } else {
            boardRepository.save(writeDTO, sessionUser.getId());
            return "redirect:/";
        }

    }

}
