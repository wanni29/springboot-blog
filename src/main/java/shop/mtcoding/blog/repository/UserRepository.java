package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;

// BoardController, UserController, UserRepository
// EntityManager, HttpSession
// @Component로 스캔해서 IoC에 다 올려준다 -> META annotation

@Repository
public class UserRepository {

    @Autowired
    private EntityManager em;

    @Transactional
    public void save(JoinDTO joinDTO) {
        // EntityManager가 :usernmae으로 바인딩 하게 한다.
        Query query = em.createNativeQuery(
                "insert into user_tb(username, password, email) values(:username, :password, :email)"); // createNativeQuery
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        query.executeUpdate();
    }
}
