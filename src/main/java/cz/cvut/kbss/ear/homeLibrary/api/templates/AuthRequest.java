package cz.cvut.kbss.ear.homeLibrary.api.templates;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class AuthRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}