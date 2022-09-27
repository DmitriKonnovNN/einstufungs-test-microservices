package solutions.dmitrikonnov.etmanagement.businesslayer;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etentities.ETItem;
import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETTaskLevel;
import solutions.dmitrikonnov.etmanagement.construct.ETTaskConstructDTO;
import solutions.dmitrikonnov.etmanagement.construct.ETItemConstructDTO;
import solutions.dmitrikonnov.etmanagement.construct.ETLimitConstructDTO;
import solutions.dmitrikonnov.etmanagement.s3.BucketName;
import solutions.dmitrikonnov.etmanagement.s3.S3FileStoreService;
import solutions.dmitrikonnov.exceptions.TaskNotFoundException;
import solutions.dmitrikonnov.exceptions.ThresholdNotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ETConstructorService {

    private final ETAufgabenRepo aufgabenRepo;
    private final SchwellenRepo schwellenRepo;
    private final S3FileStoreService s3store;

    public String updateAufgabeImageById (Integer id, MultipartFile file) {
        if(!aufgabenRepo.existsById(id))
            throw new NotFoundException(String.format("Keine Aufgabe mit id %d gefunden! Die Datei wurde nicht gespeichert.", id));
        if (file==null || file.isEmpty()) {
            throw new IllegalStateException("File to be uploaded is empty or not found!");
        }
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", BucketName.AUFGABE_MEDIADATA.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());

        try {
            s3store.upload(path, fileName, Optional.of(metadata), file.getInputStream());
            StringJoiner joiner = new StringJoiner(":");
            joiner.add(fileName).add(path);
            aufgabenRepo.updateImageDataById(id,joiner.toString());
        } catch ( IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return fileName;
    }

    public byte[] downloadTodoImage(int id) {

        var aufgabe = aufgabenRepo.findById(id).orElseThrow(()-> {
            throw new NotFoundException("Keine Aufgabe mit id %d gefunden!");});
        var fileAndPath = aufgabe.getAufgabenInhalt().split(":");
        return s3store.download(fileAndPath[1],fileAndPath[0]);
    }

    public String deleteImageByAufgabenId(int id) {
        var a = aufgabenRepo.findById(id).orElseThrow(()-> {
            throw new NotFoundException("Keine Aufgabe mit id %d gefunden!");});
        var fileAndPath = a.getAufgabenInhalt().split(":");
        s3store.delete(fileAndPath[0]);
        a.setAufgabenInhalt("");
        aufgabenRepo.save(a);
        return String.format("Image for task #%d has been deleted",id);
    }


    public ETTask addTask(ETTaskConstructDTO task){
        if(task.getItems().isEmpty()){
            var aufgabeEntity = setUpLoneETAufgabeEntity(task);
            log.debug("Ready to save to DB: " + aufgabeEntity.toString());
            return aufgabenRepo.save(aufgabeEntity);
        }
        var aufgabeEntity = setUpLoneETAufgabeEntity(task);
        task.getItems().forEach(itemDto->
            aufgabeEntity.addItem(setUpLoneETItem(itemDto)));
        log.debug("Ready to save to DB: " + aufgabeEntity.toString());
        return aufgabeEntity;
    }

    public void addItemsToAufgabe (List<ETItemConstructDTO> items, Integer taskId){
        var aufgabe = aufgabenRepo.findById(taskId)
                .orElseThrow(()-> new TaskNotFoundException(taskId));
        items.forEach(item->aufgabe.addItem(setUpLoneETItem(item)));
        aufgabenRepo.save(aufgabe);
    }

    public void deleteItemInAufgabe (Integer itemId, Integer aufgabeId) {

    }
    public void deleteAufgabeSamtItems (Integer aufgabeId) {

    }
    public List<ETTask> findAllTasks(){
        return aufgabenRepo.findAll();
    }
    public List<ETTask> findAllAufgabenByNiveau(ETTaskLevel level){
        return aufgabenRepo.findAllByAufgabenNiveau(level);
    }
    public List<ETLimit> findAllLimits(){
        return schwellenRepo.findAll();
    }

    public ETLimit findSchwelleByNiveau(ETExerciseLevel niveau){
        return  schwellenRepo.findByNiveau(niveau)
                .orElseThrow(()-> new ThresholdNotFoundException(niveau));

    }

    public ETLimit addSchwelle(ETLimitConstructDTO limit) {

        getMaxSchwellenByNiveaus();
        return schwellenRepo.save(ETLimit.builder()
                .niveau(limit.getLevel())
                .mindestSchwelle(limit.getMinLimit().shortValue())
                .maximumSchwelle(limit.getMaxLimit().shortValue())
                .build());
    }

    public Map<ETTaskLevel,Short> getMaxSchwellenByNiveaus (){
        return schwellenRepo.findMaximumSchwellenByNiveaus();
    }

    public ETLimit updateSchwelle(ETLimitConstructDTO limit) {
        var entity = findSchwelleByNiveau(limit.getLevel());
        entity.setMaximumSchwelle(limit.getMaxLimit().shortValue());
        entity.setMindestSchwelle(limit.getMinLimit().shortValue());
        return schwellenRepo.save(entity);
    }

    public void patchSchwelle(ETLimitConstructDTO limit) {
        if(schwellenRepo.existsByNiveau( limit.getLevel())){
            schwellenRepo.updateByNiveau(
                    limit.getLevel(),
                    limit.getMinLimit().shortValue(),
                    limit.getMaxLimit().shortValue());
        }
        else throw new ThresholdNotFoundException(limit.getLevel());
    }

    private ETTask setUpLoneETAufgabeEntity(ETTaskConstructDTO dto) {
        return ETTask.builder()
                .taskContent(dto.getTaskContent())
                .taskLevel(dto.getTaskLevel())
                .taskDefinition(dto.getTaskDefinition())
                .taskType(dto.getTaskType())
                .frontEndType(dto.getFrontEndType())
                .weigh(dto.getWeigh())
                .build();
    }

    private ETItem setUpLoneETItem(ETItemConstructDTO dto) {
        return ETItem.builder()
                .itemTaskContent(dto.getItemTaskContent())
                .possibleAnswers(Set.copyOf(dto.getPossibleSolutions()))
                .solutions(dto.getSolutions())
                .build();
    }
}
