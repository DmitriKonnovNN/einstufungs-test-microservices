package solutions.dmitrikonnov.einstufungstest.businesslayer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.dto.ETAnswerSheetDto;
import solutions.dmitrikonnov.dto.ETExerciseDto;
import solutions.dmitrikonnov.dto.ETAnswerSheet;
import solutions.dmitrikonnov.dto.ETResultsDto;
import solutions.dmitrikonnov.etentities.*;

import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;
import solutions.dmitrikonnov.einstufungstest.utils.AntwortBogenCheckedEvent;
import solutions.dmitrikonnov.einstufungstest.utils.ETAufgabenToDTOConverter;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static solutions.dmitrikonnov.etenums.ETExerciseLevel.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETAnswersCheckerTest {

    @Mock
    private LimitsRepo mindSchwRepoMock;
    @Mock
    private ApplicationEventPublisher publisherMock;
    private AntwortBogenCheckedEvent expectedEvent;
    private final Faker faker = new Faker();
    private ETAnswersChecker underTest;
    private ETAnswerSheetDto givenAntwortBogen;
    private ETAnswerSheet givenCachedAufgabenBogen;
    private Map<Integer, List<String>> givenItemHashZuAMap;
    private List<ETExercise> givenAufgabenListe = new ArrayList<>();
    private List<ETLimit> mindestschwellen;
    private ETResultsDto expectedDto;
    private ETAufgabenToDTOConverter converter = new ETAufgabenToDTOConverter();
    private List<ETExerciseDto> givenAufgabenDTOListe = new ArrayList<>();


    @BeforeEach
    void setUp() {

        int ABH = faker.number().numberBetween(1,10000);
        underTest = new ETAnswersChecker(publisherMock,mindSchwRepoMock);


        ETLimit schwelleA1 = ETLimit.builder().id((short)1).niveau(A1).mindestSchwelle((short)2).maximumSchwelle((short)5).build();
        ETLimit schwelleA2 = ETLimit.builder().id((short)2).niveau(A2).mindestSchwelle((short)2).maximumSchwelle((short)5).build();
        ETLimit schwelleB1 = ETLimit.builder().id((short)3).niveau(B1).mindestSchwelle((short)2).maximumSchwelle((short)5).build();
        ETLimit schwelleB2 = ETLimit.builder().id((short)4).niveau(B2).mindestSchwelle((short)2).maximumSchwelle((short)5).build();
        ETLimit schwelleC1 = ETLimit.builder().id((short)5).niveau(C1).mindestSchwelle((short)2).maximumSchwelle((short)5).build();
        ETLimit schwelleC2 = ETLimit.builder().id((short)6).niveau(C2).mindestSchwelle((short)2).maximumSchwelle((short)5).build();
        mindestschwellen = new ArrayList<>();
        mindestschwellen.add(schwelleA1);
        mindestschwellen.add(schwelleA2);
        mindestschwellen.add(schwelleB1);
        mindestschwellen.add(schwelleB2);
        mindestschwellen.add(schwelleC1);
        mindestschwellen.add(schwelleC2);

        ETItem item1 = ETItem.builder()
                .itemId(1)
                .itemAufgabenInhalt("Verpiss ...!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .loesungen(Collections.singletonList("dich"))
                .build();

        ETItem item2 = ETItem.builder()
                .itemId(2)
                .itemAufgabenInhalt("Scheiß ..., Alta!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .loesungen(Collections.singletonList("drauf"))
                .build();
        ETItem item3 = ETItem.builder()
                .itemId(3)
                .itemAufgabenInhalt("Willst du mir .. blasen, Schatz?")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("einen Schwanz", "den Arsch", "die Eier", "einen")))
                .loesungen(Collections.singletonList("einen"))
                .build();
        ETItem item4 = ETItem.builder()
                .itemId(4)
                .itemAufgabenInhalt("Du kannst du mich mal ...?")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("kreuz und quer", "am Arsch lecken", "in den Arsch ficken", "kreuzweis")))
                .loesungen(Arrays.asList("am Arsch lecken", "kreuzweis"))
                .build();


        ETItem item5 = ETItem.builder()
                .itemId(5)
                .itemAufgabenInhalt("...wohnt in Salzburg")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Anna"))
                .build();
        ETItem item6 = ETItem.builder()
                .itemId(6)
                .itemAufgabenInhalt("...kommt aus Graz")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Katharina"))
                .build();
        ETItem item7 = ETItem.builder()
                .itemId(7)
                .itemAufgabenInhalt("...mag Musik")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Maximilian"))
                .build();
        ETItem item8 = ETItem.builder()
                .itemAufgabenInhalt("...arbeitet als Lehrerin")
                .itemId(8)
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Anna"))
                .build();
        ETItem item9 = ETItem.builder()
                .itemId(8)
                .itemAufgabenInhalt("...kocht und schwimmt gern")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Katharina"))
                .build();


        ETExercise aufgabe1 = ETExercise.builder()
                .aufgabeId(4)
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item1)))
                .build();
        ETExercise aufgabe2 = ETExercise.builder()
                .aufgabeId(8)
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item2)))
                .build();
        ETExercise aufgabe3 = ETExercise.builder()
                .aufgabeId(9)
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A2)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item3)))
                .build();
        ETExercise aufgabe4 = ETExercise.builder()
                .aufgabeId(2)
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A2)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item4)))
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
                .aufgabenTyp(ETAufgabenTyp.LESEN)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Aufgaben 1-5\n" +
                        "Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?\n")
                .items(new HashSet<>(Arrays.asList(item5,item6,item7,item8,item9)))
                .build();

        ETItem item10 = ETItem.builder()
                .itemId(10)
                .itemAufgabenInhalt("Wie ${dropbox} heißt du?\n +" +
                        "Ich bin Katharina, Katharina Berger")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("heißt","kommst", "gehst")))
                .loesungen(Collections.singletonList("heißt"))
                .build();
        ETItem item11 = ETItem.builder()
                .itemId(11  )
                .itemAufgabenInhalt("Woher ${dropbox} du?")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("wohnst","kommst", "gehst")))
                .loesungen(Collections.singletonList("kommst"))
                .build();
        ETItem item12 = ETItem.builder()
                .itemId(12)
                .itemAufgabenInhalt("Ich {dropbox} aus Österreich, aus Graz")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("komme", "wohne", "lebe")))
                .loesungen(Collections.singletonList("komme"))
                .build();
        ETExercise aufgabe6 = ETExercise.builder()
                .aufgabeId(3)
                .aufgabenStellung("Wählen Sie das richrige Wort")
                .aufgabenNiveau(A2)
                .items(new HashSet<>(Arrays.asList(item10,item11,item12)))
                .build();


        givenAufgabenListe.addAll(Arrays.asList(aufgabe1, aufgabe2, aufgabe3, aufgabe4, aufgabe5,aufgabe6));
        givenAufgabenDTOListe = converter.convert(givenAufgabenListe,ABH);

        Map<Integer, List<String>> itemZuLoesungen = new HashMap<>(){{
            put(item1.getItemId(),item1.getLoesungen());
            put(item2.getItemId(),item2.getLoesungen());
            put(item3.getItemId(),item3.getLoesungen());
            put(item4.getItemId(),item4.getLoesungen());
            put(item5.getItemId(), item5.getLoesungen());
            put(item6.getItemId(), item6.getLoesungen());
            put(item7.getItemId(), item7.getLoesungen());
            put(item8.getItemId(), item8.getLoesungen());
            put(item9.getItemId(), item9.getLoesungen());
            put(item10.getItemId(), item10.getLoesungen());
            put(item11.getItemId(), item11.getLoesungen());
            put(item12.getItemId(), item12.getLoesungen());
        }};

        Map<Integer, ETExerciseLevel> itemZuNiveau = new HashMap<>(){{
            put(item1.getItemId(),aufgabe1.getAufgabenNiveau());
            put(item2.getItemId(),aufgabe2.getAufgabenNiveau());
            put(item3.getItemId(),aufgabe3.getAufgabenNiveau());
            put(item4.getItemId(),aufgabe4.getAufgabenNiveau());
            put(item5.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item6.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item7.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item8.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item9.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item10.getItemId(),aufgabe6.getAufgabenNiveau());
            put(item11.getItemId(),aufgabe6.getAufgabenNiveau());
            put(item12.getItemId(),aufgabe6.getAufgabenNiveau());

        }};


        /*List<ETAufgabenNiveau> moeglicheNiveaus = Arrays.asList(A1,A2,B1,B2,C1,C2);
        List<String> antwortenZumRandomisieren = Arrays.asList("A", "B", "C", "D");*/




        //TODO: fill both lists!
       /* for (int i = 0; i < 10; i++ ){
            ETAufgabe aufgabe = ETAufgabe.builder()
                    .aufgabeId(faker.number().numberBetween(1,10000))
                    .aufgabenNiveau(moeglicheNiveaus.get(r.nextInt(moeglicheNiveaus.size())))
                    .moeglicheAntworten(new HashSet<>(Collections.singleton(
                            antwortenZumRandomisieren.get(r.nextInt(antwortenZumRandomisieren.size())))))
                    .build();
            //givenAufgabenListe.a
        }
*/
        givenCachedAufgabenBogen = new ETAnswerSheet(
                ABH,
                givenAufgabenDTOListe,
                System.currentTimeMillis(),
                itemZuLoesungen,
                itemZuNiveau );


        givenItemHashZuAMap = new HashMap<>(){{
            put(sumHash(item4.getItemId(),ABH), new ArrayList<>(Collections.singleton("in den Arsch ficken")));
            put(sumHash(item1.getItemId(),ABH),new ArrayList<>(Collections.singleton("dich")));
            put(sumHash(item2.getItemId(), ABH),new ArrayList<>(Collections.singleton("drauf")));
            put(sumHash(item3.getItemId(),ABH),new ArrayList<>(Arrays.asList("die Eier", "einen Schwanz")));
            put(sumHash(item5.getItemId(),ABH),new ArrayList<>(Collections.singletonList("Anna"))); // correct
            put(sumHash(item6.getItemId(),ABH),new ArrayList<>(Collections.singletonList(""))); // false
            put(sumHash(item7.getItemId(),ABH),new ArrayList<>(Arrays.asList("Anna", "Katharina"))); // two false
            put(sumHash(item8.getItemId(),ABH),new ArrayList<>(Arrays.asList("Maximilian", "Anna"))); // false: one false one right
            put(sumHash(item9.getItemId(),ABH),new ArrayList<>(Collections.singletonList("Katharina"))); // correct
            put(sumHash(item10.getItemId(),ABH),new ArrayList<>(Collections.singletonList("heißt"))); // correct
            put(sumHash(item11.getItemId(),ABH),new ArrayList<>(Collections.singletonList("kommst"))); // correct
            put(sumHash(item12.getItemId(),ABH),new ArrayList<>(Collections.singletonList("wohne"))); // false


        }};


        givenAntwortBogen = new ETAnswerSheetDto(ABH,givenItemHashZuAMap,1);
        expectedDto = ETResultsDto.builder()
                .exerciseSetHash(ABH)
                .numberCorrectAnswers((short)6)
                .idToCorrectnessMap(new HashMap<>(){{
                   put(item1.getItemId(),true);
                   put(item2.getItemId(),true);
                   put(item3.getItemId(),false);
                   put(item4.getItemId(),false);
                   put(item5.getItemId(),true);
                   put(item6.getItemId(),false);
                   put(item7.getItemId(),false);
                   put(item8.getItemId(),false);
                   put(item9.getItemId(),true);
                   put(item10.getItemId(),true);
                   put(item11.getItemId(),true);
                   put(item12.getItemId(),false);
                }})
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A2,A2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        //TODO: check that the correct event has been thrown
        expectedEvent = new AntwortBogenCheckedEvent(underTest,ABH,expectedDto.toString());
    }

    @AfterEach
    void tearDown() {
    }

    //@RepeatedTest(3)
    @Test
    void shouldcheckBogen() {
        //given

        given(mindSchwRepoMock.findAllByOrderByLevel()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.checkSheet(givenAntwortBogen, givenCachedAufgabenBogen);
        //then
        then(publisherMock)
                .should()
                .publishEvent(any(AntwortBogenCheckedEvent.class));
        assertThat(actualResult).isEqualTo(expectedDto);





    }

    private Integer sumHash (Integer a, Integer b) {
        return a+b;
    }
}