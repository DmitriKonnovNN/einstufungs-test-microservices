package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import solutions.dmitrikonnov.dto.ETResultsDto;
import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etlimitsrepo.EtLimitsRepo;
import solutions.dmitrikonnov.etutils.Randomizer;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static solutions.dmitrikonnov.etenums.ETTaskLevel.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETResultsEvaluatorTest {

    @Mock
    private EtLimitsRepo minLimitRepoMock;
    private static List<ETLimit> minLimits2;
    private static List<ETLimit> minLimits3;
    private ETResultsEvaluator underTest;
    private ETResultsDto passedDto;
    private ETResultsDto expectedDto;

    private int ABH;

    @BeforeAll
    static void setUpMindestSchwelle() {
        ETLimit limA1 = ETLimit.builder().id((short)1).level(A1).minLimit((short)2).maxLimit((short)4).build();
        ETLimit limA2 = ETLimit.builder().id((short)2).level(A2).minLimit((short)2).maxLimit((short)4).build();
        ETLimit limB1 = ETLimit.builder().id((short)3).level(B1).minLimit((short)2).maxLimit((short)4).build();
        ETLimit limB2 = ETLimit.builder().id((short)4).level(B2).minLimit((short)2).maxLimit((short)4).build();
        ETLimit limC1 = ETLimit.builder().id((short)5).level(C1).minLimit((short)2).maxLimit((short)4).build();
        ETLimit limC2 = ETLimit.builder().id((short)6).level(C2).minLimit((short)2).maxLimit((short)4).build();

        minLimits2 = new ArrayList<>();
        minLimits2.add(limA1);
        minLimits2.add(limA2);
        minLimits2.add(limB1);
        minLimits2.add(limB2);
        minLimits2.add(limC1);
        minLimits2.add(limC2);

        ETLimit lim3A1 = ETLimit.builder().id((short)1).level(A1).minLimit((short)3).maxLimit((short)5).build();
        ETLimit lim3A2 = ETLimit.builder().id((short)2).level(A2).minLimit((short)3).maxLimit((short)5).build();
        ETLimit lim3B1 = ETLimit.builder().id((short)3).level(B1).minLimit((short)3).maxLimit((short)5).build();
        ETLimit lim3B2 = ETLimit.builder().id((short)4).level(B2).minLimit((short)3).maxLimit((short)5).build();
        ETLimit lim3C1 = ETLimit.builder().id((short)5).level(C1).minLimit((short)3).maxLimit((short)5).build();
        ETLimit lim3C2 = ETLimit.builder().id((short)6).level(C2).minLimit((short)3).maxLimit((short)5).build();

        minLimits3 = new ArrayList<>();
        minLimits3.add(lim3A1);
        minLimits3.add(lim3A2);
        minLimits3.add(lim3B1);
        minLimits3.add(lim3B2);
        minLimits3.add(lim3C1);
        minLimits3.add(lim3C2);

    }

    @BeforeEach
    void setUp() {
        underTest = new ETResultsEvaluator(minLimitRepoMock);

        ABH = Randomizer.generate(1,1000);
    }


    @RepeatedTest(value = 2)
    void evaluate2_reachedLevel_shouldBe_A0_if_noneCorrect() {
        //given
        passedDto = ETResultsDto.builder()
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)0)
                .correctAnswersPerLevel(Collections.singletonList(null))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A0);
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate3_reachedLevel_shouldBe_A1_1_ifOnlyA1Erreicht (){
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-3")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)7)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A2,B1,B2,C1,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)2);
            put(A2,(short)1);
            put(B1,(short)1);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)1);
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate4_reachedLevel_shouldBe_A1_1_ifCorrect_2_0_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-4")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)6)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,B1,B1,B2,C1))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)2); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)0);
            put(B1,(short)2);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate5_reachedLevel_shouldBe_A2_1_ifCorrect_2_1_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-5")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)7)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A2,B1,B1,B2,C1))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A2_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)2); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)1);
            put(B1,(short)2);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate6_reachedLevel_shouldBe_A0_ifOnlyOneCorrect() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-6")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)7)
                .correctAnswersPerLevel(Collections.singletonList(A1))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A0);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)1); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)0);
            put(B1,(short)0);
            put(B2,(short)0);
            put(C1,(short)0);
            put(C2,(short)0);
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate6_reachedLevel_shouldBe_A2_1_ifCorrect_3_0_3() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-6")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)8)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,B1,B1,B1,B2,C1))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A2_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)3); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)0);
            put(B1,(short)3);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate7_reachedLevel_shouldBe_BA2_ifCorrect_3_1_3() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-7")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)8)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A2,B1,B1,B1,B2,C1))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A2);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)3); // Zahl richtiger Antworten je nach Niveau
            put(A2,(short)1);
            put(B1,(short)3);
            put(B2,(short)1);
            put(C1,(short)1);
            put(C2,(short)0);
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate8_reachedLevel_shouldBe_A1_1_evenIfCorrect_2_1_1_0_2_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-8")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)8)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A2,B1,C1,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)2); // Knapp erreicht = 50 % (die Mindestschwelle ist 2 richtige Lösungen)
            put(A2,(short)1); // Niveau nicht erreicht = unter 50 %
            put(B1,(short)1); // Niveau nicht erreicht = unter 50 %
            put(B2,(short)0); // Von hier an werden keine Antworten mehr berücksichtigt
            put(C1,(short)2); // ENDERGEBNIS: A1
            put(C2,(short)2);
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @RepeatedTest(value = 2)
    void evaluate9_reachedLevel_shouldBe_B2_1_if_3_2_2_1_2_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-9")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)12)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A2,A2,B1,B1,B2,C1,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B2_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)3); // erreicht = 75 % (die Mindestschwelle = 2 richtige Lösungen)
            put(A2,(short)2); // knapp erreicht = genau 50 %
            put(B1,(short)2); // knapp erreicht = genau 50 %
            put(B2,(short)1); // Niveau nicht erreicht = unter 50 %
            put(C1,(short)2); // knapp erreicht = genau 50 %
            put(C2,(short)2); // // knapp erreicht = genau 50 %
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate10_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_4_3_3_2_2_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-10")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)16)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B2,B2,C1,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (2/5)
            put(C2,(short)2); // nicht erreicht =       40 % (2/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate11_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_4_3_3_2_2_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-11")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)16)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B2,B2,C1,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (2/5)
            put(C2,(short)2); // nicht erreicht =       40 % (2/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate12_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_3_3_1_3_3_3() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-12")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)16)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,B2,C1,C1,C1,C2,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)1); // nicht erreicht =       20 % (1/5)
            put(B2,(short)3); // knapp erreicht =       60 % (2/5)
            put(C1,(short)3); // knapp erreicht =       60 % (3/5)
            put(C2,(short)3); // knapp erreicht =       60 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }



    @RepeatedTest(value = 2)
    void evaluate13_given_minLevel_3_reachedLevel_shouldBe_A2_evenIf_3_3_1_2_3_5() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-13")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)17)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,C1,C1,C1,C2,C2,C2,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A2_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)1); // nicht erreicht =       20 % (1/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)3); // knapp erreicht =       60 % (3/5)
            put(C2,(short)5); // max erreicht aber egal = 100 % (5/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate14_given_minLevel_3_reachedLevel_shouldBe_B1_if_4_4_3_1_1_0() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-14")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)13)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,A2,B1,B1,B1,B2,C1))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)4); // erreicht =             80 % (4/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)1); // nicht erreicht =       20 % (1/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)0); // nicht erreicht =       0 % (0/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate15_given_minLevel_3_reachedLevel_shouldBe_A2_1_if_4_2_3_1_0_0() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-15")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)12)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A2,A2,B1,B1,B1,B2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A2_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)2); // nicht erreicht =       40 % (2/5)
            put(B1,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B2,(short)1); // nicht erreicht =       20 % (1/5)
            put(C1,(short)0); // nicht erreicht =       0 % (0/5)
            put(C2,(short)0); // nicht erreicht =       0 % (0/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());


    }
    @RepeatedTest(value = 2)
    void evaluate16_given_minLevel_3_reachedLevel_shouldBe_A2_1_if_3_3_1_2_3_3() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-16")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)15)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,C1,C1,C1,C2,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A2_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht = genau 60 % (3/5)
            put(B1,(short)1); // nicht erreicht =       20 % (1/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)3); // knapp erreicht =       60 % (3/5)
            put(C2,(short)3); // knapp erreicht =       60 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate17_given_minLevel_3_reachedLevel_shouldBe_A2_if_3_5_2_2_2_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-17")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)16)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A2,A2,A2,A2,A2,B1,B1,B2,B2,C1,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A2);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)2); // nicht erreicht =       40 % (2/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (3/5)
            put(C2,(short)2); // knicht erreicht =      40 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate18_given_minLevel_3_reachedLevel_shouldBe_A1_2_if_0_5_2_2_2_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-18")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)13)
                .correctAnswersPerLevel(Arrays.asList(A2,A2,A2,A2,A2,B1,B1,B2,B2,C1,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A1_2);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)0); // alle falsch =    genau 0 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)2); // nicht erreicht =       40 % (2/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)2); // nicht erreicht =       40 % (3/5)
            put(C2,(short)2); // knicht erreicht =      40 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate19_given_minLevel_3_reachedLevel_shouldBe_B1_1_if_5_5_3_2_1_1() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-19")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)18)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,A2,A2,B1,B1,B1,B2,B2,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)3); // knapp erreicht =       60 % (3/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)2); // nicht erreicht =       40 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate20_given_minLevel_3_reachedLevel_shouldBe_B1_2_if_5_5_4_2_1_1() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-20")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)19)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,A2,A2,B1,B1,B1,B1,B2,B2,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_2);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)5); // alle richtig =   genau 100 % (5/5)
            put(B1,(short)4); // erreicht =             80 % (4/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)2); // nicht erreicht =       40 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate21_given_minLevel_3_reachedLevel_shouldBe_B1_2_if_5_4_4_2_1_2() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-21")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)18)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,A2,B1,B1,B1,B1,B2,B2,C1,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_2);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)4); //  erreicht  =           80 % (4/5)
            put(B1,(short)4); // erreicht =             80 % (4/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)2); // nicht erreicht =       40 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate22_given_minLevel_3_reachedLevel_shouldBe_B1_2_if_5_3_4_2_1_3() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-22")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)18)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B1,B2,B2,C1,C2,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B1_2);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)3); // knapp erreicht  =      60 % (3/5)
            put(B1,(short)4); // erreicht =             80 % (4/5)
            put(B2,(short)2); // nicht erreicht =       40 % (2/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)3); // nicht erreicht =       40 % (3/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }

    @RepeatedTest(value = 2)
    void evaluate23_given_minLevel_3_reachedLevel_shouldBe_A1_if_5_1_1_5_1_4() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-23")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)17)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A1,A2,B1,B2,B2,B2,B2,B2,C1,C2,C2,C2,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(A1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)5); // alle richtig =    alle 100 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)1); // nicht erreicht  =      20 % (1/5)
            put(B1,(short)1); // nicht erreicht =        80 % (1/5)
            put(B2,(short)5); // alle richtig aber egal =40 % (5/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)4); // erreicht aber egal =      80 % (4/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }
    @RepeatedTest(value = 2)
    void evaluate24_given_minLevel_3_reachedLevel_shouldBe_B2_1_if_4_1_5_3_1_1() {
        //given
        passedDto = ETResultsDto.builder()
                .id("PASSED-DTO-ID-24")
                .taskSheetHash(ABH)
                .numberCorrectAnswers((short)15)
                .correctAnswersPerLevel(Arrays.asList(A1,A1,A1,A1,A2,B1,B1,B1,B1,B1,B2,B2,B2,C1,C2))
                .levelToNumberOfCorrect(new HashMap<>(){{
                    put(A1,(short)0);
                    put(A2,(short)0);
                    put(B1,(short)0);
                    put(B2,(short)0);
                    put(C1,(short)0);
                    put(C2,(short)0);
                }})
                .build();

        expectedDto = new ETResultsDto(passedDto);
        expectedDto.setMaxReachedLevel(B2_1);
        expectedDto.setLevelToNumberOfCorrect(new HashMap<>(){{
            put(A1,(short)4); //  erreicht  =       alle 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,(short)1); // nicht erreicht  =      20 % (1/5)
            put(B1,(short)5); // alle richtig =        100 % (5/5)
            put(B2,(short)3); // knapp erreicht  =60 % (3/5)
            put(C1,(short)1); // nicht erreicht =       20 % (1/5)
            put(C2,(short)1); // nciht erreicht =      20 % (1/5)
        }});
        given(minLimitRepoMock.findAllByOrderByLevel()).willReturn(minLimits3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxReachedLevel());
        log.debug(passedDto.toString());

    }
}