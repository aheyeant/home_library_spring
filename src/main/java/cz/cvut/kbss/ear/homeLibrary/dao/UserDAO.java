package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
public class UserDAO extends BaseDAO<User>{

    public UserDAO() {
        super(User.class);
    }

    public User findByEmail(String email){
        Objects.requireNonNull(email);
        try {
            return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}


