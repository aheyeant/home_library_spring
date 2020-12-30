package cz.cvut.kbss.ear.homeLibrary.api;

import cz.cvut.kbss.ear.homeLibrary.api.exceptions.LibraryNullPointerException;
import cz.cvut.kbss.ear.homeLibrary.api.exceptions.LogicalException;
import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.api.exceptions.ValidationException;
import cz.cvut.kbss.ear.homeLibrary.api.util.RestUtils;
import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.Tag;
import cz.cvut.kbss.ear.homeLibrary.security.jwt.JwtAuthenticationManager;
import cz.cvut.kbss.ear.homeLibrary.service.BookRentService;
import cz.cvut.kbss.ear.homeLibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/books")
public class BookController {

    public final BookService bookService;

    public final BookRentService bookRentService;

    public final JwtAuthenticationManager jwtAuthenticationManager;

    @Autowired
    public BookController(BookService bookService, BookRentService bookRentService, JwtAuthenticationManager jwtAuthenticationManager){
        this.bookService = bookService;
        this.bookRentService = bookRentService;
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }

    //public
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllBooks(){
        return bookService.findAll();
    }

    //public
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAvailableBooks(){
        return bookService.getAvailableBooks();
    }

    //public
    @GetMapping(value = "/borrowed", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getBorrowedBooks(){
        return bookService.getBorrowedBooks();
    }

    //public
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book getBook(@PathVariable("id") Integer id ){
        final Book book = bookService.find(id);
        if (book == null) {
            throw NotFoundException.create(Book.class.getName(), id);
        }
        return book;
    }

    //public
    @GetMapping(value="/library/{library_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllBooksFromLibrary(Authentication auth, @PathVariable("library_id") Integer libraryId){
        Objects.requireNonNull(libraryId);
        List<Book> books = bookService.getAllBooksFromLibrary(libraryId);
        if (books == null) {
            throw  NotFoundException.create(Library.class.getName(), libraryId);
        }
        if (jwtAuthenticationManager.isAdmin(auth)) {
            return books;
        }
        List<Book> ret = new ArrayList<>();
        for (Book book : books) {
            if (book.getLibrary().getVisible()) {
                ret.add(book);
            }
        }
        return ret;
    }

    //public
    @GetMapping(value="/library/available/{library_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAvailableBooksFromLibrary(Authentication auth, @PathVariable("library_id") Integer libraryId){
        Objects.requireNonNull(libraryId);
        List<Book> books = bookService.getAvailableBooksFromLibrary(libraryId);
        if (books == null) {
            throw  NotFoundException.create(Library.class.getName(), libraryId);
        }
        if (jwtAuthenticationManager.isAdmin(auth)) {
            return books;
        }
        List<Book> ret = new ArrayList<>();
        for (Book book : books) {
            if (book.getLibrary().getVisible()) {
                ret.add(book);
            }
        }
        return ret;
    }

    //public
    @GetMapping(value="/library/borrowed/{library_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getBorrowedBooksFromLibrary(Authentication auth, @PathVariable("library_id") Integer libraryId){
        Objects.requireNonNull(libraryId);
        List<Book> books = bookService.getBorrowedBooksFromLibrary(libraryId);
        if (books == null) {
            throw  NotFoundException.create(Library.class.getName(), libraryId);
        }
        if (jwtAuthenticationManager.isAdmin(auth)) {
            return books;
        }
        List<Book> ret = new ArrayList<>();
        for (Book book : books) {
            if (book.getLibrary().getVisible()) {
                ret.add(book);
            }
        }
        return ret;
    }

    // admin - see all
    // other - see only visible libraries
    @GetMapping(value="/tag/{tag_text}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllByTag(Authentication auth, @PathVariable("tag_text") String text) {
        Objects.requireNonNull(text);
        if (jwtAuthenticationManager.isAdmin(auth)) {
            return bookService.getAllByTag(text);
        }
        return bookService.getAvailableAllByTag(text);
    }


    // user - can add to own library
    // admin - no
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBook(Authentication auth, @RequestBody Book book) {
        Objects.requireNonNull(book);
        Library library = jwtAuthenticationManager.getUserLibrary(auth);
        if (library == null) {
            throw LibraryNullPointerException.create(jwtAuthenticationManager.getUserId(auth));
        }
        book.setLibrary(library);
        bookService.persist(book);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromHomeURI("api/books/{id}", book.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // user - can edit in its own library
    // admin - can be edited in the whole library
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/update/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(Authentication auth, @PathVariable("book_id") Integer id, @RequestBody Book newBook){
        Objects.requireNonNull(id);
        Objects.requireNonNull(newBook);
        Book oldBook = bookService.find(id);
        if (oldBook == null) {
            throw NotFoundException.create(Book.class.getName(), id);
        }
        if (!oldBook.getLibrary().getId().equals(jwtAuthenticationManager.getUserLibraryId(auth)) && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("Library_id=" + jwtAuthenticationManager.getUserLibraryId(auth) + " dont have book_id=" + oldBook.getId());
        }
        if (!newBook.validate()) {
            throw new ValidationException(Book.class.getName());
        }
        oldBook.setTitle(newBook.getTitle());
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setISBN(newBook.getISBN());
        oldBook.setAvailable(newBook.getAvailable());
        oldBook.setAvailableFrom(newBook.getAvailableFrom());
        bookService.update(oldBook);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromHomeURI("api/books/{id}", oldBook.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // user - can delete in its own library
    // admin - can be deleted in the whole library
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete/{book_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(Authentication auth, @PathVariable("book_id") Integer id){
        Objects.requireNonNull(id);
        Book book = bookService.find(id);
        if (book == null) {
            throw NotFoundException.create(Book.class.getName(), id);
        }
        if (!book.getLibrary().getId().equals(jwtAuthenticationManager.getUserLibraryId(auth))  && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("Library_id=" + jwtAuthenticationManager.getUserLibraryId(auth) + " dont have book_id=" + book.getId());
        }
        bookService.remove(book);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value="/borrow/{book_id}")
    public void borrow(Authentication auth, @PathVariable("book_id") Integer id){
        Objects.requireNonNull(id);
        Book book = bookService.find(id);
        if (book == null) {
            throw NotFoundException.create(Book.class.getName(), id);
        }
        if (book.getLibrary().getId().equals(jwtAuthenticationManager.getUserLibraryId(auth))) {
            throw new LogicalException("User own this book");
        }
        if (!book.getAvailable()) {
            throw new ValidationException("Book is borrowed");
        }
        Integer borrowerId = jwtAuthenticationManager.getUserId(auth);
        bookService.borrow(borrowerId, book);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value="/return/{book_id}")
    public void returnBook(Authentication auth, @PathVariable("book_id") Integer id){
        Objects.requireNonNull(id);
        Book book = bookService.find(id);
        if (book == null) {
            throw NotFoundException.create(Book.class.getName(), id);
        }
        if (book.getAvailable()) {
            throw new ValidationException("Book is not borrowed");
        }
        bookService.returnBook(book);
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PostMapping(value="/add-tag/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTag(Authentication auth, @PathVariable("book_id") Integer id, @RequestBody Tag tag) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(tag);
        Book book = bookService.find(id);
        if (book == null) {
            throw NotFoundException.create(Book.class.getName(), id);
        }
        if (!book.getLibrary().getId().equals(jwtAuthenticationManager.getUserLibraryId(auth))  && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("book_id=" + book.getId());
        }
        if (!tag.validate()) {
            throw new ValidationException(Tag.class.getName());
        }
        bookService.addTag(book, tag);
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PostMapping(value="/remove-tag/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeTag(Authentication auth, @PathVariable("book_id") Integer id, @RequestBody Tag tag) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(tag);
        Book book = bookService.find(id);
        if (book == null) {
            throw NotFoundException.create(Book.class.getName(), id);
        }
        if (!book.getLibrary().getId().equals(jwtAuthenticationManager.getUserLibraryId(auth))  && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("book_id=" + book.getId());
        }
        if (!tag.validate()) {
            throw new ValidationException(Tag.class.getName());
        }
        bookService.removeTag(book, tag);
    }

}
