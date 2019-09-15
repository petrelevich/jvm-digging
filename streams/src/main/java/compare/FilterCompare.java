package compare;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterCompare {

    private static final ForkJoinPool customThreadPool = createForkJoinPool();
    private static String filter = "000";

    private final String[] array;
    private final List<String> list;
    private final boolean withLoad;

    public FilterCompare(boolean withLoad) {
        int size = 100_000_000;
        array = makeArray(size);
        list = Arrays.asList(array);
        this.withLoad = withLoad;
    }

    private void loadFile() {
        try {
            Path path = Paths.get(this.getClass().getClassLoader().getResource("demoData.txt").getFile());
            try (Stream<String> stream = Files.lines(path)) {
                long count = stream.count();
                System.out.println(String.format("read %s lines", count));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FilterCompare filterCompare = new FilterCompare(false);
        int count;

        count = filterCompare.filterArrayFor();
        System.out.println(count);
        count = filterCompare.filterArrayStream();
        System.out.println(count);

        count = filterCompare.filterFor();
        System.out.println(count);
        count = filterCompare.filterStream();
        System.out.println(count);

        count = filterCompare.filterStreamUnordered();
        System.out.println(count);

        count = filterCompare.filterStreamParallel();
        System.out.println(count);
    }

    public int filterArrayFor() {
        if (this.withLoad) {
            loadFile();
        }

        List<String> listFiltered = new ArrayList<>();
        for (int idx = 0; idx < array.length; idx++) {
            if (array[idx].contains(filter)) {
                listFiltered.add(array[idx]);
            }
        }
        return listFiltered.size();
    }

    public int filterArrayStream() {
        if (this.withLoad) {
            loadFile();
        }

        List<String> listFiltered = Arrays.stream(array).filter(val -> val.contains(filter)).collect(Collectors.toList());
        return listFiltered.size();
    }

    public int filterFor() {
        if (this.withLoad) {
            loadFile();
        }

        List<String> listFiltered = new ArrayList<>();
        for (int idx = 0; idx < list.size(); idx++) {
            if (list.get(idx).contains(filter)) {
                listFiltered.add(list.get(idx));
            }
        }
        return listFiltered.size();
    }

    public int filterStream() {
        if (this.withLoad) {
            loadFile();
        }

        List<String> listFiltered = list.stream().filter(val -> val.contains(filter)).collect(Collectors.toList());
        return listFiltered.size();
    }

    public int filterStreamUnordered() {
        if (this.withLoad) {
            loadFile();
        }

        List<String> listFiltered = list.stream().unordered().filter(val -> val.contains(filter)).collect(Collectors.toList());
        return listFiltered.size();
    }


//////////////////////////////////////


    public int filterStreamParallel() throws ExecutionException, InterruptedException {
        if (this.withLoad) {
            loadFile();
        }

        List<String> listFiltered = customThreadPool.submit(
                () -> list.parallelStream()
                        .filter(val -> val.contains(filter)).collect(Collectors.toList())
        ).get();

        return listFiltered.size();
    }

    private static ForkJoinPool createForkJoinPool() {
        final ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("custom-worker-" + worker.getPoolIndex());
            return worker;
        };

        return new ForkJoinPool(10, factory, null, false);
    }

//////////////////////////////////////

    private static String[] makeArray(int size) {
        String[] array = new String[size];
        for (int idx = 0; idx < size; idx++) {
            array[idx] = "val:" + idx;
        }
        return array;
    }
}
