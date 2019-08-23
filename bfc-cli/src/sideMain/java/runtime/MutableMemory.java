package runtime;

import java.util.ArrayList;
import java.util.List;

public class MutableMemory {
    private final List<Integer> storage = new ArrayList<>();

    public int get(int memoryPointer) {
        checkMemory(memoryPointer);
        return storage.get(memoryPointer);
    }

    public void set(int memoryPointer, int value) {
        checkMemory(memoryPointer);
        storage.set(memoryPointer, value);
    }

    private void checkMemory(int requiredSize) {
        while (storage.size() <= requiredSize) storage.add(0);
    }
}
