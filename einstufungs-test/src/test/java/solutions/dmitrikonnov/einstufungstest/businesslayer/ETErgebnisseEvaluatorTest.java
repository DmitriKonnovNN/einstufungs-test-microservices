package solutions.dmitrikonnov.einstufungstest.businesslayer;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import solutions.dmitrikonnov.dto.ETErgebnisseDto;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.SchwellenRepo;
import solutions.dmitrikonnov.etentities.ETSchwelle;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static solutions.dmitrikonnov.etenums.ETAufgabenNiveau.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETErgebnisseEvaluatorTest {

    @Mock
    private SchwellenRepo mindSchwRepoMock;
    private static List<ETSchwelle> mindestschwellen2;
    private static List<ETSchwelle> mindestschwellen3;
    private ETErgebnisseEvaluator underTest;
    private ETErgebnisseDto passedDto;
    private ETErgebnisseDto expectedDto;

    private int ABH;

    @BeforeAll
    static void setUpMindestSchwelle() {
        ETSchwelle schwelleA1 = ETSchwelle.builder().id((short)1).niveau(A1).mindestSchwelle((short)2).maximumSchwelle((short)4).build();
        ETSchwelle schwelleA2 = ETSchwelle.builder().id((short)2).niveau(A2).mindestSchwelle((short)2).maximumSchwelle((short)4).build();
        ETSchwelle schwelleB1 = ETSchwelle.builder().id((short)3).niveau(B1).mindestSchwelle((short)2).maximumSchwelle((short)4).build();
        ETSchwelle schwelleB2 = ETSchwelle.builder().id((short)4).niveau(B2).mindestSchwelle((short)2).maximumSchwelle((short)4).build();
        ETSchwelle schwelleC1 = ETSchwelle.builder().id((short)5).niveau(C1).mindestSchwelle((short)2).maximumSchwelle((short)4).build();
        ETSchwelle schwelleC2 = ETSchwelle.builder().id((short)6).niveau(C2).mindestSchwelle((short)2).maximumSchwelle((short)4).build();

        mindestschwellen2 = new ArrayList<>();
        mindestschwellen2.add(schwelleA1);
        mindestschwellen2.add(schwelleA2);
        mindestschwellen2.add(schwelleB1);
        mindestschwellen2.add(schwelleB2);
        mindestschwellen2.add(schwelleC1);
        mindestschwellen2.add(schwelleC2);

        ETSchwelle schwelle3A1 = ETSchwelle.builder().id((short)1).niveau(A1).mindestSchwelle((short)3).maximumSchwelle((short)5).build();
        ETSchwelle schwelle3A2 = ETSchwelle.builder().id((short)2).niveau(A2).mindestSchwelle((short)3).maximumSchwelle((short)5).build();
        ETSchwelle schwelle3B1 = ETSchwelle.builder().id((short)3).niveau(B1).mindestSchwelle((short)3).maximumSchwelle((short)5).build();
        ETSchwelle schwelle3B2 = ETSchwelle.builder().id((short)4).niveau(B2).mindestSchwelle((short)3).maximumSchwelle((short)5).build();
        ETSchwelle schwelle3C1 = ETSchwelle.builder().id((short)5).niveau(C1).mindestSchwelle((short)3).maximumSchwelle((short)5).build();
        ETSchwelle schwelle3C2 = ETSchwelle.builder().id((short)6).niveau(C2).mindestSchwelle((short)3).maximumSchwelle((short)5).build();

        mindestschwellen3 = new ArrayList<>();
        mindestschwellen3.add(schwelle3A1);
        mindestschwellen3.add(schwelle3A2);
        mindestschwellen3.add(schwelle3B1);
        mindestschwellen3.add(schwelle3B2);
        mindestschwellen3.add(schwelle3C1);
        mindestschwellen3.add(schwelle3C2);

    }

    @BeforeEach
    void setUp() {
        underTest = new ETErgebnisseEvaluator(mindSchwRepoMock);

        Faker faker = new Faker();
        ABH = faker.number().numberBetween(1,10000);

    }


    @RepeatedTest(value = 2)
    void evaluate2_reachedLevel_shouldBe_A0_if_noneCorrect() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)0)
                .RichtigeLoesungenNachNiveau(Collections.singletonList(null))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A0);
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate3_reachedLevel_shouldBe_A1_1_ifOnlyA1Erreicht (){
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-3")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)7)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A2,B1,B2,C1,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)2);
            put(A2,(short)1);
            put(B1,(short)1);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)1);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate4_reachedLevel_shouldBe_A1_1_ifCorrect_2_0_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-4")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)6)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,B1,B1,B2,C1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)2); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)0);
            put(B1,(short)2);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate5_reachedLevel_shouldBe_A2_1_ifCorrect_2_1_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-5")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)7)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A2,B1,B1,B2,C1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)2); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)1);
            put(B1,(short)2);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate6_reachedLevel_shouldBe_A0_ifOnlyOneCorrect() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-6")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)7)
                .RichtigeLoesungenNachNiveau(Collections.singletonList(A1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A0);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)1); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)0);
            put(B1,(short)0);
            put(B2,(short)0);
            put(C1,(short)0);
            put(C2,(short)0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate6_reachedLevel_shouldBe_A2_1_ifCorrect_3_0_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-6")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)8)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,B1,B1,B1,B2,C1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)3); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)0);
            put(B1,(short)3);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate7_reachedLevel_shouldBe_BA2_ifCorrect_3_1_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-7")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)8)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,B1,B1,B1,B2,C1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)3); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)1);
            put(B1,(short)3);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate8_reachedLevel_shouldBe_A1_1_evenIfCorrect_2_1_1_0_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-8")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)8)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A2,B1,C1,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)2); // Knapp erreicht = 50 % (die Mindestschwelle ist 2 richtige Lösungen)
            put(A2,(short)1); // Niveau nicht erreicht = unter 50 %
            put(B1,(short)1); // Niveau nicht erreicht = unter 50 %
            put(B2,(short)0); // Von hier an werden keine Antworten mehr berücksichtigt
            put(C1,(short)2); // ENDERGEBNIS: A1
            put(C2,(short)2);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate9_reachedLevel_shouldBe_B2_1_if_3_2_2_1_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-9")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)12)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,B1,B1,B2,C1,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)3); // erreicht = 75 % (die Mindestschwelle = 2 richtige Lösungen)
            put(A2,(short)2); // knapp erreicht = genau 50 %
            put(B1,(short)2); // knapp erreicht = genau 50 %
            put(B2,(short)1); // Niveau nicht erreicht = unter 50 %
            put(C1,(short)2); // knapp erreicht = genau 50 %
            put(C2,(short)2); // // knapp erreicht = genau 50 %
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate10_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_4_3_3_2_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-10")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)16)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B2,B2,C1,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (2/5)
            put(C2,(short)2); // nicht erreicht =       40 % (2/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate11_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_4_3_3_2_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-11")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)16)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B2,B2,C1,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (2/5)
            put(C2,(short)2); // nicht erreicht =       40 % (2/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate12_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_3_3_1_3_3_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-12")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)16)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,B2,C1,C1,C1,C2,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)1); // nicht erreicht =       20 % (1/5)
            put(B2,(short)3); // knapp erreicht =       60 % (2/5)
            put(C1,(short)3); // knapp erreicht =       60 % (3/5)
            put(C2,(short)3); // knapp erreicht =       60 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }



    @RepeatedTest(value = 2)
    void evaluate13_given_minLevel_3_reachedLevel_shouldBe_A2_evenIf_3_3_1_2_3_5() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-13")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)17)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,C1,C1,C1,C2,C2,C2,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)1); // nicht erreicht =       20 % (1/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)3); // knapp erreicht =       60 % (3/5)
            put(C2,(short)5); // max erreicht aber egal = 100 % (5/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate14_given_minLevel_3_reachedLevel_shouldBe_B1_if_4_4_3_1_1_0() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-14")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)13)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,A2,B1,B1,B1,B2,C1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)4); // erreicht =             80 % (4/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)1); // nicht erreicht =       20 % (1/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)0); // nicht erreicht =       0 % (0/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate15_given_minLevel_3_reachedLevel_shouldBe_A2_1_if_4_2_3_1_0_0() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-15")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)12)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A2,A2,B1,B1,B1,B2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)2); // nicht erreicht =       40 % (2/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)1); // nicht erreicht =       20 % (1/5)
            put(C1,(short)0); // nicht erreicht =       0 % (0/5)
            put(C2,(short)0); // nicht erreicht =       0 % (0/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());


    }
    @RepeatedTest(value = 2)
    void evaluate16_given_minLevel_3_reachedLevel_shouldBe_A2_1_if_3_3_1_2_3_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-16")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)15)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,C1,C1,C1,C2,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)1); // nicht erreicht =       20 % (1/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)3); // knapp erreicht =       60 % (3/5)
            put(C2,(short)3); // knapp erreicht =       60 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate17_given_minLevel_3_reachedLevel_shouldBe_A2_if_3_5_2_2_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-17")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)16)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,A2,A2,A2,B1,B1,B2,B2,C1,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)2); // nicht erreicht =       40 % (2/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (3/5)
            put(C2,(short)2); // knicht erreicht =      40 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate18_given_minLevel_3_reachedLevel_shouldBe_A1_2_if_0_5_2_2_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-18")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)13)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A2,A2,A2,A2,A2,B1,B1,B2,B2,C1,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)0); // alle falsch =    genau 0 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)2); // nicht erreicht =       40 % (2/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (3/5)
            put(C2,(short)2); // knicht erreicht =      40 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate19_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_5_5_3_2_1_1() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-19")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)18)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,A2,A2,B1,B1,B1,B2,B2,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)3); // knapp erreicht =       60 % (3/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)2); // nicht erreicht =       40 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate20_given_minLevel_3_reachedLevel_shouldBe_B1_2_if_5_5_4_2_1_1() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-20")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)19)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,A2,A2,B1,B1,B1,B1,B2,B2,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)4); // erreicht =             80 % (4/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)2); // nicht erreicht =       40 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate21_given_minLevel_3_reachedLevel_shouldBe_B1_2_if_5_4_4_2_1_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-21")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)18)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,A2,B1,B1,B1,B1,B2,B2,C1,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)4); //  erreicht  =           80 % (4/5)
            put(B1,(short)4); // erreicht =             80 % (4/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)2); // nicht erreicht =       40 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate22_given_minLevel_3_reachedLevel_shouldBe_B1_2_if_5_3_4_2_1_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-22")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)18)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B1,B2,B2,C1,C2,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B1_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht  =      60 % (3/5)
            put(B1,(short)4); // erreicht =             80 % (4/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)3); // nicht erreicht =       40 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate23_given_minLevel_3_reachedLevel_shouldBe_A1_if_5_1_1_5_1_4() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-23")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)17)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A1,A2,B1,B2,B2,B2,B2,B2,C1,C2,C2,C2,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)1); // nicht erreicht  =      20 % (1/5)
            put(B1,(short)1); // nicht erreicht =        80 % (1/5)
            put(B2,(short)5); // alle richtig aber egal =40 % (5/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)4); // erreicht aber egal =      80 % (4/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate24_given_minLevel_3_reachedLevel_shouldBe_B2_1_if_4_1_5_3_1_1() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-24")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten((short)15)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A2,B1,B1,B1,B1,B1,B2,B2,B2,C1,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(B2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,(short)4); //  erreicht  =       alle 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)1); // nicht erreicht  =      20 % (1/5)
            put(B1,(short)5); // alle richtig =        100 % (5/5)
            put(B2,(short)3); // knapp erreicht  =60 % (3/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)1); // nciht erreicht =      20 % (1/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
}