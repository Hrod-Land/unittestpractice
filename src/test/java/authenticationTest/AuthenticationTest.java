package authenticationTest;

import authentication.Authentication;
import authentication.CredentialsService;
import authentication.PermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class AuthenticationTest {

    Authentication authentication;
    CredentialsService credentialsServiceMock;
    PermissionService permissionService;

    @BeforeEach
    public void setup (){

        authentication = new Authentication();
        credentialsServiceMock =  Mockito.mock(CredentialsService.class);
        permissionService = Mockito.mock(PermissionService.class);

        authentication.setCredentialsService(credentialsServiceMock);
        authentication.setPermissionService(permissionService);
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

        Mockito.when(credentialsServiceMock.isValidCredential(user,pass)).thenReturn(true);
        Mockito.when(permissionService.getPermission(user)).thenReturn(permission);

        Assertions.assertEquals(expected_result, authentication.login(user, pass),"Error....!!!");

        Mockito.verify(credentialsServiceMock).isValidCredential(user,pass);
        Mockito.verify(permissionService).getPermission(user);
    }

    @ParameterizedTest
    @CsvSource({ //All user/pass that ends with X is the wrong one
            "adminX,admin,user or password incorrect",
            "admin,adminX,user or password incorrect",
            "adminX,adminX,user or password incorrect",
    })
    public void verifyUnsuccessfulLogin(String user, String pass, String expected_result){

        Mockito.when(credentialsServiceMock.isValidCredential(user,pass)).thenReturn(false);

        Assertions.assertEquals(expected_result, authentication.login(user, pass),"Error....!!!");

        Mockito.verify(credentialsServiceMock).isValidCredential(user,pass);
    }
}
