package shop.mtcoding.blog.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session; // request 는 가방 session 서랍

    @PostMapping("/login")
    public String login(LoginDTO loginDTO) {

        // validation check (유효성 검사) - 부가 로직
        // 프레임 워크에서 걸러내는 기술이 있다.
        // 지금은 적어보자
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }

        // 핵심 기능
        try {
            User user = userRepository.findByUsernameAndPassword(loginDTO);
            session.setAttribute("sessionUser", user);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/exlogin";
        }

    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    // @PostMapping("user/{id}/update")
    // public Stirng update() {

    // }

    @PostMapping("/user/{id}/update") // 수정할꺼야!! 이 값으로 수정해!
    public String update(@PathVariable Integer id, UserUpdateDTO userUpdateDTO) {

        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser"); // 권한 체크를 위한 세션
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 에러코드 401
        }

        // 2. 권한 체크
        User user = userRepository.findById(id);
        if(user.getId() != sessionUser.getId()) {
            return "redirect:/40x"; // 에러코드 403 권한 없음
        }

        // 3. 핵심 로직
        userRepository.update(userUpdateDTO, id);
        
        return "redirect:/";
    }


    // post랑 get이랑 인증이랑 권한 동일한 부분 아닌가 ?  
    @GetMapping("/user/{id}/updateForm")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {

        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser"); // 권한 체크를 위한 세션
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 에러코드 401
        }

        // 2. 권한 체크
        User user = userRepository.findById(id);
        if(user.getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }

        // 3. 핵심 로직
        request.setAttribute("USER", user);

        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logoutForm() {
        session.invalidate(); // 세션 전체 무효화 (내 서랍을 )
        return "redirect:/";
    }

    // 지정된 사이트에서는 유효성 검사가 필요없다. 이미 웹페이지에서 막아뒀으니까.
    // 유효성 검사가 필요한 사람은 어떤사람일까?(공격자) 지정되지 않은 경로로 우회에서 오는 사람이 유효성검사가 필요하다.
    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {

        // validation check (유효성 검사)
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }

        try {
            userRepository.save(joinDTO);
        } catch (Exception e) {
            return "redirect:/50x";
        }

        userRepository.save(joinDTO);
        return "redirect:/";
    }

    // @PostMapping("/join")
    // public void join(String username, String password, String email) {
    // System.out.println(username);
    // System.out.println(password);
    // System.out.println(email);
    // }

    // @PostMapping("/join")
    // public String join(HttpServletRequest request) throws IOException{
    // BufferedReader br = request.getReader();
    // String body = br.readLine();
    // String username = request.getParameter("username");
    // System.out.println("body : " + body);
    // System.out.println("username : " + username);
    // return "redirect:/";
    // }

    // @PostMapping("/join")
    // public String join(HttpServletRequest request) {
    // String username = request.getParameter("username");
    // String password = request.getParameter("password");
    // String email = request.getParameter("email");
    // System.out.println("username : " + username );
    // System.out.println("password : " + password );
    // System.out.println("email : " + email);
    // return "redirect:/";
    // }

}
