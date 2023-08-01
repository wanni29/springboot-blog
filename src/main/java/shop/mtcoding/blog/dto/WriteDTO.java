package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/*
 *  글쓰기 API
 *  1. URL : http://localhost:8080/board/save
 *  2. method : POST
 *  3. 요청 body (request body) : title=값(String)&content=값(String)
 *  4. MiME타입 : x-www-form-urlencoded
 *  5. 응답 : view 를 응답함. 로그인이 진행되면 index 페이지 보여짐
 */

@Getter
@Setter
public class WriteDTO {
    private String title;
    private String content;
}
