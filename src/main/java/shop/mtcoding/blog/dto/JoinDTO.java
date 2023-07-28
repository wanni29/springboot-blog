package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

// 내가 직접 파싱한다고 칠때 이 값에 넣기위해선 뭐가 필요할까? 
// getter ,setter, new 
// get은 바디가 없어!

// 010 2222 7777 -> 숫자로 관리하면 0 이 날라간다. 10 2222 7777로 인식
// 수가 크기때문에 int에 담길수가 없다.
// 그렇기 때문에 String 으로 관리한다.

/*
 *  회원가입 API
 *  1. URL : http://localhost:8080/join
 *  2. method : POST
 *  3. 요청 body (request body) : username=값(String)&password=값(String)&email=값(String)
 *  4. MiME타입 : x-www-form-urlencoded
 *  5. 응답 : view 를 응답함
 */

@Getter
@Setter
public class JoinDTO {
    private String username;
    private String password;
    private String email;
}
