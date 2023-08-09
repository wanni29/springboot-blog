package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Reply;

@Repository
public class ReplyRepository {

    @Autowired
    private EntityManager em;

    public List<Reply> findByBoardId(Integer boardId) {
        Query query = em.createNativeQuery("select * from reply_tb where board_id = :boardId", Reply.class);
        query.setParameter("boardId", boardId);
        return query.getResultList();
    };

    @Transactional
    public void save(ReplyWriteDTO replyWriteDTO, Integer userId) {
        Query query = em.createNativeQuery(
                "insert into reply_tb(board_id, comment, user_id) values(:boardId, :comment, :userId)");
        query.setParameter("boardId", replyWriteDTO.getBoardId());
        query.setParameter("comment", replyWriteDTO.getComment());
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Transactional
    public void delete(Integer id) {
        Query query = em.createNativeQuery("delete from reply_tb where id = :Id");
        query.setParameter("Id", id);
        query.executeUpdate();
    }

    public Reply findById(int id) {
        Query query = em.createNativeQuery("select * from reply_tb where id = :Id", Reply.class);
        query.setParameter("Id", id);
        return (Reply) query.getSingleResult();
    }

}
