package authenticationStaticTest;

import authenticationStatic.Authentication;
import authenticationStatic.CredentialsStaticService;
import authenticationStatic.PermissionStaticService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class AuthenticationStaticTest {

    @ParameterizedTest
    @CsvSource({
            "admin,admin,CRUD,user authenticated successfully with permission: [CRUD]",
            "c_user,c_userP,CR,user authenticated successfully with permission: [CR]",
            "r_user,r_userP,R,user authenticated successfully with permission: [R]",
            "u_user,u_userP,RU,user authenticated successfully with permission: [RU]",
            "d_user,d_userP,RD,user authenticated successfully with permission: [RD]",
            "x_user,x_userP,No Permission for this user,user authenticated successfully with permission: [No Permission for this user]"
    })
    public void verifySuccessfulLogin(String user, String pass, String permission, String expected_result){
        MockedStatic<CredentialsStaticService> credentialsStaticServiceMocked = Mockito.mockStatic(CredentialsStaticService.class);
        credentialsStaticServiceMocked.when(()->CredentialsStaticService.isValidCredential(user, pass)).thenReturn(true);

        MockedStatic<PermissionStaticService> permissionStaticServiceMocked = Mockito.mockStatic(PermissionStaticService.class);
        permissionStaticServiceMocked.when(()->PermissionStaticService.getPermission(user)).thenReturn(permission);

        Authentication authentication = new Authentication();

        String actual_result = authentication.login(user,pass);

        Assertions.assertEquals(expected_result, actual_result, "ERROR...!!!");

        credentialsStaticServiceMocked.close();
        permissionStaticServiceMocked.close();

    }
}
