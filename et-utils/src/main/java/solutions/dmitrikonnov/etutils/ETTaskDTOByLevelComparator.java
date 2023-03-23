package solutions.dmitrikonnov.etutils;

import solutions.dmitrikonnov.dto.ETTaskDto;

import java.util.Comparator;

public class ETTaskDTOByLevelComparator implements Comparator<ETTaskDto> {
    @Override
    public int compare(ETTaskDto o1, ETTaskDto o2) {
        return o1.getLevel().compareTo(o2.getLevel());
    }
}
