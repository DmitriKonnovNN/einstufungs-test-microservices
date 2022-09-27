package solutions.dmitrikonnov.etmanagement.verwaltung.user;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import solutions.dmitrikonnov.einstufungstest.verwaltung.user.userDto.UserDtoGetDetails;
import solutions.dmitrikonnov.einstufungstest.verwaltung.user.userDto.UserDtoUpdateRole;

import javax.websocket.server.PathParam;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping ("api/v2.0.0/users")
@AllArgsConstructor

public class UserController {

    private final UserService userService;
    private static final String ID_NF_UPDATE_FAIL_MSG = "id %d not found. Update failed.";
    private final static String ID_NOT_FOUND_MSG = "id %d not found.";


    @GetMapping("/{id}/details")
    public ResponseEntity<UserDtoGetDetails> getDetails (

            @PathVariable Long id){
        if (!userService.checkIfExist(id)){
            throw new NoSuchElementException(String.format(ID_NOT_FOUND_MSG, id));}
        return ResponseEntity.status(HttpStatus.OK).body(userService.getDetails(id));
    }


    @PutMapping("/{id}/update/role")

    public void updateRole (
           @PathVariable Long id,
            @RequestBody @Validated UserDtoUpdateRole userDto){
        if (!userService.checkIfExist(id)){
            throw new NoSuchElementException(String.format(ID_NF_UPDATE_FAIL_MSG, id));}
        userDto.setId(id);
        userService.updateUserRole(userDto);
    }


    @PutMapping("/conftoken/exptime")

    @Validated
    public void setConfirmationTokenExpirationTime  (

            @PathParam (value = "minutes") Optional<Integer> minutes ) {

        userService.setExpirationTimeOfToken(minutes.get());
    }

    // hasRole ('ROLE_') hasAnyRole ('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')
    @PutMapping("/conftoken/resettime")

    public int resetConfirmationTokenExpirationTime (){
        return userService.resetExpirationTimeOfToken();
    }

    @GetMapping ("/conftoken/exptime")

    public int getConfirmationTokenExpirationTime () {
        return userService.getTokenExpiration();
    }

    @ExceptionHandler (MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String>noMatchingParameters1(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad type of param: "+ ex.getValue()
                +" .Expiration time should be given in Minutes. Min value = 1.");
    }
}