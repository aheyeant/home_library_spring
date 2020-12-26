package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Tag;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class BookDAO extends BaseDAO<Book> {
    public BookDAO() {
        super(Book.class);
    }

    @Override
    public List<Book> findAll() {
        try {
            return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Book> findAll(Tag tag) {
        try {
            return em.createQuery("SELECT b FROM Book b", Book.class).getResultList(); // vymyslet jak filtrovat
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Book> findAllAvailable() {
        try {
            return em.createQuery("SELECT b FROM Book b WHERE NOT b.borrowed", Book.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }

    }

    public List<Book> findByTag(Tag tag){
        try {
            return em.createNamedQuery("Book.findByTag", Book.class).setParameter(("tag"), tag).getResultList();
        }
        catch (NoResultException e) {
                return null;
        }
    }

}
