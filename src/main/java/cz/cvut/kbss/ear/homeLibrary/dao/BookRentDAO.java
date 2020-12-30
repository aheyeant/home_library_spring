package cz.cvut.kbss.ear.homeLibrary.dao;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.BookRent;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;

@Repository
public class BookRentDAO extends BaseDAO<BookRent> {
    protected BookRentDAO() { super(BookRent.class); }


    public List<BookRent> findAllByBookId(Integer bookId) {
        Objects.requireNonNull(bookId);
        try {
            return em.createNamedQuery("BookRent.findAllByBookId", BookRent.class).setParameter("book_id", bookId).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<BookRent> findAllByUserId(Integer userId) {
        Objects.requireNonNull(userId);
        try {
            return em.createNamedQuery("BookRent.findAllByUserId", BookRent.class).setParameter("user_id", userId).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public BookRent findRentedByBookId(Integer bookId) {
        Objects.requireNonNull(bookId);
        try {
            return em.createNamedQuery("BookRent.findRentedByBookId", BookRent.class).setParameter("book_id", bookId).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
