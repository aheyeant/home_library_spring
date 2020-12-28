package cz.cvut.kbss.ear.homeLibrary.initializers;

import cz.cvut.kbss.ear.homeLibrary.model.EUserRole;
import cz.cvut.kbss.ear.homeLibrary.model.User;
import cz.cvut.kbss.ear.homeLibrary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;

@Component
public class SystemAdminInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(SystemAdminInitializer.class);

    private final UserService userService;

    private final PlatformTransactionManager txManager;

    @Autowired
    public SystemAdminInitializer(UserService userService, PlatformTransactionManager txManager) {
        this.userService = userService;
        this.txManager = txManager;
    }

    @PostConstruct
    private void initSystem() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute((status) -> {
            generateAdmin();
            return null;
        });
    }

    /**
     * Generates an admin account if it does not already exist.
     */
    private void generateAdmin() {
        if (userService.emailExist(InitializerConstants.ADMIN_EMAIL)) {
            return;
        }
        final User admin = new User();
        admin.setFirstName("HL");
        admin.setSurname("Administrator");
        admin.setEmail(InitializerConstants.ADMIN_EMAIL);
        admin.setPassword(InitializerConstants.ADMIN_PASSWORD);
        admin.setRole(EUserRole.ADMIN);
        LOG.info("Generated admin user with credentials " + admin.getEmail() + "/" + admin.getPassword());
        userService.persist(admin);
    }

}

