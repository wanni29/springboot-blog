package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "user_tb") //
@Entity // ddl-auto 가 create
public class User {

    @Id // 프라이머리 키 맥이고
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    // 테이블을 만들떼에 번호증가 전략이라는건 데이터베이스마다 다 있다.
    // 데이터 베이스마다 번호증가 전략이 다 다르다.
    // 위의 값이 1이네 다음값은 ++ 해서 2가 들어간다.

    // 또다른 방법은 함수를 만들어 두어서 데이터가 들어올때마다 함수에서 번호를 뽑아서준다. : 시퀀스 전략
    // 페이스북에서는 long으로 만든다 그만큼 사람수가 많으니까
    private Integer id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String email;

}
