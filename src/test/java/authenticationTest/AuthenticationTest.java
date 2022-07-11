package authenticationTest;

import authentication.Authentication;
import authentication.CredentialsService;
import authentication.PermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        //Mock credentials
        Mockito.when(credentialsServiceMock.isValidCredential(user,pass)).thenReturn(true);
        //Mock Permission
        Mockito.when(permissionService.getPermission(user)).thenReturn(permission);

        authentication.setCredentialsService(credentialsServiceMock);
        authentication.setPermissionService(permissionService);

        Assertions.assertEquals(expected_result, authentication.login(user, pass),"Error....!!!");

//        Mockito.verify(credentialsServiceMock).isValidCredential("admin","admin");
//        Mockito.verify(permissionService).getPermission("admin");
    }
}
