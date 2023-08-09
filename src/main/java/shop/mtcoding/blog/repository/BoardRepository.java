package shop.mtcoding.blog.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.BoardDetailDTO;
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

    public List<BoardDetailDTO> findByIdJoinReply(Integer boardId, Integer sessionUserId) {
        String sql = "select ";
        sql += "b.id board_id, ";
        sql += "b.content board_content, ";
        sql += "b.title board_title, ";
        sql += "b.user_id board_user_id, ";
        sql += "r.id reply_id, ";
        sql += "r.comment reply_comment, ";
        sql += "r.user_id reply_user_id, ";
        sql += "ru.username reply_user_username, ";
        if (sessionUserId == null) {
            sql += "false reply_owner ";
        } else {
            sql += "case when r.user_id = :sessionUserId then true else false end reply_owner ";
            // sessionUserId 가 있을경우에
            // reply_user_id 와 sessionUserId 가 같다면 true를 갖게되고
            // 그게 아니라면 false값을 가지게 된다.
        }

        sql += "from board_tb b left outer join reply_tb r ";
        sql += "on b.id = r.board_id ";
        sql += "left outer join user_tb ru ";
        sql += "on r.user_id = ru.id ";
        sql += "where b.id = :boardId ";
        sql += "order by r.id desc";
        Query query = em.createNativeQuery(sql);
        query.setParameter("boardId", boardId);

        if (sessionUserId != null) {
            query.setParameter("sessionUserId", sessionUserId);
        }

        JpaResultMapper mapper = new JpaResultMapper();
        List<BoardDetailDTO> dtos = mapper.list(query, BoardDetailDTO.class);

        return dtos;

    }

    public Board findById(Integer id) {
        Query query = em.createNativeQuery("select * from board_tb where id = :id", Board.class);
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

}
