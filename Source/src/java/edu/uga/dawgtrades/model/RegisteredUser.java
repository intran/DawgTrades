package edu.uga.dawgtrades.model;

public interface RegisteredUser
        extends Persistable {

    String getName();

    void setName(String name);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getPassword();

    void setPassword(String password);
    void setPasswordDirect(String password);

    boolean getIsAdmin();

    void setIsAdmin(boolean isAdmin);

    boolean getIsApproved();

    void setIsApproved(boolean isAdmin);

    String getEmail();

    void setEmail(String email);

    String getPhone();

    void setPhone(String phone);

    boolean getCanText();

    void setCanText(boolean canText);
}
