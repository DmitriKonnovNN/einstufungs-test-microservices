package solutions.dmitrikonnov.einstufungstest.weblayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.dto.ETAnswerSheetDto;
import solutions.dmitrikonnov.dto.ETEndResultForFE;
import solutions.dmitrikonnov.dto.ETTaskSheetDto;

import solutions.dmitrikonnov.einstufungstest.ETExerciseSetFetchedFromCache;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETTaskService;
import solutions.dmitrikonnov.exceptions.TimeForTestExpiredException;


import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("api/v2.0.0/et_ufzgi")
@Primary
public class ETTaskController implements SwitchableController {
    private final long TIME_FOR_TEST_Millis;
    private final ETTaskService taskService;
    private final ApplicationEventPublisher publisher;
    private final ExerciseSetCache cache;
    private final ApplicationContext context;
    private boolean isEnable;

    public ETTaskController(@Autowired ETTaskService taskService,
                            @Autowired ApplicationEventPublisher publisher,
                            @Value("${app.cache.toCheckCache.config.caffeine}")  String qualifier,
                            @Autowired ApplicationContext context,
                            @Value("${app.controller.timeForTest}") String time) {
        this.context = context;
        this.TIME_FOR_TEST_Millis = TimeUnit.MINUTES.toMillis(Long.parseLong(time));
        this.taskService = taskService;
        this.publisher = publisher;
        this.cache = (ExerciseSetCache)context.getBean(qualifier);
        this.isEnable = true;
    }

    @GetMapping
    public ResponseEntity<ETTaskSheetDto> getTasks(){
        System.out.println("get Aufgaben REST CONTROLLER THREAD: " +Thread.currentThread().getName());
        if(isEnable){
            var bogen = cache.getPreparedExerciseSet();
            publisher.publishEvent(new ETExerciseSetFetchedFromCache(this));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ETTaskSheetDto(
                            bogen.getTaskSheetHash(),
                            bogen.getTaskList(),
                            System.currentTimeMillis()));}
        else return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

    }

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAnswerSheetDto sheet){
        System.out.println("check and get results REST CONTROLLER THREAD: " +Thread.currentThread().getName());
        if(System.currentTimeMillis()- sheet.getCreatedAt()>TIME_FOR_TEST_Millis) {
            throw new TimeForTestExpiredException("Zeit für den Test ist um.");
        }
        var cachedSet = cache.fetch(sheet.getAnswerSheetId());
        var result = taskService.checkAnswerSheetAndGetTestResults(sheet,cachedSet);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }

     public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
