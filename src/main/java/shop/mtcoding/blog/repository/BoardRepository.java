package shop.mtcoding.blog.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;

@Repository
public class BoardRepository {

    @Autowired
    private EntityManager em;

    // select id, title from board_tb
    // resultClass 안붙이고 직접 파싱하려면!!
    // Object[] 로 리턴 됨.
    // object[0] = 1
    // object[1] = 제목1

    @Transactional // 트랜잭션 성공 커밋, 실패하면 롤백
    public void update(UpdateDTO updateDTO, Integer id) {
        Query query = em.createNativeQuery(
                "update board_tb set title = :title, content = :content where id = :id"); // createNativeQuery
        query.setParameter("id", id);
        query.setParameter("title", updateDTO.getTitle());
        query.setParameter("content", updateDTO.getContent());
        query.executeUpdate();

    }

    public int count() {
        Query query = em.createNativeQuery("select count(*) from board_tb");
        // 원래는 object 배열로 리턴받는다, object 배열은 칼럼의 연속이다.
        // 그룹함수를 써서, 하나의 칼럼을 조회 하면, object로 리턴된다.
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public int count2() {
        Query query = em.createNativeQuery(
                "select * from board_tb", Board.class);
        return (Integer) query.getMaxResults();
    }

    @Transactional
    public void save(WriteDTO writeDTO, Integer userId) {
        // EntityManager가 :usernmae으로 바인딩 하게 한다.
        Query query = em.createNativeQuery(
                "insert into board_tb(title, content, user_id, created_at) values(:title, :content, :userId, now())"); // createNativeQuery
        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery(
                "delete from board_tb where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    // 페이징 쿼리
    // select * from board_tb limit 0,3; 처음엔 0이면 1이다. (인덱스 개념)
    // localhost:8080?page=0
    public List<Board> findAll(int page) { // 4개씩 페이징 하겠다.
        final int SIZE = 3; // 항상 변하면 안되는값, 변해도 같아야 하는값 // 상수는 대문자
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit :page, :size", Board.class);
        query.setParameter("page", page * SIZE); // 0 들어오면 1부터 3 / 1들어오면 2부터 4 ... // 근데 오더 바이 해서 숫자는 거꾸로
        query.setParameter("size", SIZE);
        return query.getResultList();
    }

    public List<Board> findByTitleAndContent() {
        Query query = em.createNativeQuery(
                "select * from board_tb", Board.class);
        List<Board> boardList = query.getResultList();
        return boardList;
    }

    public Board findById(Integer id) {
        Query query = em.createNativeQuery("select * from board_tb where id = :id", Board.class);
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

}
