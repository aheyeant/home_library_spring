package cz.cvut.kbss.ear.homeLibrary.config;

import cz.cvut.kbss.ear.homeLibrary.security.AuthenticationFailure;
import cz.cvut.kbss.ear.homeLibrary.security.AuthenticationSuccess;
import cz.cvut.kbss.ear.homeLibrary.security.DefaultAuthenticationProvider;
import cz.cvut.kbss.ear.homeLibrary.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)// Allow methods to be secured using annotation
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] COOKIES_TO_DESTROY = {
            SecurityConstants.SESSION_COOKIE_NAME,
            SecurityConstants.REMEMBER_ME_COOKIE_NAME
    };

    private final AuthenticationFailure authenticationFailure;

    private final AuthenticationSuccess authenticationSuccess;

    //private final DefaultAuthenticationProvider authenticationProvider;
    private final AuthenticationProvider authenticationProvider;

        @Autowired
    public SecurityConfig(AuthenticationFailure authenticationFailure,
                          AuthenticationSuccess authenticationSuccess,
                          AuthenticationProvider authenticationProvider) {
        this.authenticationFailure = authenticationFailure;
        this.authenticationSuccess = authenticationSuccess;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll().and()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and().headers().frameOptions().sameOrigin()
                .and().authenticationProvider(authenticationProvider)
                .csrf().disable()
                .formLogin().successHandler(authenticationSuccess)
                .failureHandler(authenticationFailure)
                .loginProcessingUrl(SecurityConstants.SECURITY_CHECK_URI)
                .usernameParameter(SecurityConstants.USERNAME_PARAM).passwordParameter(SecurityConstants.PASSWORD_PARAM)
                .and()
                .logout().invalidateHttpSession(true).deleteCookies(COOKIES_TO_DESTROY)
                .logoutUrl(SecurityConstants.LOGOUT_URI).logoutSuccessHandler(authenticationSuccess)
                .and().sessionManagement().maximumSessions(1);
    }

}
