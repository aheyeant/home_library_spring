package cz.cvut.kbss.ear.homeLibrary.initializers;

import cz.cvut.kbss.ear.homeLibrary.model.*;
import cz.cvut.kbss.ear.homeLibrary.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class TestBDInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(TestBDInitializer.class);

    private final UserService userService;

    private final LibraryService libraryService;

    private final BookService bookService;

    private final TagService tagService;

    private final BookRentService bookRentService;

    private final PlatformTransactionManager txManager;

    @Autowired
    public TestBDInitializer(UserService userService, LibraryService libraryService, BookService bookService, TagService tagService, BookRentService bookRentService, PlatformTransactionManager txManager) {
        this.userService = userService;
        this.libraryService = libraryService;
        this.bookService = bookService;
        this.tagService = tagService;
        this.bookRentService = bookRentService;
        this.txManager = txManager;
    }

    @PostConstruct
    private void initSystem() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute((status) -> {

            User user1 = createTestUser("user1", "1_user");
            User user2 = createTestUser("user2", "2_user");
            User user3 = createTestUser("user3", "3_user");
            User user4 = createTestUser("user4", "4_user");
            // System already init

            if (user1 == null || user2 == null || user3 == null) {
                return null;
            }
            Library library1 = createTestLibrary(user1, true);
            Library library2 = createTestLibrary(user2, false);
            Library library3 = createTestLibrary(user3, true);

            Book book1_l1 = createTestBook(library1, "A", library1.getUser().getEmail(), "123", true);
            Book book2_l1 = createTestBook(library1, "A B", library1.getUser().getEmail(), "123", true);
            Book book3_l1 = createTestBook(library1, "B C", library1.getUser().getEmail(), "123", false);

            Book book1_l2 = createTestBook(library2, "A B C", library2.getUser().getEmail(), "123", true);
            Book book2_l2 = createTestBook(library2, "B", library2.getUser().getEmail(), "123", true);
            Book book3_l2 = createTestBook(library2, "C", library2.getUser().getEmail(), "123", false);

            Book book1_l3 = createTestBook(library3, "C", library3.getUser().getEmail(), "123", true);
            Book book2_l3 = createTestBook(library3, "B", library3.getUser().getEmail(), "123", true);
            Book book3_l3 = createTestBook(library3, "A", library3.getUser().getEmail(), "123", false);


            Tag test = new Tag();
            test.setText("A");
            book1_l1.addTag(test);
            bookService.update(book1_l1);

            BookRent bookRent1 = createTestBookRent(user1.getId(), book1_l1, user2);

/*            Tag tagA = new Tag();
            tagA.setText("A");
            book1_l1.addTag(tagA);
            bookService.update(book1_l1);*/
            return null;
        });
    }

    /**
     * @param firstName must be unique
     * @param surname any string
     */
    private User createTestUser(String firstName, String surname) {
        if (userService.emailExist(firstName + InitializerConstants.EMAIL_POSTFIX)) {
            return null;
        }
        final User user = new User();
        user.setFirstName(firstName);
        user.setSurname(surname);
        user.setEmail(firstName + InitializerConstants.EMAIL_POSTFIX);
        user.setPassword(InitializerConstants.DEFAULT_PASSWORD);
        user.setRole(EUserRole.USER);
        LOG.info("Created user with credentials " + user.getEmail() + "/" + user.getPassword());
        userService.persist(user);
        return user;
    }

    private Library createTestLibrary(User user, boolean visible) {
        Library library = new Library();
        library.setBorrowingPeriod(InitializerConstants.DEFAULT_BORROWING_PERIOD);
        library.setVisible(visible);
        user.setLibrary(library);
        LOG.info("Created library related to " + user.getEmail());
        libraryService.persist(library);
        userService.update(user);
        return library;
    }

    // DATE format (YYYY-MM-DD)
    private Book createTestBook(Library library, String title, String author, String isbn, boolean available) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setISBN(isbn);
        book.setAvailable(available);
        book.setAvailableFrom(new Date());
        book.setLibrary(library);
        bookService.persist(book);
        return book;
    }

    private Tag createTestTag(String text) {
        Tag tag = new Tag();
        tag.setText(text);
        tagService.persist(tag);
        return tag;
    }

    private BookRent createTestBookRent(Integer ownerId, Book book, User user) {
        BookRent bookRent = new BookRent();
        bookRent.setOwnerId(ownerId);
        bookRent.setStartDate(new Date());
        bookRent.setEndDate(new Date());
        bookRent.setArchive(false);
        bookRent.setBook(book);
        bookRent.setUser(user);
        bookRentService.persist(bookRent);
        return bookRent;
    }

}

