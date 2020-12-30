package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class LibraryDAO extends BaseDAO<Library> {
    public LibraryDAO() {
        super(Library.class);
    }

    // visible == true
    public Library findByIdAsAnonymous(Integer id){
        try {
            return em.createNamedQuery("Library.findByIdAsAnonymous", Library.class).setParameter("id", id).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    // visible == (true or false)
    public Library findByIdAsAdmin(Integer id){
        try {
            return em.createNamedQuery("Library.findByIdAsAdmin", Library.class).setParameter("id", id).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    // visible == true
    public List<Library> findAllAsAnonymous(){
        try {
            return em.createNamedQuery("Library.findAllAsAnonymous", Library.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public Library findByUserId(Integer id) {
        Objects.requireNonNull(id);
        try {
            return em.createNamedQuery("Library.findByUserId", Library.class).setParameter("id", id).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
