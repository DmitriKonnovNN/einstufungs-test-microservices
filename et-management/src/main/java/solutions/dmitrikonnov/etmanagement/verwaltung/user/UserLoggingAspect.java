package solutions.dmitrikonnov.etmanagement.verwaltung.user;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Aspect
@Slf4j
public class UserLoggingAspect {


    private final static String TOKEN_EXPIRATION_TIME_RESET_MSG = "Confirmation Token's Expiration time has been " +
            "reset to its default value: %s";
    private final static String TOKEN_EXPIRATION_TIME_SET_MSG = "Confirmation Token's Expiration time has been " +
            "set to value: %s";


    @Pointcut("within(solutions.dmitrikonnov.einstufungstest.verwaltung.user.*)")
    private void inUserPackagePoint() {
    }

    /**
     * Pointcut ("solutions.dmitrikonnov.einstufungstest.verwaltung.user.UserController*)")
     */
    @Pointcut("within(solutions.dmitrikonnov.einstufungstest.verwaltung.user.UserController*)")
    protected void inUserControllerPoint() {
    }

    @Pointcut("execution(* set*(..))")
    private void anySetPoint() {
    }

    @Pointcut("execution(* resetConfirmationTokenExpirationTime(..))")
    private void resetConfTokenExpTimePoint() {
    }

    /**
     * Pointcut ("execution(* setConfirmationTokenExpirationTime(..))")
     */
    @Pointcut("execution(* setConfirmationTokenExpirationTime(..))")
    protected void setConfTokenExpTimePoint() {
    }


    @AfterReturning(value = "inUserControllerPoint() && resetConfTokenExpTimePoint()", returning = "retVal")
    public void afterResetConfirmationTokenExpTime(int retVal) {
        log.warn(String.format(TOKEN_EXPIRATION_TIME_RESET_MSG, retVal));
    }

    @Before("inUserControllerPoint() &&  setConfTokenExpTimePoint() && args(time)")
    public void beforeSetConfirmationTokenExpTime(Object time) {
        if (time instanceof Optional<?>) {
            var value = (Optional<?>) time;
            var value1 = value.get();
            if (value1 instanceof Integer) {
                System.out.println("CHECK DONE: " + value.get());
            }

        }

    }
}
