package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.User;

// BoardController, UserController, UserRepository
// EntityManager, HttpSession
// @Component로 스캔해서 IoC에 다 올려준다 -> META annotation

@Repository
public class UserRepository {

    @Autowired
    private EntityManager em;

    @Transactional
    public void update(UserUpdateDTO userUpdateDTO, Integer id) {
        Query query = em.createNativeQuery(
                "update user_tb set password = :password where id = :id");
        query.setParameter("id", id);
        query.setParameter("password", userUpdateDTO.getPassword());
        query.executeUpdate();
        System.out.println("데이터가 정확히 들어갔다면 이 문장이 출력됩니다.");
        System.out.println("h2-console로 넘어가서 데이터 값의 변화를 확인하세요.");

    }

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

    // 모델로 받을수 없는것은 DTO 로 받아야 한다.
    // 아이디를 틀리면 리턴이 안된다. 조회가 안되니 no entity found 라고 뜨는거다

    public User findById(Integer id) {
        Query query = em.createNativeQuery("select * from user_tb where id = :id", User.class);
        query.setParameter("id", id);
        User user = (User) query.getSingleResult();
        return user;
    }

    public User findByUsernameAndPassword(LoginDTO loginDTO) {
        Query query = em.createNativeQuery(
                "select * from user_tb where username = :username and password = :password", User.class);
        query.setParameter("username", loginDTO.getUsername());
        query.setParameter("password", loginDTO.getPassword());
        return (User) query.getSingleResult();
    }

}
