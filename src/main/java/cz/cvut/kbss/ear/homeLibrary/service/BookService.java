package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.dao.BookDAO;
import cz.cvut.kbss.ear.homeLibrary.dao.LibraryDAO;
import cz.cvut.kbss.ear.homeLibrary.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    private final BookDAO bookDAO;

    private final LibraryDAO libraryDAO;


    @Autowired
    public BookService(BookDAO bookDAO, LibraryDAO libraryDAO) {
        this.bookDAO = bookDAO;
        this.libraryDAO = libraryDAO;
    }

    @Transactional(readOnly = true)
    public Optional<Book> find(Integer id){
        return Optional.of(bookDAO.find(id));
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(){
       return bookDAO.findAll();
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
    public List<Book> getNotAvailableBooksFromLibrary(Integer libraryId){
        return bookDAO.getNotAvailableBooksFromLibrary(libraryId);
    }


    @Transactional
    public void persist(Book book){
        bookDAO.persist(book);
    }

    @Transactional
    public void update(Book book) {
        bookDAO.update(book);
    }

    //todo tag
    @Transactional
    public void remove(Book book) {
        bookDAO.remove(book);
    }




    //todo
/*    @Transactional
    public void borrowBy(User borrower, Book book){
        if (book.getAvailable()){
           book.setAvailable(false);
           Library borrowersLibrary = libraryDAO.getOne(borrower.getLibrary().getId());
           BookRent rent = new BookRent();
        }
        else{

        }
    }*/

    //todo mb tag not exist
    @Transactional
    public void addTag(Book book, Tag tag){
        Objects.requireNonNull(book);
        Objects.requireNonNull(tag);
        book.addTag(tag);
        persist(book);
    }

    //todo mb tag not exist
    @Transactional
    public void removeTag(Book book, Tag tag){
        Objects.requireNonNull(book);
        Objects.requireNonNull(tag);
        book.removeTag(tag);
        persist(book);
    }

    //todo
    @Transactional
    public void returnBook(Book book){

    }

}
