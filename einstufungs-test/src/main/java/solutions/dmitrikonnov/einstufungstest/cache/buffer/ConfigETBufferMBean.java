package solutions.dmitrikonnov.einstufungstest.cache.buffer;

import lombok.AllArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ManagedResource (description = "Task Buffer Config Bean")
public class ConfigETBufferMBean {

    private final ETBuffer buffer;

    @ManagedAttribute (description = "get bufferMaxSize")
    public int getBufferMaxSize(){
        return buffer.getBufferAllocSize();
    }

    @ManagedAttribute (description = "set bufferMaxSize")
    public void setBufferMaxSize(int size) {
        buffer.setBufferAllocSize(size);
    }

    @ManagedAttribute(description = "get useUpFactor")
    public float getUseUpFactor(){
        return buffer.getUseUpFactor();
    }
    @ManagedAttribute(description = "set useUpFactor")
    public void setUseUpFactor(float factor){
        buffer.setUseUpFactor(factor);
    }
}
