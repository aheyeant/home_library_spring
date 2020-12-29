package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.dao.BookDAO;
import cz.cvut.kbss.ear.homeLibrary.dao.LibraryDAO;
import cz.cvut.kbss.ear.homeLibrary.dao.TagDAO;
import cz.cvut.kbss.ear.homeLibrary.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private final BookDAO bookDAO;

    private final LibraryDAO libraryDAO;

    private final TagDAO tagDAO;


    @Autowired
    public BookService(BookDAO bookDAO, LibraryDAO libraryDAO, TagDAO tagDAO) {
        this.bookDAO = bookDAO;
        this.libraryDAO = libraryDAO;
        this.tagDAO = tagDAO;
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

    //todo filter by visible
    @Transactional(readOnly = true)
    public List<Book> getAllBooksFromLibrary(Integer libraryId){
        Objects.requireNonNull(libraryId);
        return bookDAO.getAllBooksFromLibrary(libraryId);
    }

    //todo filter by visible
    @Transactional(readOnly = true)
    public List<Book> getAvailableBooksFromLibrary(Integer libraryId){
        Objects.requireNonNull(libraryId);
        return bookDAO.getAvailableBooksFromLibrary(libraryId);
    }

    //todo filter by visible
    @Transactional(readOnly = true)
    public List<Book> getBorrowedBooksFromLibrary(Integer libraryId){
        Objects.requireNonNull(libraryId);
        return bookDAO.getBorrowedBooksFromLibrary(libraryId);
    }

    @Transactional
    public void persist(Book book){
        if (book != null && book.getTags() != null) {
            book.setTags(correctTags(book.getTags()));
        }
        bookDAO.persist(book);
    }

    @Transactional
    public void update(Book book) {
        if (book != null && book.getTags() != null) {
            book.setTags(correctTags(book.getTags()));
        }
        bookDAO.update(book);
        tagDAO.removeUnusedTag();
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
