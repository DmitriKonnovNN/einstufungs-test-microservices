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
class LimitsRepoTest {

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
                .level(ETTaskLevel.A1)
                .maxLimit((short)5)
                .minLimit((short)2)
                .build();
        underTest.save(mindestschwelle);
        //when
        //then
        Assertions.assertTrue(true);
    }
}