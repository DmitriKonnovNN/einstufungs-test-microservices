package solutions.dmitrikonnov.einstufungstest.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.etenums.ETTaskType;
import solutions.dmitrikonnov.dto.ETTaskDto;
import solutions.dmitrikonnov.dto.ETItemDto;
import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etentities.ETItem;
import solutions.dmitrikonnov.etutils.ETTasksToDTOConverter;

import static solutions.dmitrikonnov.etenums.ETTaskLevel.*;
import solutions.dmitrikonnov.etutils.Randomizer;



import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETAufgabenToDTOConverterTest {


    private ETTasksToDTOConverter underTest = new ETTasksToDTOConverter();
    private List<ETTask> givenAufgabenListe;
    private List<ETTaskDto> expectedListOfDto;
    int ABH;
    long ID;

    @BeforeEach
    void setUp() {
        Random r = new Random();
        long range = 1234567L;
        ID = (long)(r.nextDouble()*range);
        ABH = Randomizer.generate(1,1000);
        givenAufgabenListe = new ArrayList<>();
        expectedListOfDto = new ArrayList<>();
    }

    @Test
    void shouldConvertEntiyToDTO() {
        //given
        ETItem item1 = ETItem.builder()
                .itemId(1)
                .itemTaskContent("Verpiss ...!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .solutions(Collections.singletonList("dich"))
                .build();
        ETTask aufgabe1 = ETTask.builder()
                .taskId(4)
                .taskType(ETTaskType.ELEMENTS)
                .taskLevel(A1)
                .taskDefinition("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item1)))
                .build();
        givenAufgabenListe.add(aufgabe1);

        ETItemDto itemDto = ETItemDto.builder()
                .itemId(1+ABH)
                .itemTaskContent("Verpiss ...!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .build();

        ETTaskDto expectedDto = ETTaskDto.builder()
                .taskHash(4+ABH)
                .taskDefinition("Ergänzen Sie den Satz!")
                .level(A1)
                .items(Collections.singletonList(itemDto))
                .build();
        expectedListOfDto.add(expectedDto);

        //when
        var actualResult = underTest.convert(givenAufgabenListe,ABH);
        //then
        assertThat(actualResult).isEqualTo(expectedListOfDto);

    }

    @Test
    void shouldConvertEntiesToDTO() {
        //given
        ETItem item2 = ETItem.builder()
                .itemId(2)
                .itemTaskContent("Scheiß ..., Alta!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .solutions(Collections.singletonList("drauf"))
                .build();
        ETItemDto expectedItem2 = ETItemDto.builder()
                .itemId(2+ABH)
                .itemTaskContent("Scheiß ..., Alta!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .build();

        ETItem item5 = ETItem.builder()
                .itemId(5)
                .itemTaskContent("...wohnt in Salzburg")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Anna"))
                .build();
        ETItemDto expectedItem5 = ETItemDto.builder()
                .itemId(5+ABH)
                .itemTaskContent("...wohnt in Salzburg")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item6 = ETItem.builder()
                .itemId(6)
                .itemTaskContent("...kommt aus Graz")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Katharina"))
                .build();
        ETItemDto expectedItem6 = ETItemDto.builder()
                .itemId(6+ABH)
                .itemTaskContent("...kommt aus Graz")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item7 = ETItem.builder()
                .itemId(7)
                .itemTaskContent("...mag Musik")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Maximilian"))
                .build();
        ETItemDto expectedItem7 = ETItemDto.builder()
                .itemId(7+ABH)
                .itemTaskContent("...mag Musik")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item8 = ETItem.builder()
                .itemTaskContent("...arbeitet als Lehrerin")
                .itemId(8)
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Anna"))
                .build();
        ETItemDto expectedItem8 = ETItemDto.builder()
                .itemId(8+ABH)
                .itemTaskContent("...arbeitet als Lehrerin")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item9 = ETItem.builder()
                .itemId(9)
                .itemTaskContent("...kocht und schwimmt gern")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Katharina"))
                .build();
        ETItemDto expectedItem9 = ETItemDto.builder()
                .itemId(9+ABH)
                .itemTaskContent("...kocht und schwimmt gern")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();


        ETTask aufgabe2 = ETTask.builder()
                .taskId(8)
                .taskType(ETTaskType.ELEMENTS)
                .taskLevel(A1)
                .taskDefinition("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item2)))
                .build();
        ETTaskDto expectedAufgabeDto2 = ETTaskDto.builder()
                .taskHash(8+ABH)
                .taskDefinition("Ergänzen Sie den Satz!")
                .level(A1)
                .items(Collections.singletonList(expectedItem2))
                .build();

        ETTask aufgabe5 = ETTask.builder()
                .taskId(1)
                .taskContent("Maximilian\n" +
                        "Servus! Mein Name ist Maximilian Gruber. Ich komme aus Österreich und wohne in Wien. Ich habe eine Ausbildung zum Mechatroniker gemacht und arbeite jetzt in einer Autowerkstatt. Ich mag Autos! In der Freizeit spiele ich Fußball, fahre Rad und höre Musik. Ein Tag ohne Musik ist kein guter Tag!\n" +
                        "\n" +
                        "Anna\n" +
                        "Grüß Gott! Ich heiße Anna Moser und komme aus Österreich, aus Klagenfurt. Jetzt wohne ich in Salzburg. Ich bin Musikerin und Lehrerin. Ich unterrichte am Mozarteum. Ich mag meine Studenten, die Arbeit macht mir Spaß. Meine Hobbys sind Reisen und Lesen. Ich lese gern Romane.\n" +
                        "\n" +
                        "Katharina\n" +
                        "Guten Tag! Ich heiße Katharina Berger. Ich wohne in Linz, aber ich komme aus Graz. Ich bin Krankenschwester von Beruf und arbeite im Spital. Meine Hobbys sind kochen und Filme sehen. Ich mag auch Sport: Schwimmen tut gut! Ich schwimme zweimal pro Woche.\n")
                .taskType(ETTaskType.READING)
                .taskLevel(A1)
                .taskDefinition("Aufgaben 1-5\n" +
                        "Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?\n")
                .items(new HashSet<>(Arrays.asList(item5,item6,item7,item8,item9)))
                .build();
        ETTaskDto expectedAufgabeDto5 = ETTaskDto.builder()
                .taskHash(1+ABH)
                .taskContent("Maximilian\n" +
                        "Servus! Mein Name ist Maximilian Gruber. Ich komme aus Österreich und wohne in Wien. Ich habe eine Ausbildung zum Mechatroniker gemacht und arbeite jetzt in einer Autowerkstatt. Ich mag Autos! In der Freizeit spiele ich Fußball, fahre Rad und höre Musik. Ein Tag ohne Musik ist kein guter Tag!\n" +
                        "\n" +
                        "Anna\n" +
                        "Grüß Gott! Ich heiße Anna Moser und komme aus Österreich, aus Klagenfurt. Jetzt wohne ich in Salzburg. Ich bin Musikerin und Lehrerin. Ich unterrichte am Mozarteum. Ich mag meine Studenten, die Arbeit macht mir Spaß. Meine Hobbys sind Reisen und Lesen. Ich lese gern Romane.\n" +
                        "\n" +
                        "Katharina\n" +
                        "Guten Tag! Ich heiße Katharina Berger. Ich wohne in Linz, aber ich komme aus Graz. Ich bin Krankenschwester von Beruf und arbeite im Spital. Meine Hobbys sind kochen und Filme sehen. Ich mag auch Sport: Schwimmen tut gut! Ich schwimme zweimal pro Woche.\n")
                .taskDefinition("Aufgaben 1-5\n" +
                        "Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?\n")
                .level(A1)
                .items(Arrays.asList(expectedItem5,expectedItem6,expectedItem7,expectedItem8,expectedItem9))
                .build();

        givenAufgabenListe.addAll(Arrays.asList(aufgabe2,aufgabe5));
        expectedListOfDto.addAll(Arrays.asList(expectedAufgabeDto2,expectedAufgabeDto5));
        //when
        var actualResult = underTest.convert(givenAufgabenListe,ABH);
        //then
        assertThat(actualResult).isEqualTo(expectedListOfDto);


    }


}