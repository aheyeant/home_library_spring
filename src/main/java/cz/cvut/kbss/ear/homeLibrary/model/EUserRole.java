package cz.cvut.kbss.ear.homeLibrary.model;

public enum EUserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), GUEST("ROLE_GUEST");

    private final String name;

    EUserRole(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
