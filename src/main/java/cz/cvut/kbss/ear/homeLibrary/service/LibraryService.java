package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.dao.*;
import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.BookRent;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class LibraryService {

    private final LibraryDAO libraryDAO;

    private final UserDAO userDAO;

    private final BookDAO bookDAO;

    private final BookRentDAO bookRentDAO;

    private final TagDAO tagDAO;


    @Autowired
    public LibraryService(LibraryDAO libraryDAO, UserDAO userDAO, BookDAO bookDAO, BookRentDAO bookRentDAO, TagDAO tagDAO) {
        this.libraryDAO = libraryDAO;
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.bookRentDAO = bookRentDAO;
        this.tagDAO = tagDAO;
    }

    @Transactional(readOnly = true)
    public Library findByIdAsAnonymous(Integer id) {
        Objects.requireNonNull(id);
        return libraryDAO.findByIdAsAnonymous(id);
    }

    @Transactional(readOnly = true)
    public Library findByIdAsAdmin(Integer id) {
        Objects.requireNonNull(id);
        return libraryDAO.findByIdAsAdmin(id);
    }

    @Transactional(readOnly = true)
    public List<Library> findAllAsAnonymous() {
        return libraryDAO.findAllAsAnonymous();
    }

    @Transactional(readOnly = true)
    public List<Library> findAllAsAdmin() {
        return libraryDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Library findByUserId(Integer userId) {
        return libraryDAO.findByUserId(userId);
    }


    @Transactional
    public void persist(Library library) {
        Objects.requireNonNull(library);
        Objects.requireNonNull(library.getUser());
        libraryDAO.persist(library);
    }

    @Transactional
    public void update(Library library) {
        Objects.requireNonNull(library);
        Objects.requireNonNull(library.getUser());
        libraryDAO.update(library);
        userDAO.update(library.getUser());
    }

    @Transactional
    public void remove(Library library) {
        Objects.requireNonNull(library);
        if (library.getBooks() != null) {
            int len = library.getBooks().size();
            for (int i = 0; i < len; i++) {
                Book book = library.getBooks().get(0);
                book.setTags(null);
                bookDAO.update(book);
                List<BookRent> bookRents = bookRentDAO.findAllByBookId(book.getId());
                if (bookRents != null) {
                    bookRents.forEach(bookRent -> {
                        bookRent.getUser().removeBookRent(bookRent);
                        userDAO.update(bookRent.getUser());
                        bookRent.getBook().removeBookRent(bookRent);
                        bookDAO.update(bookRent.getBook());
                        bookRentDAO.remove(bookRent);
                    });
                }
                book.getLibrary().removeBook(book);
                libraryDAO.update(book.getLibrary());
                bookDAO.remove(book);
            }
        }
        tagDAO.removeUnusedTag();

        library.getUser().setLibrary(null);
        userDAO.update(library.getUser());
        libraryDAO.remove(library);
    }


}
