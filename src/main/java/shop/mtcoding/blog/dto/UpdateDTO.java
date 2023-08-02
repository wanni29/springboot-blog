package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/*
 *  회원가입 API
 *  1. URL : http://localhost:8080/board/{id}/update
 *  2. method : POST
 *  3. 요청 body (request body) : title=값(String)&content=값(String)
 *  4. MiME타입 : x-www-form-urlencoded
 *  5. 응답 : view 를 응답함. 로그인이 진행되면 detail 페이지 보여짐 (해당 번호를 들고 들어가야한다.)
 */

@Getter
@Setter
public class UpdateDTO {

    private String title;
    private String content;

}
