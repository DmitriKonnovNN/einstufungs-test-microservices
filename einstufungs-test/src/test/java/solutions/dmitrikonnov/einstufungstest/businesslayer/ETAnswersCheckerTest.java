package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.dto.*;
import solutions.dmitrikonnov.einstufungstest.ETAnswerSheetCheckedEvent;
import solutions.dmitrikonnov.etentities.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;

import solutions.dmitrikonnov.etenums.ETTaskLevel;
import solutions.dmitrikonnov.etenums.ETTaskType;
import solutions.dmitrikonnov.etutils.ETTasksToDTOConverter;
import solutions.dmitrikonnov.etutils.Randomizer;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static solutions.dmitrikonnov.etenums.ETTaskLevel.*;
import static solutions.dmitrikonnov.etutils.Randomizer.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETAnswersCheckerTest {

    @Mock
    private LimitsRepo mindSchwRepoMock;
    @Mock
    private ApplicationEventPublisher publisherMock;
    private ETAnswerSheetCheckedEvent expectedEvent;
    private ETAnswersChecker underTest;
    private ETAnswerSheetDto givenAnswerSheet;
    private ETTaskSheet givenCachedTaskSheet;
    private Map<Integer, List<String>> givenItemHashZuAMap;
    private List<ETTask> givenAufgabenListe = new ArrayList<>();
    private List<ETLimit> mindestschwellen;
    private ETResultsDto expectedDto;
    private ETTasksToDTOConverter converter = new ETTasksToDTOConverter();
    private List<ETTaskDto> givenTaskDTOList = new ArrayList<>();


    @BeforeEach
    void setUp() {

        int ABH = Randomizer.generate(1,1000);
        underTest = new ETAnswersChecker(publisherMock,mindSchwRepoMock);


        ETLimit schwelleA1 = ETLimit.builder().id((short)1).level(A1).minLimit((short)2).maxLimit((short)5).build();
        ETLimit schwelleA2 = ETLimit.builder().id((short)2).level(A2).minLimit((short)2).maxLimit((short)5).build();
        ETLimit schwelleB1 = ETLimit.builder().id((short)3).level(B1).minLimit((short)2).maxLimit((short)5).build();
        ETLimit schwelleB2 = ETLimit.builder().id((short)4).level(B2).minLimit((short)2).maxLimit((short)5).build();
        ETLimit schwelleC1 = ETLimit.builder().id((short)5).level(C1).minLimit((short)2).maxLimit((short)5).build();
        ETLimit schwelleC2 = ETLimit.builder().id((short)6).level(C2).minLimit((short)2).maxLimit((short)5).build();
        mindestschwellen = new ArrayList<>();
        mindestschwellen.add(schwelleA1);
        mindestschwellen.add(schwelleA2);
        mindestschwellen.add(schwelleB1);
        mindestschwellen.add(schwelleB2);
        mindestschwellen.add(schwelleC1);
        mindestschwellen.add(schwelleC2);

        ETItem item1 = ETItem.builder()
                .itemId(1)
                .itemTaskContent("Verpiss ...!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .solutions(Collections.singletonList("dich"))
                .build();

        ETItem item2 = ETItem.builder()
                .itemId(2)
                .itemTaskContent("Scheiß ..., Alta!")
                .possibleAnswers(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .solutions(Collections.singletonList("drauf"))
                .build();
        ETItem item3 = ETItem.builder()
                .itemId(3)
                .itemTaskContent("Willst du mir .. blasen, Schatz?")
                .possibleAnswers(new HashSet<>(Arrays.asList("einen Schwanz", "den Arsch", "die Eier", "einen")))
                .solutions(Collections.singletonList("einen"))
                .build();
        ETItem item4 = ETItem.builder()
                .itemId(4)
                .itemTaskContent("Du kannst du mich mal ...?")
                .possibleAnswers(new HashSet<>(Arrays.asList("kreuz und quer", "am Arsch lecken", "in den Arsch ficken", "kreuzweis")))
                .solutions(Arrays.asList("am Arsch lecken", "kreuzweis"))
                .build();


        ETItem item5 = ETItem.builder()
                .itemId(5)
                .itemTaskContent("...wohnt in Salzburg")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Anna"))
                .build();
        ETItem item6 = ETItem.builder()
                .itemId(6)
                .itemTaskContent("...kommt aus Graz")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Katharina"))
                .build();
        ETItem item7 = ETItem.builder()
                .itemId(7)
                .itemTaskContent("...mag Musik")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Maximilian"))
                .build();
        ETItem item8 = ETItem.builder()
                .itemTaskContent("...arbeitet als Lehrerin")
                .itemId(8)
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Anna"))
                .build();
        ETItem item9 = ETItem.builder()
                .itemId(8)
                .itemTaskContent("...kocht und schwimmt gern")
                .possibleAnswers(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .solutions(Collections.singletonList("Katharina"))
                .build();


        ETTask aufgabe1 = ETTask.builder()
                .taskId(4)
                .taskType(ETTaskType.ELEMENTS)
                .taskLevel(A1)
                .taskDefinition("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item1)))
                .build();
        ETTask aufgabe2 = ETTask.builder()
                .taskId(8)
                .taskType(ETTaskType.ELEMENTS)
                .taskLevel(A1)
                .taskDefinition("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item2)))
                .build();
        ETTask aufgabe3 = ETTask.builder()
                .taskId(9)
                .taskType(ETTaskType.ELEMENTS)
                .taskLevel(A2)
                .taskDefinition("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item3)))
                .build();
        ETTask aufgabe4 = ETTask.builder()
                .taskId(2)
                .taskType(ETTaskType.ELEMENTS)
                .taskLevel(A2)
                .taskDefinition("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item4)))
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

        ETItem item10 = ETItem.builder()
                .itemId(10)
                .itemTaskContent("Wie ${dropbox} heißt du?\n +" +
                        "Ich bin Katharina, Katharina Berger")
                .possibleAnswers(new HashSet<>(Arrays.asList("heißt","kommst", "gehst")))
                .solutions(Collections.singletonList("heißt"))
                .build();
        ETItem item11 = ETItem.builder()
                .itemId(11  )
                .itemTaskContent("Woher ${dropbox} du?")
                .possibleAnswers(new HashSet<>(Arrays.asList("wohnst","kommst", "gehst")))
                .solutions(Collections.singletonList("kommst"))
                .build();
        ETItem item12 = ETItem.builder()
                .itemId(12)
                .itemTaskContent("Ich {dropbox} aus Österreich, aus Graz")
                .possibleAnswers(new HashSet<>(Arrays.asList("komme", "wohne", "lebe")))
                .solutions(Collections.singletonList("komme"))
                .build();
        ETTask aufgabe6 = ETTask.builder()
                .taskId(3)
                .taskDefinition("Wählen Sie das richrige Wort")
                .taskLevel(A2)
                .items(new HashSet<>(Arrays.asList(item10,item11,item12)))
                .build();


        givenAufgabenListe.addAll(Arrays.asList(aufgabe1, aufgabe2, aufgabe3, aufgabe4, aufgabe5,aufgabe6));
        givenTaskDTOList = converter.convert(givenAufgabenListe,ABH);

        Map<Integer, List<String>> itemZuLoesungen = new HashMap<>(){{
            put(item1.getItemId(),item1.getSolutions());
            put(item2.getItemId(),item2.getSolutions());
            put(item3.getItemId(),item3.getSolutions());
            put(item4.getItemId(),item4.getSolutions());
            put(item5.getItemId(), item5.getSolutions());
            put(item6.getItemId(), item6.getSolutions());
            put(item7.getItemId(), item7.getSolutions());
            put(item8.getItemId(), item8.getSolutions());
            put(item9.getItemId(), item9.getSolutions());
            put(item10.getItemId(), item10.getSolutions());
            put(item11.getItemId(), item11.getSolutions());
            put(item12.getItemId(), item12.getSolutions());
        }};

        Map<Integer, ETTaskLevel> itemZuNiveau = new HashMap<>(){{
            put(item1.getItemId(),aufgabe1.getTaskLevel());
            put(item2.getItemId(),aufgabe2.getTaskLevel());
            put(item3.getItemId(),aufgabe3.getTaskLevel());
            put(item4.getItemId(),aufgabe4.getTaskLevel());
            put(item5.getItemId(),aufgabe5.getTaskLevel());
            put(item6.getItemId(),aufgabe5.getTaskLevel());
            put(item7.getItemId(),aufgabe5.getTaskLevel());
            put(item8.getItemId(),aufgabe5.getTaskLevel());
            put(item9.getItemId(),aufgabe5.getTaskLevel());
            put(item10.getItemId(),aufgabe6.getTaskLevel());
            put(item11.getItemId(),aufgabe6.getTaskLevel());
            put(item12.getItemId(),aufgabe6.getTaskLevel());

        }};


        givenCachedTaskSheet = new ETTaskSheet(
                ABH,
                givenTaskDTOList,
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


        givenAnswerSheet = new ETAnswerSheetDto(ABH,givenItemHashZuAMap,1);
        expectedDto = ETResultsDto.builder()
                .taskSheetHash(ABH)
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
        expectedEvent = new ETAnswerSheetCheckedEvent(underTest,ABH,expectedDto.toString());
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
        var actualResult = underTest.checkSheet(givenAnswerSheet, givenCachedTaskSheet);
        //then
        then(publisherMock)
                .should()
                .publishEvent(any(ETAnswerSheetCheckedEvent.class));
        assertThat(actualResult).isEqualTo(expectedDto);





    }

    private Integer sumHash (Integer a, Integer b) {
        return a+b;
    }
}