package cz.cvut.kbss.ear.homeLibrary.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="tags")
public class Tag extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    private String text;    // in mySQL: VARCHAR(255)




    // todo
    @ManyToMany(mappedBy = "tags")
    private List<Book> books;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book){
        Objects.requireNonNull(book);
        if (books == null) {
            this.books = new ArrayList<>();
        }
        books.add(book);
    }

    public void removeBook(Book book){
        Objects.requireNonNull(book);
        if (books == null) {
            return;
        }
        books.removeIf(b -> Objects.equals(b.getId(), book.getId()));
    }
}
