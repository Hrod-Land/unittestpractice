package authenticationStaticTest;
import authenticationStatic.Authentication;
import authenticationStatic.CredentialsStaticService;
import authenticationStatic.PermissionStaticService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class AuthenticationStaticTest {

    Authentication authentication;

    @BeforeEach
    public void setup (){

        authentication = new Authentication();
    }


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

        String actual_result = authentication.login(user,pass);

        credentialsStaticServiceMocked.close();
        permissionStaticServiceMocked.close();

        Assertions.assertEquals(expected_result, actual_result, "ERROR...!!!");

    }

    @ParameterizedTest
    @CsvSource({ //All user/pass that ends with X is the wrong one
            "adminX,admin,user or password incorrect",
            "admin,adminX,user or password incorrect",
            "adminX,adminX,user or password incorrect"
    })
    public void verifyUnsuccessfulLogin(String user, String pass, String expected_result){

        MockedStatic<CredentialsStaticService> credentialsStaticServiceMocked = Mockito.mockStatic(CredentialsStaticService.class);
        credentialsStaticServiceMocked.when(()->CredentialsStaticService.isValidCredential(user, pass)).thenReturn(false);
        credentialsStaticServiceMocked.close();

        Assertions.assertEquals(expected_result, authentication.login(user,pass), "FAILED: Take attention...!!!");
    }
}
