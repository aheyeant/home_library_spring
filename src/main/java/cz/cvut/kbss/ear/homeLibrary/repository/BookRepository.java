package cz.cvut.kbss.ear.homeLibrary.repository;

import cz.cvut.kbss.ear.homeLibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("FROM Book WHERE Book.origin.id = ?1")
    public Iterable<Book> getOwnedBooksFromLibrary(Integer id);

    @Query("FROM Book WHERE Book.current.id = ?1")
    public Iterable<Book> getBorrowedBooksFromLibrary(Integer id);
}
