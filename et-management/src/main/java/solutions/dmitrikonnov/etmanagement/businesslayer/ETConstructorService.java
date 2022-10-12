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
import solutions.dmitrikonnov.etlimitsrepo.EtLimitsRepo;
import solutions.dmitrikonnov.etmanagement.constructDTO.ETTaskConstructDTO;
import solutions.dmitrikonnov.etmanagement.constructDTO.ETItemConstructDTO;
import solutions.dmitrikonnov.etmanagement.constructDTO.ETLimitConstructDTO;
import solutions.dmitrikonnov.etmanagement.s3.BucketName;
import solutions.dmitrikonnov.etmanagement.s3.S3FileStoreService;
import solutions.dmitrikonnov.ettaskrepo.ETTaskRepo;
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

    private final ETTaskRepo etTaskRepo;
    private final EtLimitsRepo limitsRepo;
    private final S3FileStoreService s3store;

    public String updateTaskImageByTaskId(Integer id, MultipartFile file) {
        if(!etTaskRepo.existsById(id))
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
            etTaskRepo.updateImageDataById(id,joiner.toString());
        } catch ( IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return fileName;
    }

    public byte[] downloadTodoImage(int id) {

        var task = etTaskRepo.findById(id).orElseThrow(()-> {
            throw new NotFoundException("Keine Aufgabe mit id %d gefunden!");});
        var fileAndPath = task.getTaskContent().split(":");
        return s3store.download(fileAndPath[1],fileAndPath[0]);
    }

    public String deleteImageByAufgabenId(int id) {
        var a = etTaskRepo.findById(id).orElseThrow(()-> {
            throw new NotFoundException("Keine Aufgabe mit id %d gefunden!");});
        var fileAndPath = a.getTaskContent().split(":");
        s3store.delete(fileAndPath[0]);
        a.setTaskContent("");
        etTaskRepo.save(a);
        return String.format("Image for task #%d has been deleted",id);
    }


    public ETTask addTask(ETTaskConstructDTO task){
        if(task.getItems().isEmpty()){
            var aufgabeEntity = setUpLoneETTaskEntity(task);
            log.debug("Ready to save to DB: " + aufgabeEntity.toString());
            return etTaskRepo.save(aufgabeEntity);
        }
        var aufgabeEntity = setUpLoneETTaskEntity(task);
        task.getItems().forEach(itemDto->
            aufgabeEntity.addItem(setUpLoneETItem(itemDto)));
        log.debug("Ready to save to DB: " + aufgabeEntity.toString());
        return aufgabeEntity;
    }

    public void addItemsToAufgabe (List<ETItemConstructDTO> items, Integer taskId){
        var task = etTaskRepo.findById(taskId)
                .orElseThrow(()-> new TaskNotFoundException(taskId));
        items.forEach(item->task.addItem(setUpLoneETItem(item)));
        etTaskRepo.save(task);
    }

    public void deleteItemWithingTask(Integer itemId, Integer taskId) {

    }
    public void deleteTaskWithItems(Integer taskId) {

    }
    public List<ETTask> findAllTasks(){
        return etTaskRepo.findAll();
    }
    public List<ETTask> findAllTasksByLevel(ETTaskLevel level){
        return etTaskRepo.findAllByTaskLevel(level);
    }
    public List<ETLimit> findAllLimits(){
        return limitsRepo.findAll();
    }

    public ETLimit findLimitsByLevel(ETTaskLevel niveau){
        return  limitsRepo.findByLevel(niveau)
                .orElseThrow(()-> new ThresholdNotFoundException(niveau));

    }

    public ETLimit addLimit(ETLimitConstructDTO limit) {

        getMaxLimitsByLevels();
        return limitsRepo.save(ETLimit.builder()
                .level(limit.getLevel())
                .minLimit(limit.getMinLimit().shortValue())
                .maxLimit(limit.getMaxLimit().shortValue())
                .build());
    }

    public Map<ETTaskLevel,Short> getMaxLimitsByLevels(){
        return limitsRepo.findMaximumLimitByLevels();
    }

    public ETLimit updateSchwelle(ETLimitConstructDTO limit) {
        var entity = findLimitsByLevel(limit.getLevel());
        entity.setMaxLimit(limit.getMaxLimit().shortValue());
        entity.setMinLimit(limit.getMinLimit().shortValue());
        return limitsRepo.save(entity);
    }

    public void patchLimit(ETLimitConstructDTO limit) {
        if(limitsRepo.existsByLevel( limit.getLevel())){
            limitsRepo.updateByLevel(
                    limit.getLevel(),
                    limit.getMinLimit().shortValue(),
                    limit.getMaxLimit().shortValue());
        }
        else throw new ThresholdNotFoundException(limit.getLevel());
    }

    private ETTask setUpLoneETTaskEntity(ETTaskConstructDTO dto) {
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
