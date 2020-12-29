package cz.cvut.kbss.ear.homeLibrary.api;

import cz.cvut.kbss.ear.homeLibrary.api.templates.AuthRequest;
import cz.cvut.kbss.ear.homeLibrary.api.templates.AuthResponse;
import cz.cvut.kbss.ear.homeLibrary.config.jwt.JwtProvider;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import cz.cvut.kbss.ear.homeLibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    private final JwtProvider jwtProvider;


    @Autowired
    public AuthController(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @PostMapping
    public AuthResponse auth(@RequestBody @Valid AuthRequest request) {
        User user = userService.authenticate(request.getEmail(), request.getPassword());
        String token = jwtProvider.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}