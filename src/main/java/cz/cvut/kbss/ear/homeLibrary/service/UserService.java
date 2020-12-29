package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.dao.LibraryDAO;
import cz.cvut.kbss.ear.homeLibrary.dao.UserDAO;
import cz.cvut.kbss.ear.homeLibrary.model.BookRent;
import cz.cvut.kbss.ear.homeLibrary.model.EUserRole;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import cz.cvut.kbss.ear.homeLibrary.security.SecurityUtils;
import cz.cvut.kbss.ear.homeLibrary.security.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserDAO userDAO;

    private final LibraryDAO libraryDAO;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserDAO userRepository, LibraryDAO libraryDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userRepository;
        this.libraryDAO = libraryDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User find(Integer id){
        return userDAO.find(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll(){
        return userDAO.findAll();
    }

    @Transactional
    public void persist(User user){
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        if (user.getRole() == null) {
            user.setRole(EUserRole.USER);
        }
        userDAO.persist(user);
    }

    @Transactional
    public void update(User user){
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getId());
        User res = userDAO.find(user.getId());
        if (res != null) {
            userDAO.update(user);
        }
    }

    //todo library, books, bookrent, messages, all
    @Transactional
    public void remove(User user) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getId());
        User res = userDAO.find(user.getId());

    }

    @Transactional
    public Library createLibrary(Integer userId){
        Objects.requireNonNull(userId);
        User user = find(userId);
        if (libraryDAO.findByUserId(user.getId()).isPresent()) {
            return null;
        }
        Library library = new Library();
        library.setUser(user);
        libraryDAO.persist(library);
        user.setLibrary(library);
        userDAO.update(user);
        return library;
    }



    //todo
/*    @Transactional
    public void addBorrowedBook(User user, BookRent bookRent){
        Objects.requireNonNull(user);
        Objects.requireNonNull(bookRent);
        user.addBookRent(bookRent);
        userDAO.save(user);
    }*/


    //todo
/*    @Transactional
    public void removeBorrowedBook(User user, BookRent bookRent){
        Objects.requireNonNull(user);
        Objects.requireNonNull(bookRent);
        user.removeBookRent(bookRent);
        userDAO.save(user);
    }*/


    @Transactional(readOnly = true)
    public boolean emailExist(String email){
        return userDAO.findByEmail(email) != null;
    }


    public User authenticate(String email, String password) throws AuthenticationException {
        User user = userDAO.findByEmail(email);
        if (user == null) throw NotFoundException.create(User.class.getName(), email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else throw new BadCredentialsException("Bad Credentials");
    }

}
