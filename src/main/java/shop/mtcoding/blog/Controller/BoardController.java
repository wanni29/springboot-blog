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
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @ResponseBody
    @GetMapping("/test/board/2")
    public List<Reply> test2() {
        List<Reply> replys = replyRepository.findByBoardId(1);
        return replys;
    }

    @ResponseBody
    @GetMapping("/test/board/1")
    public Board test() {
        Board board = boardRepository.findById(1);
        return board;
    }

    @PostMapping("/board/{id}/update") // 수정할꺼야 ! 이 값으로 수정해!
    public String update(@PathVariable Integer id, UpdateDTO updateDTO) {

        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 권한 체크
        Board board = boardRepository.findById(id);
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }

        // 3. 핵심 로직
        // update board_tb set title = :title, content = :content where id = :id
        boardRepository.update(updateDTO, id);

        return "redirect:/board/" + id;

    }

    @GetMapping("/board/{id}/updateForm") // 수정하는 창을 보여줘
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        // 1. 인증 검사 (아이디에 대한 세션값이 필요한가 ? post맨으로 우회접근을 하면 필요하지 않을까 ? )
        User sessionUser = (User) session.getAttribute("sessionUser"); // 권한체크를 위한 세션 접근
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401
        }

        // 2. 권한 체크 (로그인한 아이디의 세션값과 게시글의 적힌 세션의 값이 동일한지 파악해야 하는가 ? )
        Board board = boardRepository.findById(id);
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }

        // 3. 핵심 로직
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("board/{id}/delete")
    public String delete(@PathVariable Integer id) {
        // 1. PathVariable 값 받기

        // 2. 인증 검사 (로그인 페이지 보내기)
        // session에 접근 해서 sessionUser 키값을 가져오세요
        // null 이면 로그인 페이지로 보내고
        // null 아니면 3번을 실행하세요.
        // 우회 접근을 했을 시
        User sessionUser = (User) session.getAttribute("sessionUser"); // 권한체크를 위한 세션 접근
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401
        }

        // 3. 권한 검사
        Board board = boardRepository.findById(id);
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x"; // 403 권한 없음
        }

        // 4. 모델에 접근해서 삭제
        // boardRepository.deleteById(id); 호출하세요 ->deleteById(id);메소드를 만들때 리턴을 받지 마세요
        // delete from board_tb where id = :id
        boardRepository.deleteById(id);

        return "redirect:/";
    }

    // localhost:8080 <= 널값 (왜 널값인데 ? Integer 클래스는 null 값이 허용되니깐)
    // 그래서 RequestParam(defaultValue = "0") 이거를 넣어서 기본값으로 만들어서
    // 널값이 안들어 가게 만든다.
    @GetMapping({ "/", "/board" })
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        // 매개변수가 쿼리스트링이 되는구나..

        // 1. 유효성 검사 x
        // 2. 인증 검사 x

        // 값 검증이라는것을 해야한다. 바로 뷰에 뿌리면 안된다.
        // 뭘하든 request에 담는거다.
        List<Board> boardList = boardRepository.findAll(page); // page = 1
        int totalcount = boardRepository.count(); // totalCount = 5
        int totalPage = totalcount / 3; // totalPage = 1

        if (totalcount % 3 > 0) { //
            totalPage = totalPage + 1; // totalPage = 2
        }

        boolean last = totalPage - 1 == page;

        // System.out.println("테스트 : " + boardList.size());
        // System.out.println("테스트 : " + boardList.get(0).getTitle());

        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalcount", totalcount);
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
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser"); // 권한체크를 위한 세션 접근
        List<BoardDetailDTO> dtos = null;
        if (sessionUser == null) {
            dtos = boardRepository.findByIdJoinReply(id, null);
        } else {
            dtos = boardRepository.findByIdJoinReply(id, sessionUser.getId());
        }

        boolean pageOwner = false;
        if (sessionUser != null) {
            pageOwner = sessionUser.getId() == dtos.get(0).getBoardUserId();
        }

        request.setAttribute("dtos", dtos);
        request.setAttribute("pageOwner", pageOwner); // false => 내가 적은글이 아니야 !
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
