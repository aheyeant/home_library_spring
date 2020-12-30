package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.dao.BookDAO;
import cz.cvut.kbss.ear.homeLibrary.dao.BookRentDAO;
import cz.cvut.kbss.ear.homeLibrary.dao.UserDAO;
import cz.cvut.kbss.ear.homeLibrary.model.BookRent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BookRentService {

    private final BookRentDAO bookRentDAO;

    private final BookDAO bookDAO;

    private final UserDAO userDAO;


    @Autowired
    public BookRentService(BookRentDAO bookRentDAO, BookDAO bookDAO, UserDAO userDAO) {
        this.bookRentDAO = bookRentDAO;
        this.bookDAO = bookDAO;
        this.userDAO = userDAO;
    }

    @Transactional(readOnly = true)
    public BookRent find(Integer id){
        return bookRentDAO.find(id);
    }

    @Transactional(readOnly = true)
    public List<BookRent> findAll(){
        return bookRentDAO.findAll();
    }

    @Transactional(readOnly = true)
    public List<BookRent> findAllByBookId(Integer bookId) {
        return bookRentDAO.findAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public List<BookRent> findAllByUserId(Integer userId) {
        return bookRentDAO.findAllByUserId(userId);
    }

    @Transactional
    public void persist(BookRent bookRent){
        Objects.requireNonNull(bookRent);
        bookRentDAO.persist(bookRent);
        bookRent.getBook().addBookRent(bookRent);
        bookDAO.update(bookRent.getBook());
        bookRent.getUser().addBookRent(bookRent);
        userDAO.update(bookRent.getUser());
    }

    @Transactional
    public void update(BookRent bookRent) {
        Objects.requireNonNull(bookRent);
        bookRentDAO.update(bookRent);
    }

    @Transactional
    public void remove(BookRent bookRent) {
        Objects.requireNonNull(bookRent);
        bookRent.getUser().removeBookRent(bookRent);
        userDAO.update(bookRent.getUser());
        bookRent.getBook().removeBookRent(bookRent);
        bookDAO.update(bookRent.getBook());
        bookRentDAO.remove(bookRent);
    }
}
