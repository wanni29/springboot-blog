package shop.mtcoding.blog.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

// UserController, BoardController, ReplyController, ErroController // 내가 띄운것
// UserRePository, BoardRepository, ReplyRepository // 내가 띄운것
// EntityManager, HttpSession // Spring이 띄운것 
@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyRepository replyRepository;

    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO replyWriteDTO, HttpServletRequest request) {
        // comment 유효성 검사
        // board 아이디가 널인지 공백인지 comment 아이디가 널인지 공백인지
        if (replyWriteDTO.getBoardId() == null || replyWriteDTO.getBoardId() == 0) {
            return "redirect:/40x";
        }

        if (replyWriteDTO.getComment() == null || replyWriteDTO.getComment().isEmpty()) {
            return "redirect:/40x";
        }

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 댓글쓰기
        // 유저 아이들 넘겨주고 ????
        replyRepository.save(replyWriteDTO, sessionUser.getId());
        return "redirect:/board/" + replyWriteDTO.getBoardId();

    }

    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable Integer id, Integer boardId) {

        // 유효성 검사
        if (boardId == null) {
            return "redirect:/40x";
        }

        // 보안이라는것은 나라의 수준이다.
        // 인증 체크 -> 포스트맨으로 공격할수있기때문에 세션체크안할수있음!
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 권한 체크
        Reply reply = replyRepository.findById(id);
        if (reply.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }

        // 핵심 로직
        replyRepository.delete(id);
        return "redirect:/board/" + boardId;
    }

}
