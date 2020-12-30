package cz.cvut.kbss.ear.homeLibrary.api;


import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.api.exceptions.AlreadyExistException;
import cz.cvut.kbss.ear.homeLibrary.api.templates.RegistrationRequest;
import cz.cvut.kbss.ear.homeLibrary.api.util.RestUtils;
import cz.cvut.kbss.ear.homeLibrary.model.EUserRole;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import cz.cvut.kbss.ear.homeLibrary.security.jwt.JwtAuthenticationManager;
import cz.cvut.kbss.ear.homeLibrary.security.model.UserDetails;
import cz.cvut.kbss.ear.homeLibrary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    public final UserService userService;

    public final JwtAuthenticationManager jwtAuthenticationManager;


    @Autowired
    public UserController(UserService userService, JwtAuthenticationManager jwtAuthenticationManager) {
        this.userService = userService;
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }

    @PreAuthorize("anonymous || hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        if (userService.emailExist(registrationRequest.getEmail())) {
            throw AlreadyExistException.create(User.class.getName(), registrationRequest.getEmail());
        }
        User user = new User();
        user.setRole(EUserRole.USER);
        user.setFirstName(registrationRequest.getFirstName());
        user.setSurname(registrationRequest.getSurname());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        userService.persist(user);
        LOG.debug("User {} successfully registered.", user.getEmail());

        final HttpHeaders headers = RestUtils.createLocationHeaderFromHomeURI("/api/user/" + user.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/register-admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerAdmin(@RequestBody @Valid RegistrationRequest registrationRequest) {
        if (userService.emailExist(registrationRequest.getEmail())) {
            throw AlreadyExistException.create(User.class.getName(), registrationRequest.getEmail());
        }
        User user = new User();
        user.setRole(EUserRole.ADMIN);
        user.setFirstName(registrationRequest.getFirstName());
        user.setSurname(registrationRequest.getSurname());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        userService.persist(user);
        LOG.debug("User admin {} successfully registered.", user.getEmail());

        final HttpHeaders headers = RestUtils.createLocationHeaderFromHomeURI("/api/user/" + user.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Bad Credentials");
        }
        return jwtAuthenticationManager.getUserTDO(authentication);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable("id") Integer id){
        Objects.requireNonNull(id);
        User user = userService.find(id);
        if (user == null) {
            throw NotFoundException.create(User.class.getName(), id);
        }
        return user.convertTDO();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers(){
        return userService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value="/create-library", produces = MediaType.APPLICATION_JSON_VALUE)
    public Library createLibrary(Authentication authentication){
        Integer userId = jwtAuthenticationManager.getUserId(authentication);
        Library library = userService.createLibrary(userId);
        if (library == null) {
            throw AlreadyExistException.create(Library.class.getName(), "used_id=" + userId);
        }
        return library;
    }

    //todo send message
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @PostMapping(value = "/send/{recipient_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(Authentication authentication, @PathVariable("recipient_id") Integer recipientId, @RequestBody String messageText) {
        Objects.requireNonNull(recipientId);
        Objects.requireNonNull(messageText);
        Integer senderId = jwtAuthenticationManager.getUserId(authentication);
        //todo check if chat exist
        //todo send message
    }


    // todo remove chat
    // admin - can remove all
    // user - only current
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @DeleteMapping(value = "/chat/{chat_id}")
    public void removeChat(Authentication authentication, @PathVariable("chat_id") Integer chatId) {
        Objects.requireNonNull(chatId);
        //todo check if chat exist
        //todo remove chat with all messages
    }

    //todo remove user
}
