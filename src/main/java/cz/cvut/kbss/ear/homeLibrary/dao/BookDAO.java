package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Tag;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public List<Book> findAllAvailable() {
        try {
            return em.createQuery("SELECT b FROM Book b WHERE b.available", Book.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Book> findAllNotAvailable() {
        try {
            return em.createQuery("SELECT b FROM Book b WHERE NOT b.available", Book.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Book> getAllBooksFromLibrary(Integer libraryId) {
        Objects.requireNonNull(libraryId);
        try {
            return em.createNamedQuery("Book.getAllBooksFromLibrary", Book.class).setParameter("id", libraryId).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    //todo
    public List<Book> getAvailableBooksFromLibrary(Integer libraryId) {
        Objects.requireNonNull(libraryId);
        try {
            //SELECT b FROM Book b WHERE b.library.id = :id AND b.available
            return em.createQuery("SELECT b FROM Book b WHERE b.library.id = :id AND b.available", Book.class).setParameter("id", libraryId).getResultList();
            //return null;
            //return em.createNamedQuery("Book.getAvailableBooksFromLibrary", Book.class).setParameter("id", libraryId).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    //todo
    public List<Book> getNotAvailableBooksFromLibrary(Integer libraryId) {
        Objects.requireNonNull(libraryId);
        try {
            return null;
            //return em.createNamedQuery("Book.getNotAvailableBooksFromLibrary", Book.class).setParameter("id", libraryId).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    //todo
/*    public Optional<User> getBookOwner(Integer bookId) {
        Objects.requireNonNull(bookId);
        try {
            return em.createNamedQuery("Book.getNotAvailableBooksFromLibrary", Book.class).setParameter("id", libraryId).getResultList();
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }*/

    //todo
    public List<Book> findAllByTag(Tag tag) {
        Objects.requireNonNull(tag);
        try {
            return em.createQuery("SELECT b FROM Book b", Book.class).getResultList(); // vymyslet jak filtrovat
        }
        catch (NoResultException e) {
            return null;
        }
    }

    //todo
    public List<Book> findAllAvailableByTag(Tag tag) {
        Objects.requireNonNull(tag);
        try {
            return em.createQuery("SELECT b FROM Book b", Book.class).getResultList(); // vymyslet jak filtrovat
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
