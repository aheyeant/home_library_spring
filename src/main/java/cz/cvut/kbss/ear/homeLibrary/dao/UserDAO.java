package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDAO extends BaseDAO<User>{

    public UserDAO() {
        super(User.class);
    }

    public User findByEmail(String email){
        try {
            return em.createQuery("SELECT l FROM Library l WHERE ", User.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}


