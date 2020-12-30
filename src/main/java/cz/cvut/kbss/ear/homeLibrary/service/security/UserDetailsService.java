package cz.cvut.kbss.ear.homeLibrary.service.security;

import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.dao.UserDAO;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username is email
        final User user = userDAO.findByEmail(username);
        if (user == null) {
            throw new NotFoundException("User with username " + username + " not found.");
        }
        return new cz.cvut.kbss.ear.homeLibrary.security.model.UserDetails(user);
    }
}
