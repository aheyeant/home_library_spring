package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.model.*;
import cz.cvut.kbss.ear.homeLibrary.repository.BookRepository;
import cz.cvut.kbss.ear.homeLibrary.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    private final LibraryRepository libraryRepository;


    @Autowired
    public BookService(BookRepository repository, LibraryRepository libraryRepository) {
        this.repository = repository;
        this.libraryRepository = libraryRepository;
    }

    @Transactional
    public Book get(Integer id){
        Objects.requireNonNull(id);
        return repository.getOne(id);
    }

    @Transactional(readOnly = true)
    public Iterable<Book> findAll(){
       return repository.findAll();
    }

    @Transactional
    public void save(Book book){
        repository.save(book);
    }

    @Transactional
    public void remove(Book book) {
        repository.delete(book);
    }

    @Transactional
    public void update(Book book) {
        repository.save(book);
    }

    @Transactional
    public void addTag(Book book, Tag tag){
        Objects.requireNonNull(book);
        Objects.requireNonNull(tag);
        book.addTag(tag);
        save(book);
    }

    @Transactional
    public void removeTag(Book book, Tag tag){
        Objects.requireNonNull(book);
        Objects.requireNonNull(tag);
        book.removeTag(tag);
        save(book);
    }

    @Transactional
    public Iterable<Book> getOwnedBooksFromLibrary(Integer libraryId){
        return repository.getOwnedBooksFromLibrary(libraryId);
    }

    @Transactional
    public Iterable<Book> getBorrowedBooksFromLibrary(Integer libraryId){
        return repository.getBorrowedBooksFromLibrary(libraryId);
    }

    @Transactional
    public void borrowBy(User borrower, Book book){
        if (book.getAvailable()){
           book.setAvailable(false);
           Library borrowersLibrary = libraryRepository.getOne(borrower.getLibrary().getId());
           BookRent rent = new BookRent();
        }
        else{

        }
    }

    @Transactional
    public void returnBook(Book book){

    }

}
