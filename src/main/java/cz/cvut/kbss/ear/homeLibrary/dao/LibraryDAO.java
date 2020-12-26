package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class LibraryDAO extends BaseDAO<Library> {
    public LibraryDAO() {
        super(Library.class);
    }

    @Override
    public List<Library> findAll() {
        return em.createQuery("SELECT l FROM Library l WHERE l.visible", Library.class).getResultList();
    }
}
