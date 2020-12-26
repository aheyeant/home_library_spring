package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.model.BookRent;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import cz.cvut.kbss.ear.homeLibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Transactional
    public void create(User user){
        Objects.requireNonNull(user);
        createLibrary(user);
        repository.save(user);
    }

    /**
     * Creates a library for a given user.
     *
     * @param user User
     * */
    @Transactional
    public Library createLibrary(User user){
        Library library = new Library();
        user.setLibrary(library);
        return library;
    }

    @Transactional
    public Optional<User> get(Integer id){
        return repository.findById(id);
    }

    @Transactional
    public List<User> getAll(){
        return repository.findAll();
    }

    public void update(Integer id, User user){
        Optional<User> opt = repository.findById(id);
            opt.ifPresent(userOld -> {
                if (user.getId().equals(userOld.getId())){
                    repository.save(user);
                }
        });

    }

    @Transactional
    public void remove(Integer id){
        Objects.requireNonNull(id);
        Optional<User> user = repository.findById(id);
        user.ifPresent(repository::delete);
    }

    @Transactional(readOnly = true)
    public boolean exists(String email){
        return repository.findByEmail(email) != null;
    }

    @Transactional
    public void addBorrowedBook(User user, BookRent bookRent){
        Objects.requireNonNull(user);
        Objects.requireNonNull(bookRent);
        user.addBookRent(bookRent);
        repository.save(user);
    }

    @Transactional
    public void removeBorrowedBook(User user, BookRent bookRent){
        Objects.requireNonNull(user);
        Objects.requireNonNull(bookRent);
        user.removeBookRent(bookRent);
        repository.save(user);
    }
}
