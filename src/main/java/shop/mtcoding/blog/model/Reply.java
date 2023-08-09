package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

// User(1) - Reply(n)
// Board(1) - Reply(n)
@Getter
@Setter
@Table(name = "reply_tb")
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100) // 널을 허용하나요 ? 아니요
    private String comment; // 댓글 내용

    @ManyToOne
    private User user; // Fk user_id

    @ManyToOne
    private Board board; // Fk board_id

}
