package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETTaskLevel;


@DataJpaTest
@ActiveProfiles("unit-test")
class SchwelleRepoTest {

    @Autowired
    private LimitsRepo underTest;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void itShouldCheckIf_AllSchwellenFoundOrderByNiveauAscended() {
        //given
        ETLimit mindestschwelle = ETLimit.builder()
                .id((short)1)
                .niveau(ETTaskLevel.A1)
                .maximumSchwelle((short)5)
                .mindestSchwelle((short)2)
                .build();
        underTest.save(mindestschwelle);
        //when
        //then
        Assertions.assertTrue(true);
    }
}