package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.api.exceptions.LogicalException;
import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.dao.*;
import cz.cvut.kbss.ear.homeLibrary.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private final BookDAO bookDAO;

    private final LibraryDAO libraryDAO;

    private final TagDAO tagDAO;

    private final BookRentDAO bookRentDAO;

    private final UserDAO userDAO;


    @Autowired
    public BookService(BookDAO bookDAO, LibraryDAO libraryDAO, TagDAO tagDAO, BookRentDAO bookRentDAO, UserDAO userDAO) {
        this.bookDAO = bookDAO;
        this.libraryDAO = libraryDAO;
        this.tagDAO = tagDAO;
        this.bookRentDAO = bookRentDAO;
        this.userDAO = userDAO;
    }

    @Transactional(readOnly = true)
    public Book find(Integer id){
        return bookDAO.find(id);
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(){
       return bookDAO.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks(){
        return bookDAO.findAllAvailable();
    }

    @Transactional(readOnly = true)
    public List<Book> getBorrowedBooks(){
        return bookDAO.findAllBorrowed();
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooksFromLibrary(Integer libraryId){
        Objects.requireNonNull(libraryId);
        return bookDAO.getAllBooksFromLibrary(libraryId);
    }

    @Transactional(readOnly = true)
    public List<Book> getAvailableBooksFromLibrary(Integer libraryId){
        Objects.requireNonNull(libraryId);
        return bookDAO.getAvailableBooksFromLibrary(libraryId);
    }

    @Transactional(readOnly = true)
    public List<Book> getBorrowedBooksFromLibrary(Integer libraryId){
        Objects.requireNonNull(libraryId);
        return bookDAO.getBorrowedBooksFromLibrary(libraryId);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllByTag(String text) {
        Tag tag = tagDAO.findByText(text);
        if (tag == null) {
            throw NotFoundException.create(Tag.class.getName(), text);
        }
        List<Book> books = bookDAO.findAll();
        if (books == null) return new ArrayList<>();
        List<Book> filtered = new ArrayList<>();
        for (Book book : books) {
            if (book.getTags() != null) {
                if (book.getTags().contains(tag)) {
                    filtered.add(book);
                }
            }
        }
        return filtered;
    }

    @Transactional(readOnly = true)
    public List<Book> getAvailableAllByTag(String text) {
        Tag tag = tagDAO.findByText(text);
        if (tag == null) {
            throw NotFoundException.create(Tag.class.getName(), text);
        }
        List<Book> books = bookDAO.findAll();
        if (books == null) return new ArrayList<>();
        List<Book> filtered = new ArrayList<>();
        for (Book book : books) {
            if (book.getTags() != null && book.getLibrary().getVisible()) {
                if (book.getTags().contains(tag)) {
                    filtered.add(book);
                }
            }
        }
        return filtered;
    }

    @Transactional
    public void persist(Book book){
        Objects.requireNonNull(book);
        if (book.getTags() != null) {
            book.setTags(correctTags(book.getTags()));
        }
        bookDAO.persist(book);
        book.getLibrary().addBook(book);
        libraryDAO.update(book.getLibrary());
    }

    @Transactional
    public void update(Book book) {
        if (book != null && book.getTags() != null) {
            book.setTags(correctTags(book.getTags()));
        }
        bookDAO.update(book);
    }

    @Transactional
    public void remove(Book book) {
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
        tagDAO.removeUnusedTag();
    }

    //todo mb create chat between two users
    @Transactional
    public void borrow(Integer borrowerId, Book book) {
        User borrower = userDAO.find(borrowerId);
        Integer bookOwnerId = book.getLibrary().getUser().getId();

        BookRent bookRent = new BookRent();

        Date start = new Date();
        Date end = new Date(start.getTime() + (1000 * 60 * 60 * 24) * book.getLibrary().getBorrowingPeriod());

        bookRent.setOwnerId(bookOwnerId);
        bookRent.setStartDate(start);
        bookRent.setEndDate(end);
        bookRent.setArchive(false);
        bookRent.setBook(book);
        bookRent.setUser(borrower);

        bookRentDAO.persist(bookRent);
        bookRent.getBook().addBookRent(bookRent);
        bookDAO.update(bookRent.getBook());
        bookRent.getUser().addBookRent(bookRent);
        userDAO.update(bookRent.getUser());

        book.setAvailable(false);
        book.setAvailableFrom(end);
        bookDAO.update(book);
    }

    @Transactional
    public void returnBook(Book book){
        BookRent bookRent = bookRentDAO.findRentedByBookId(book.getId());
        if (bookRent == null) {
            throw new LogicalException("????");
        }
        bookRent.setArchive(true);
        bookRentDAO.update(bookRent);
        book.setAvailable(true);
        book.setAvailableFrom(null);
        bookDAO.update(book);
    }

    @Transactional
    public void addTag(Book book, Tag tag){
        book.addTag(tag);
        book.setTags(correctTags(book.getTags()));
        bookDAO.update(book);
        libraryDAO.update(book.getLibrary());
    }

    @Transactional
    public void removeTag(Book book, Tag tag){
        Tag oldTag = tagDAO.findByText(tag.getText());
        if (oldTag == null) {
            throw NotFoundException.create(Tag.class.getName(), tag.getText());
        }
        book.removeTag(oldTag);
        book.setTags(correctTags(book.getTags()));
        bookDAO.update(book);
        libraryDAO.update(book.getLibrary());
    }






    private List<Tag> correctTags(List<Tag> tags) {
        if (tags.size() == 0) return tags;
        List<Tag> newTags = new ArrayList<>();
        tags.forEach(t -> {
            Tag tmp = tagDAO.findByText(t.getText());
            if (tmp == null) {
                tagDAO.persist(t);
                tmp = tagDAO.findByText(t.getText());
                if (tmp != null) {
                    if (!newTags.contains(tmp)) newTags.add(tmp);
                }
            }
            if (!newTags.contains(tmp)) newTags.add(tmp);
        });
        return newTags;
    }

}
