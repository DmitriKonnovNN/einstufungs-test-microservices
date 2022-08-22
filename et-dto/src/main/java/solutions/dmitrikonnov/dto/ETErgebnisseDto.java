package solutions.dmitrikonnov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import solutions.dmitrikonnov.etenums.ETAufgabenNiveau;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ETErgebnisseDto implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    public ETErgebnisseDto(ETErgebnisseDto ergebnisseDto) {
        this.id = ergebnisseDto.getId();
        this.aufgabenBogenHash = ergebnisseDto.getAufgabenBogenHash();
        this.maxErreichtesNiveau = ergebnisseDto.getMaxErreichtesNiveau();
        this.zahlRichtigerAntworten = ergebnisseDto.getZahlRichtigerAntworten();
        this.RichtigeLoesungenNachNiveau = ergebnisseDto.getRichtigeLoesungenNachNiveau();
        this.idZuRichtigkeitMap = ergebnisseDto.getIdZuRichtigkeitMap();
        this.niveauZurZahlRichtiger = ergebnisseDto.getNiveauZurZahlRichtiger();

    }

    private  String id;
    private  Integer aufgabenBogenHash;
    private ETAufgabenNiveau maxErreichtesNiveau;
    private  Short zahlRichtigerAntworten;
    private  List<ETAufgabenNiveau> RichtigeLoesungenNachNiveau = new ArrayList<>();
    private  Map<Integer, Boolean> idZuRichtigkeitMap = new HashMap<>();
    private  Map<ETAufgabenNiveau, Short> niveauZurZahlRichtiger = new HashMap<>();

}
