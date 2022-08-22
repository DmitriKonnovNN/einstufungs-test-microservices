package solutions.dmitrikonnov.etverwaltung.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import solutions.dmitrikonnov.etenums.ETAufgabenNiveau;
import solutions.dmitrikonnov.etentities.ETAufgabe;
import solutions.dmitrikonnov.etentities.ETSchwelle;
import solutions.dmitrikonnov.etverwaltung.businesslayer.ETConstructorService;
import solutions.dmitrikonnov.etverwaltung.construct.ETAufgabeConstructDTO;
import solutions.dmitrikonnov.etverwaltung.construct.ETItemConstructDTO;
import solutions.dmitrikonnov.etverwaltung.construct.ETSchwellenConstructDTO;

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
    public ResponseEntity<ETAufgabe> addTask(@Valid @RequestBody ETAufgabeConstructDTO dto){
        var result = service.addAufgabe(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
    @GetMapping("/tasks")
    public ResponseEntity<List<ETAufgabe>> getAllAufgaben(){
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
    public ResponseEntity<ETSchwelle> addLimit(@Valid @RequestBody ETSchwellenConstructDTO schwelle){

       return ResponseEntity.status(HttpStatus.CREATED).body(service.addSchwelle(schwelle));
    }

    @PutMapping("/limits")
    public ResponseEntity<ETSchwelle> updateLimit ( @Valid @RequestBody ETSchwellenConstructDTO schwelle){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateSchwelle(schwelle));
    }
    @PatchMapping("/limits")
    @ResponseStatus(HttpStatus.OK)
    public void patchLimit (@Valid @RequestBody ETSchwellenConstructDTO schwelle){
        service.patchSchwelle(schwelle);
    }

    @GetMapping("/limits")
    public Map<ETAufgabenNiveau,Short> getAllMaxLimits(){
        return service.getMaxSchwellenByNiveaus();
    }


}
