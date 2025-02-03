package com.example.solvesphere.TestUnit;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.UserData.User;
import org.testng.annotations.Test;

public class SecurityTests {
    private final String PASSWORD_FOR_TEST = "tstPass123";
    private final PasswordHasher hasher = new PasswordHasher();
    @Test
    public void testPassHahsing(){
        assert !hasher.hashPassword(PASSWORD_FOR_TEST).equals(PASSWORD_FOR_TEST);
    }
    @Test
    public void testPassValidation(){
        assert hasher.verifyPassword(PASSWORD_FOR_TEST, hasher.hashPassword(PASSWORD_FOR_TEST));
    }
}
