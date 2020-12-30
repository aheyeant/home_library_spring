package cz.cvut.kbss.ear.homeLibrary.security.jwt;

import cz.cvut.kbss.ear.homeLibrary.dao.LibraryDAO;
import cz.cvut.kbss.ear.homeLibrary.model.EUserRole;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import cz.cvut.kbss.ear.homeLibrary.security.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationManager {

    public User getUserTDO(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser().convertTDO();
    }

    public Integer getUserId(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }

    public String getUserEmail(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser().getEmail();
    }

    /**
     * @param authentication - token
     * @return libraryId or Null
     */
    public Integer getUserLibraryId(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails.getUser().getLibrary() != null) {
            return userDetails.getUser().getLibrary().getId();
        }
        return null;
    }

    public Library getUserLibrary(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails.getUser().getLibrary() != null) {
            return userDetails.getUser().getLibrary();
        }
        return null;
    }

    public boolean isAdmin(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser().getRole() == EUserRole.ADMIN;
    }

    public boolean isUser(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser().getRole() == EUserRole.USER;
    }

    public boolean isAnonymous(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken || authentication == null;
    }
}
