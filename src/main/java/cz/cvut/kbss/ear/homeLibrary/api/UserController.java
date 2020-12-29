package cz.cvut.kbss.ear.homeLibrary.api;


import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.api.exceptions.AlreadyExistException;
import cz.cvut.kbss.ear.homeLibrary.api.templates.RegistrationRequest;
import cz.cvut.kbss.ear.homeLibrary.api.util.RestUtils;
import cz.cvut.kbss.ear.homeLibrary.model.EUserRole;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
        LOG.debug("User {} successfully registered.", user);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/" + user.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser().convertTDO();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable("id") Integer id){
        User user = userService.find(id);
        if (user == null) {
            throw NotFoundException.create(User.class.getName(), id);
        }
        return user.convertTDO();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers(){
        List<User> founded = userService.findAll();
        List<User> ret = new ArrayList<>();
        if (founded != null && founded.size() != 0) {
            for (User u : founded) {
                ret.add(u.convertTDO());
            }
        }
        return ret;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value="/create-library", produces = MediaType.APPLICATION_JSON_VALUE)
    public Library createLibrary(Authentication authentication){
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUser().convertTDO().getId();
        Library library = userService.createLibrary(userId);
        if (library == null) {
            throw AlreadyExistException.create(Library.class.getName(), "used_id=" + userId);
        }
        return library;
    }

}
