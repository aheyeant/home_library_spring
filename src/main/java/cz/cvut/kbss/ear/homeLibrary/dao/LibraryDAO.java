package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class LibraryDAO extends BaseDAO<Library> {
    public LibraryDAO() {
        super(Library.class);
    }

    public List<Library> findAllVisible() {
        try {
            return em.createQuery("SELECT l FROM Library l WHERE l.visible", Library.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public Optional<Library> findByUserId(Integer id) {
        Objects.requireNonNull(id);
        try {
            return Optional.of(em.createNamedQuery("Library.findByUserId", Library.class).setParameter("id", id).getSingleResult());
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
