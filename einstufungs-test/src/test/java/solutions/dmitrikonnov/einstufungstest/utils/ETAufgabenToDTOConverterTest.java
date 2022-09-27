package solutions.dmitrikonnov.einstufungstest.utils;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.etenums.ETExerciseTyp;
import solutions.dmitrikonnov.dto.ETExerciseDto;
import solutions.dmitrikonnov.dto.ETItemDto;
import solutions.dmitrikonnov.etentities.ETExercise;
import solutions.dmitrikonnov.etentities.ETItem;
import static solutions.dmitrikonnov.etenums.ETExerciseLevel.*;



import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETAufgabenToDTOConverterTest {

    private final Faker faker = new Faker();
    private ETAufgabenToDTOConverter underTest = new ETAufgabenToDTOConverter();
    private List<ETExercise> givenAufgabenListe;
    private List<ETExerciseDto> expectedListOfDto;
    int ABH;
    long ID;

    @BeforeEach
    void setUp() {
        Random r = new Random();
        long range = 1234567L;
        ID = (long)(r.nextDouble()*range);
        ABH = faker.number().numberBetween(1,10000);
        givenAufgabenListe = new ArrayList<>();
        expectedListOfDto = new ArrayList<>();
    }

    @Test
    void shouldConvertEntiyToDTO() {
        //given
        ETItem item1 = ETItem.builder()
                .itemId(1)
                .itemAufgabenInhalt("Verpiss ...!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .loesungen(Collections.singletonList("dich"))
                .build();
        ETExercise aufgabe1 = ETExercise.builder()
                .aufgabeId(4)
                .aufgabenTyp(ETExerciseTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item1)))
                .build();
        givenAufgabenListe.add(aufgabe1);

        ETItemDto itemDto = ETItemDto.builder()
                .itemId(1+ABH)
                .itemExerciseContent("Verpiss ...!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .build();

        ETExerciseDto expectedDto = ETExerciseDto.builder()
                .execriseHash(4+ABH)
                .taskDefinition("Ergänzen Sie den Satz!")
                .niveau(A1)
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
                .itemAufgabenInhalt("Scheiß ..., Alta!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .loesungen(Collections.singletonList("drauf"))
                .build();
        ETItemDto expectedItem2 = ETItemDto.builder()
                .itemId(2+ABH)
                .itemExerciseContent("Scheiß ..., Alta!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .build();

        ETItem item5 = ETItem.builder()
                .itemId(5)
                .itemAufgabenInhalt("...wohnt in Salzburg")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Anna"))
                .build();
        ETItemDto expectedItem5 = ETItemDto.builder()
                .itemId(5+ABH)
                .itemExerciseContent("...wohnt in Salzburg")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item6 = ETItem.builder()
                .itemId(6)
                .itemAufgabenInhalt("...kommt aus Graz")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Katharina"))
                .build();
        ETItemDto expectedItem6 = ETItemDto.builder()
                .itemId(6+ABH)
                .itemExerciseContent("...kommt aus Graz")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item7 = ETItem.builder()
                .itemId(7)
                .itemAufgabenInhalt("...mag Musik")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Maximilian"))
                .build();
        ETItemDto expectedItem7 = ETItemDto.builder()
                .itemId(7+ABH)
                .itemExerciseContent("...mag Musik")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item8 = ETItem.builder()
                .itemAufgabenInhalt("...arbeitet als Lehrerin")
                .itemId(8)
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Anna"))
                .build();
        ETItemDto expectedItem8 = ETItemDto.builder()
                .itemId(8+ABH)
                .itemExerciseContent("...arbeitet als Lehrerin")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();
        ETItem item9 = ETItem.builder()
                .itemId(9)
                .itemAufgabenInhalt("...kocht und schwimmt gern")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Katharina"))
                .build();
        ETItemDto expectedItem9 = ETItemDto.builder()
                .itemId(9+ABH)
                .itemExerciseContent("...kocht und schwimmt gern")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .build();


        ETExercise aufgabe2 = ETExercise.builder()
                .aufgabeId(8)
                .aufgabenTyp(ETExerciseTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item2)))
                .build();
        ETExerciseDto expectedAufgabeDto2 = ETExerciseDto.builder()
                .execriseHash(8+ABH)
                .taskDefinition("Ergänzen Sie den Satz!")
                .niveau(A1)
                .items(Collections.singletonList(expectedItem2))
                .build();

        ETExercise aufgabe5 = ETExercise.builder()
                .aufgabeId(1)
                .aufgabenInhalt("Maximilian\n" +
                        "Servus! Mein Name ist Maximilian Gruber. Ich komme aus Österreich und wohne in Wien. Ich habe eine Ausbildung zum Mechatroniker gemacht und arbeite jetzt in einer Autowerkstatt. Ich mag Autos! In der Freizeit spiele ich Fußball, fahre Rad und höre Musik. Ein Tag ohne Musik ist kein guter Tag!\n" +
                        "\n" +
                        "Anna\n" +
                        "Grüß Gott! Ich heiße Anna Moser und komme aus Österreich, aus Klagenfurt. Jetzt wohne ich in Salzburg. Ich bin Musikerin und Lehrerin. Ich unterrichte am Mozarteum. Ich mag meine Studenten, die Arbeit macht mir Spaß. Meine Hobbys sind Reisen und Lesen. Ich lese gern Romane.\n" +
                        "\n" +
                        "Katharina\n" +
                        "Guten Tag! Ich heiße Katharina Berger. Ich wohne in Linz, aber ich komme aus Graz. Ich bin Krankenschwester von Beruf und arbeite im Spital. Meine Hobbys sind kochen und Filme sehen. Ich mag auch Sport: Schwimmen tut gut! Ich schwimme zweimal pro Woche.\n")
                .aufgabenTyp(ETExerciseTyp.READING)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Aufgaben 1-5\n" +
                        "Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?\n")
                .items(new HashSet<>(Arrays.asList(item5,item6,item7,item8,item9)))
                .build();
        ETExerciseDto expectedAufgabeDto5 = ETExerciseDto.builder()
                .execriseHash(1+ABH)
                .exerciseContent("Maximilian\n" +
                        "Servus! Mein Name ist Maximilian Gruber. Ich komme aus Österreich und wohne in Wien. Ich habe eine Ausbildung zum Mechatroniker gemacht und arbeite jetzt in einer Autowerkstatt. Ich mag Autos! In der Freizeit spiele ich Fußball, fahre Rad und höre Musik. Ein Tag ohne Musik ist kein guter Tag!\n" +
                        "\n" +
                        "Anna\n" +
                        "Grüß Gott! Ich heiße Anna Moser und komme aus Österreich, aus Klagenfurt. Jetzt wohne ich in Salzburg. Ich bin Musikerin und Lehrerin. Ich unterrichte am Mozarteum. Ich mag meine Studenten, die Arbeit macht mir Spaß. Meine Hobbys sind Reisen und Lesen. Ich lese gern Romane.\n" +
                        "\n" +
                        "Katharina\n" +
                        "Guten Tag! Ich heiße Katharina Berger. Ich wohne in Linz, aber ich komme aus Graz. Ich bin Krankenschwester von Beruf und arbeite im Spital. Meine Hobbys sind kochen und Filme sehen. Ich mag auch Sport: Schwimmen tut gut! Ich schwimme zweimal pro Woche.\n")
                .taskDefinition("Aufgaben 1-5\n" +
                        "Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?\n")
                .niveau(A1)
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