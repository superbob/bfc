// >>[-]<<[->>+<<]

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        InputStream in = System.in;
        OutputStream out = System.out;

        List<Integer> storage = new ArrayList<>();
        int memoryPointer = 0;

        memoryPointer++;
        memoryPointer++;
        while ($bfc$get(storage, memoryPointer) != 0) {
            $bfc$decrease(storage, memoryPointer);
        }
        memoryPointer--;
        memoryPointer--;
        while ($bfc$get(storage, memoryPointer) != 0) {
            $bfc$decrease(storage, memoryPointer);
            memoryPointer++;
            memoryPointer++;
            $bfc$increase(storage, memoryPointer);
            memoryPointer--;
            memoryPointer--;
        }
        //, read
        $bfc$set(storage, memoryPointer, $bfc$read(in));
        //. write
        $bfc$write(out, $bfc$get(storage, memoryPointer));
    }

    public void echoinout(InputStream in, OutputStream out) {
        List<Integer> storage = new ArrayList<>();
        int memoryPointer = 0;

        memoryPointer++;
        memoryPointer--;
        $bfc$increase(storage, memoryPointer);
        $bfc$decrease(storage, memoryPointer);

        //, read
        $bfc$set(storage, memoryPointer, $bfc$read(in));
        //. write
        $bfc$write(out, $bfc$get(storage, memoryPointer));

        //return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    public String echostrstr(String str) {
        InputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<Integer> storage = new ArrayList<>();
        int memoryPointer = 0;

        memoryPointer++;
        memoryPointer--;
        $bfc$increase(storage, memoryPointer);
        $bfc$decrease(storage, memoryPointer);

        //, read
        $bfc$set(storage, memoryPointer, $bfc$read(in));
        //. write
        $bfc$write(out, $bfc$get(storage, memoryPointer));

        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    public int echoii(int param) {
        Deque<Integer> in = new ArrayDeque<>();
        in.addLast(param); // Work to do here
        List<Integer> out = new ArrayList<>();

        List<Integer> storage = new ArrayList<>();
        int memoryPointer = 0;

        //, read
        $bfc$set(storage, memoryPointer, $bfc$readi(in));
        //. write
        $bfc$writei(out, $bfc$get(storage, memoryPointer));

        return out.get(0); // Work to do here
        // Handle no such element and to much elements (add tolerant and intolerant options)
    }

    private static void $bfc$increase(List<Integer> storage, int memoryPointer) {
        $bfc$checkMemory(storage, memoryPointer);
        storage.set(memoryPointer, storage.get(memoryPointer) + 1);
    }

    private static void $bfc$decrease(List<Integer> storage, int memoryPointer) {
        $bfc$checkMemory(storage, memoryPointer);
        storage.set(memoryPointer, storage.get(memoryPointer) - 1);
    }

    private static int $bfc$get(List<Integer> storage, int memoryPointer) {
        $bfc$checkMemory(storage, memoryPointer);
        return storage.get(memoryPointer);
    }

    private static void $bfc$set(List<Integer> storage, int memoryPointer, int value) {
        $bfc$checkMemory(storage, memoryPointer);
        storage.set(memoryPointer, value);
    }

    private static void $bfc$checkMemory(List<Integer> storage, int requiredSize) {
        while (storage.size() <= requiredSize) storage.add(0);
    }

    private static int $bfc$read(InputStream in) {
        try {
            return in.read();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void $bfc$write(OutputStream out, int value) {
        try {
            out.write(value);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static int $bfc$readi(Deque<Integer> in) {
        return in.removeFirst();
    }

    private static void $bfc$writei(List<Integer> out, int value) {
        out.add(value);
    }
}
