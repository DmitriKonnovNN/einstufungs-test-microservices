package solutions.dmitrikonnov.ettaskrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.dmitrikonnov.etentities.ETItem;


public interface ETItemRepo extends JpaRepository<ETItem,Integer> {

    @Modifying
    @Query ("update ETItem i set i.counterCorrectAnswers = i.counterCorrectAnswers+1 where i.itemId = :id")
    void updateCounterCorrectAnswers(@Param("id") Integer id);
}
