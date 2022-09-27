package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabeDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.utils.ETAufgabenToDTOConverter;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ETAufgabenBogenAufsetzer {
    private final ETAufgabenToDTOConverter aufgabeToDtoConverter;

    public ETAufgabenBogen aufsetzen (List<ETAufgabe> aufgaben) {
        final Integer aufgabenBogenHash = aufgaben.hashCode();
        final var dtos = aufgabeToDtoConverter.convert(aufgaben,aufgabenBogenHash);
        final var shuffeled = shuffleItems(dtos);

        return ETAufgabenBogen.builder()
                .aufgabenBogenHash(aufgabenBogenHash)
                .itemZuLoesungen(extractItems(aufgaben))
                .itemZuNiveau(extractNiveaus(aufgaben))
                .aufgabenListe(shuffeled)
                .build();
    }

    private Map<Integer, List<String>> extractItems (List<ETAufgabe> aufgaben) {
        Map<Integer, List<String>> itemIdZuLoesungen = new HashMap<>();

        for (ETAufgabe aufgabe: aufgaben) {
            aufgabe.getItems().forEach(item -> {
                itemIdZuLoesungen.put(item.getItemId(), item.getLoesungen());

            });
        }
        return itemIdZuLoesungen;
    }

    private Map<Integer, ETAufgabenNiveau> extractNiveaus (List<ETAufgabe> aufgaben){

        Map<Integer,ETAufgabenNiveau> itemIdZuNiveau = new HashMap<>();
        for (ETAufgabe aufgabe: aufgaben) {
            ETAufgabenNiveau niveau = aufgabe.getAufgabenNiveau();
            aufgabe.getItems().forEach(item -> {
                itemIdZuNiveau.put(item.getItemId(), niveau);
            });
        }
        return itemIdZuNiveau;
    }

    private List<ETAufgabeDto> shuffleItems(List<ETAufgabeDto> dtos) {
        return dtos.stream()
                .peek(aufgdto -> Collections.shuffle(aufgdto.getItems()))
                .collect(Collectors.groupingBy(ETAufgabeDto::getNiveau, TreeMap::new, Collectors.toList()))
                .values()
                .stream()
                .peek(Collections::shuffle)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }



}
