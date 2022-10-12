package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import solutions.dmitrikonnov.einstufungstest.exceptions.BadRequestParameterException;

import java.util.Optional;

@Component
@Aspect
@Slf4j
public class UserControllerValidatorAspect {

    private static final String EXP_TIME_RESTRICTIONS_MSG = "Expiration time should be given in Minutes. Min value = 1, Max value = %d";

    private final int MAX_POSSIBLE_EXP_TIME;


    public UserControllerValidatorAspect(@Value("${app.userService.maxTokenExpirationTime.min}") int minutes,
                                         @Value("${app.userService.maxTokenExpirationTime.hours}") int hours,
                                         @Value("${app.userService.maxTokenExpirationTime.days}") int days) {
        int DAYS, HOURS, MINUTES;
        if (days>0){
            DAYS = days;
            HOURS = 24;
            MINUTES = 60;
        }
        else if (hours>0){
            DAYS = 1;
            HOURS = hours;
            MINUTES = 60;
        }
        else {
            DAYS = 1;
            HOURS = 1;
            MINUTES = minutes;
        }

        this.MAX_POSSIBLE_EXP_TIME = MINUTES*HOURS*DAYS;
    }




    @Around("UserLoggingAspect.inUserControllerPoint() && UserLoggingAspect.setConfTokenExpTimePoint() && args(time) && @annotation(org.springframework.validation.annotation.Validated)")
    public Object aroundSetConfToken(ProceedingJoinPoint pjp, Object time) throws Throwable {

        if (time instanceof Optional<?>){
            var value = (Optional<?>)time;
            if(value.isEmpty()) {
                throw new BadRequestParameterException("No parameters have been passed! " +
                        String.format(EXP_TIME_RESTRICTIONS_MSG, MAX_POSSIBLE_EXP_TIME) );
            }

            var value1 = value.get();
            if (value1 instanceof Integer){
                var value2 = (Integer)value1;
                if(value2<1 || value2> MAX_POSSIBLE_EXP_TIME){
                    throw new BadRequestParameterException(String.format(EXP_TIME_RESTRICTIONS_MSG, MAX_POSSIBLE_EXP_TIME));
                }
                Object result = pjp.proceed();
                log.info("Param: "+ value2 + " have been validated");
                time = value2; // TODO : Check whether this line is needed and works properly!
                return result;
            }



        }
        throw  new BadRequestParameterException("Expiration time should be given in Minutes. Min value = 1.");
    }
}
