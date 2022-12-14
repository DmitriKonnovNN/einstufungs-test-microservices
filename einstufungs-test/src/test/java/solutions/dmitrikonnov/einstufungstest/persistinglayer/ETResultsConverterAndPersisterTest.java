package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.dto.ETResultsDto;
import solutions.dmitrikonnov.etentities.ETResults;
import solutions.dmitrikonnov.etenums.ETTaskLevel;
import solutions.dmitrikonnov.etresultsrepo.ETResultsRepo;
import solutions.dmitrikonnov.etutils.Randomizer;


import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static solutions.dmitrikonnov.etenums.ETTaskLevel.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETResultsConverterAndPersisterTest {

    @Mock
    private ETResultsRepo repoMock;
    private ETResultsConverterAndPersister underTest;
    private ETResultsDto dtoGiven;
    private ETResults entityExpected;


    @BeforeEach
    void setUp() {
//        int leftLimit = 48; // numeral '0'
//        int rightLimit = 122; // letter 'z'
//        int targetStringLength = 10;
//        Random random = new Random();
//        String generatedString = random.ints(leftLimit, rightLimit + 1)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                .limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                .toString();
        UUID ID = UUID.randomUUID();
        Integer ABH = Randomizer.generate(1,1000);
        ETTaskLevel MEN = A2;
        Short ZR = 5;
        List<ETTaskLevel> RLNN = new ArrayList<>(List.of(A1, A1, A2, A2, B1));
        Map<Integer, Boolean> IDZUR = new HashMap<>(){{
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);
            put(Randomizer.generate(1,1000),true);

        }};
        Map<ETTaskLevel, Short> NZZR = new HashMap<>(){{
            put(A1,(short)2);
            put(A2,(short)2);
            put(B1,(short)1);
            put(B2,(short)0);
            put(C1,(short)0);
            put(C2,(short)0);
        }};
        Map<String, Short> NZZRSTRING = new HashMap<>(){{
            put("A1",(short)2);
            put("A2",(short)2);
            put("B1",(short)1);
            put("B2",(short)0);
            put("C1",(short)0);
            put("C2",(short)0);
        }};

        dtoGiven = ETResultsDto.builder()
                .id(ID.toString())
                .taskSheetHash(ABH)
                .maxReachedLevel(MEN)
                .numberCorrectAnswers(ZR)
                .correctAnswersPerLevel(RLNN)
                .idToCorrectnessMap(IDZUR)
                .levelToNumberOfCorrect(NZZR)
                .build();
        underTest = new ETResultsConverterAndPersister(repoMock);

        entityExpected = ETResults.builder()
                .id(ID)
                .taskSheetHash(ABH)
                .maxReachedLevel(MEN)
                .numberCorrectAnswers(ZR)
                .idToCorrectnessMap(IDZUR)
                .levelToNumberCorrect(NZZRSTRING)
                .build();


    }

    @AfterEach
    void tearDown() {

    }

    @RepeatedTest(1)
    void shouldConvertAndPersist() {
        //given see setUp();
        BDDMockito.given(repoMock.save(any())).willReturn(entityExpected);
        //when
        underTest.convertAndPersist(dtoGiven);
        //then
        ArgumentCaptor<ETResults> entityArgumentCaptor = ArgumentCaptor.forClass(ETResults.class);
        verify(repoMock).save(entityArgumentCaptor.capture());
        ETResults result = entityArgumentCaptor.getValue();
        assertThat(entityExpected).isEqualTo(result);

    }
}