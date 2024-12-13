package lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

record GraphDataResultSet(
        List<Integer> xValues,
        Map<String, List<Double>> results
) {}

public class EvalIntListSpeed {
    // Configuration parameters
    private static final int INITIAL_N = 0;
    private static final int N_STEP = 1000;
    private static final int N_MAX = 10000;
    private static final int WARMUP_RUNS = 2;
    private static final int TEST_ITERATIONS = 3;

    // Interface to support both IntList and generic list testing
    interface ListWrapper<T> {
        void append(T value);
        String getName();
    }

    // Wrapper for IntList implementations
    static class IntListWrapper implements ListWrapper<Integer> {
        private final IntList list;
        private final String name;

        public IntListWrapper(IntList list, String name) {
            this.list = list;
            this.name = name;
        }

        @Override
        public void append(Integer value) {
            list.append(value);
        }

        @Override
        public String getName() {
            return name;
        }
    }

    // Wrapper for generic list implementations
    static class GenericListWrapper<T> implements ListWrapper<T> {
        private final GenericList<T> list;
        private final String name;

        public GenericListWrapper(GenericList<T> list, String name) {
            this.list = list;
            this.name = name;
        }

        @Override
        public void append(T value) {
            list.append(value);
        }

        @Override
        public String getName() {
            return name;
        }
    }

    static <T> long timeNAppends(ListWrapper<T> list, int n, Supplier<T> valueSupplier) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            list.append(valueSupplier.get());
        }
        long end = System.nanoTime();
        return (end - start) / 1_000_000; // Convert to milliseconds
    }

    static <T> double runTest(Supplier<ListWrapper<T>> listMaker, int n, Supplier<T> valueSupplier) {
        // Warmup runs
        for (int i = 0; i < WARMUP_RUNS; i++) {
            timeNAppends(listMaker.get(), n, valueSupplier);
        }

        // Actual test runs
        double totalTime = 0;
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            totalTime += timeNAppends(listMaker.get(), n, valueSupplier);
        }
        return totalTime / TEST_ITERATIONS;
    }

    public static void main(String[] args) {

        List<Supplier<ListWrapper<Integer>>> listMakers = new ArrayList<>();

        // Integer-specific list implementations
        listMakers.add(() -> new IntListWrapper(new IntArrayList(), "IntArrayList"));
        listMakers.add(() -> new IntListWrapper(new IntLinkedList(), "IntLinkedList"));
        listMakers.add(() -> new IntListWrapper(new EfficientIntArrayList(), "EfficientIntArrayList"));
        listMakers.add(() -> new IntListWrapper(new EfficientIntLinkedList(), "EfficientIntLinkedList"));

        // Generic list implementations
        listMakers.add(() -> new GenericListWrapper<>(new GenericArrayList<>(), "GenericArrayList"));
        listMakers.add(() -> new GenericListWrapper<>(new GenericLinkedList<>(), "GenericLinkedList"));
        listMakers.add(() -> new GenericListWrapper<>(new GenericLinkedListRecord<>(), "GenericLinkedListRecord"));


        List<Integer> xValues = new ArrayList<>();
        for (int n = INITIAL_N; n <= N_MAX; n += N_STEP) {
            xValues.add(n);
        }


        Map<String, List<Double>> results = new HashMap<>();
        for (Supplier<ListWrapper<Integer>> listMaker : listMakers) {
            ListWrapper<Integer> sampleList = listMaker.get();
            String seriesName = sampleList.getName();
            List<Double> series = new ArrayList<>();

            System.out.println("\nTesting: " + seriesName);
            System.out.println("=".repeat(50));

            for (int n : xValues) {
                System.out.printf("Running test for n=%d...", n);
                double avgTime = runTest(listMaker, n, () -> 1);
                series.add(avgTime);
                System.out.printf(" Average time: %.2f ms%n", avgTime);
            }

            results.put(seriesName, series);
        }


        System.out.println("\nTest Summary");
        System.out.println("=".repeat(50));
        System.out.println("Configuration:");
        System.out.printf("Initial N: %d%n", INITIAL_N);
        System.out.printf("N Step: %d%n", N_STEP);
        System.out.printf("Max N: %d%n", N_MAX);
        System.out.printf("Warmup runs: %d%n", WARMUP_RUNS);
        System.out.printf("Test iterations: %d%n", TEST_ITERATIONS);

        System.out.println("\nDetailed Results:");
        System.out.println(new GraphDataResultSet(xValues, results));
    }
}