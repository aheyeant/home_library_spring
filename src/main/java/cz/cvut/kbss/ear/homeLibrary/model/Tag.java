package cz.cvut.kbss.ear.homeLibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="tags")
@NamedQueries({
        @NamedQuery(name = "Tag.findByText", query = "SELECT t FROM Tag t WHERE t.text = :text")
})
public class Tag extends AbstractIdentifiableObject {
    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String text;    // in mySQL: VARCHAR(255)

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
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

    //todo
    public void addBook(Book book){
        Objects.requireNonNull(book);
        if (books == null) {
            this.books = new ArrayList<>();
        }
        books.add(book);
    }

    //todo
    public void removeBook(Book book){
        Objects.requireNonNull(book);
        if (books == null) {
            return;
        }
        books.removeIf(b -> Objects.equals(b.getId(), book.getId()));
    }

    //todo toString
}
