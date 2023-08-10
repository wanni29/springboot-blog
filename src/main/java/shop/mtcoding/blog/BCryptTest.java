package shop.mtcoding.blog;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {

    public static void main(String[] args) {
        String encPassword1 = BCrypt.hashpw("1234", BCrypt.gensalt());
        System.out.println("encPassword : " + encPassword1);

        String encPassword2 = BCrypt.hashpw("1234", BCrypt.gensalt());
        System.out.println("encPassword : " + encPassword2);

        boolean isValid = BCrypt.checkpw("1234", encPassword1);
        System.out.println(isValid);
    }

}
