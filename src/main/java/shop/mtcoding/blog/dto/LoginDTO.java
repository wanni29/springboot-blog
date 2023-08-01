package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/*
 *  회원가입 API
 *  1. URL : http://localhost:8080/join
 *  2. method : POST
 *  3. 요청 body (request body) : username=값(String)&password=값(String)&email=값(String)
 *  4. MiME타입 : x-www-form-urlencoded
 *  5. 응답 : view 를 응답함. 로그인이 진행되면 index 페이지 보여짐
 */

@Getter
@Setter
public class LoginDTO {

    private String username;
    private String password;

}
