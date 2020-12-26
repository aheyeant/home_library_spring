package cz.cvut.kbss.ear.homeLibrary.api;

import cz.cvut.kbss.ear.homeLibrary.api.util.RestUtils;
import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    public final BookService service;

    @Autowired
    public BookController(BookService service){
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Book> getBooks(){
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book getBook(@PathVariable("id") Integer id ){
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBook(@RequestBody Book book) {
        service.save(book);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", book.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody Book newBook){
        Book oldBook = service.get(id);
        if (newBook.getId().equals(oldBook.getId())){
            this.service.update(newBook);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", oldBook.getId());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
        return null;
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("id") Integer id){
        Book book = service.get(id);
        service.remove(book);
    }

    @GetMapping(value="/owned/from-library/{library_id}")
    public Iterable<Book> getOwnedBooksFromLibrary(@PathVariable("library_id") Integer id){
        return service.getOwnedBooksFromLibrary(id);
    }

    @GetMapping(value="/borrowed/from-library/{library_id}")
    public Iterable<Book> getBorrowedBooksFromLibrary(@PathVariable("library_id") Integer id){
        return service.getBorrowedBooksFromLibrary(id);
    }

    @PutMapping(value="/{id}")
    public void borrow(){

    }
}
