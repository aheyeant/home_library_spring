package cz.cvut.kbss.ear.homeLibrary.api;


import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.api.util.RestUtils;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import cz.cvut.kbss.ear.homeLibrary.security.model.AuthenticationToken;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PreAuthorize("(!#user.isAdmin() && anonymous) || hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) {
        userService.persist(user);
        LOG.debug("User {} successfully registered.", user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Principal principal) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        User user = auth.getPrincipal().getUser();
        return user;
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable("id") Integer id){
        User user = userService.find(id);
        if (user == null) {
            throw NotFoundException.create(User.class.getName(), id);
        }
        return user;
    }

    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers(){
        return userService.findAll();
    }

/*    @PostMapping(value="/create-library", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Library createLibrary(@RequestBody User user){
        return service.createLibrary(user);
    }*/

}
