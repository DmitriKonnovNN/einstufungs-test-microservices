package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFEinCaceOfException;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETErgebnisseConverterAndPersister;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class ETAufgabenService {

    private final ETAufgabenAufsetzer aufsetzer;
    private final ETAntwortenPruefer pruefer;
    private final ETErgebnisseEvaluator evaluator;
    private final ETErgebnisseConverterAndPersister converterAndPersister;
    private final ETAufgabenBogenAufsetzer bogenAufsetzer;


    @Transactional(readOnly = true)
    public ETAufgabenBogen getAufgabenListe (){
        List<ETAufgabe> aufgesetzteListe = aufsetzer.listeAufsetzen();
        if(aufgesetzteListe.isEmpty()) return null;
        ETAufgabenBogen bogen = bogenAufsetzer.aufsetzen(aufgesetzteListe);
        return bogen;
    }


    @Transactional
    public ETEndResultForFE checkAntwortBogenAndGetTestErgebnisse (ETAntwortBogenDto antwortBogen, ETAufgabenBogen chachedAufgabenBogen) {
        var ergebnisseDto = pruefer.checkBogen(antwortBogen, chachedAufgabenBogen);
        var ergebnisseDto1 = evaluator.evaluate(ergebnisseDto);
        Future<String> ergebnisseUUID = converterAndPersister.convertAndPersist(ergebnisseDto1);

        try {
            return ETEndResultForFE.builder()
                    .erreichtesNiveau(ergebnisseDto1.getMaxErreichtesNiveau())
                    .zahlRichtigerAntworten(ergebnisseDto1.getZahlRichtigerAntworten().toString())
                    .id(ergebnisseUUID.get(5, TimeUnit.SECONDS))
                    .build();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(Arrays.toString(e.getStackTrace()));//TODO: we need here some logic to notify person in charge about the fact of data base's gone.
            ergebnisseUUID.cancel(true);
            return new ETEndResultForFEinCaceOfException(
                    ergebnisseDto1.getId(),
                    ergebnisseDto1.getMaxErreichtesNiveau(),
                    ergebnisseDto1.getZahlRichtigerAntworten().toString(),
                    ergebnisseDto1);
        }
    }

}

