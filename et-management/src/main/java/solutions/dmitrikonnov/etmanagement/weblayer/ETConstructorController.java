package solutions.dmitrikonnov.etmanagement.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;
import solutions.dmitrikonnov.etentities.ETExercise;
import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etmanagement.businesslayer.ETConstructorService;
import solutions.dmitrikonnov.etmanagement.construct.ETAufgabeConstructDTO;
import solutions.dmitrikonnov.etmanagement.construct.ETItemConstructDTO;
import solutions.dmitrikonnov.etmanagement.construct.ETSchwellenConstructDTO;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2.1.0/constructor")
@AllArgsConstructor
public class ETConstructorController {
    private final ETConstructorService service;
    @PostMapping("/tasks")
    public ResponseEntity<ETExercise> addTask(@Valid @RequestBody ETAufgabeConstructDTO dto){
        var result = service.addAufgabe(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
    @GetMapping("/tasks")
    public ResponseEntity<List<ETExercise>> getAllAufgaben(){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllAufgaben());
    }

    @PutMapping(path = "/tasks/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addAufgabeImage(@RequestParam ("image") MultipartFile file, @PathVariable Integer id) throws IOException {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateAufgabeImageById(id, file));
    }
    @GetMapping(value = "/tasks/{id}/image")
    public byte[] downloadTodoImage(@PathVariable("id") int id) {
        return service.downloadTodoImage(id);
    }
    @DeleteMapping(value = "tasks/{id}/image")
    public ResponseEntity<String> deleteImageById(@PathVariable("id") int id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteImageByAufgabenId(id));
    }

    @PostMapping("/tasks/{id}/items")
    public void addItemToTask (@RequestBody List< @Valid ETItemConstructDTO> items, @PathVariable Integer id) {

        service.addItemsToAufgabe(items, id);
    }

    @PostMapping("/limits")
    public ResponseEntity<ETLimit> addLimit(@Valid @RequestBody ETSchwellenConstructDTO schwelle){

       return ResponseEntity.status(HttpStatus.CREATED).body(service.addSchwelle(schwelle));
    }

    @PutMapping("/limits")
    public ResponseEntity<ETLimit> updateLimit (@Valid @RequestBody ETSchwellenConstructDTO schwelle){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateSchwelle(schwelle));
    }
    @PatchMapping("/limits")
    @ResponseStatus(HttpStatus.OK)
    public void patchLimit (@Valid @RequestBody ETSchwellenConstructDTO schwelle){
        service.patchSchwelle(schwelle);
    }

    @GetMapping("/limits")
    public Map<ETExerciseLevel,Short> getAllMaxLimits(){
        return service.getMaxSchwellenByNiveaus();
    }


}
