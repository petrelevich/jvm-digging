package compare;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MutationArrayList {
    public static void main(String[] args) {
        MutationArrayList mutator = new MutationArrayList();

        mutator.doJobFor();

        mutator.doJobStream();

        mutator.dirtyFunction();
    }

    private void doJobFor() {
        List<String> list = makeList();

        list.replaceAll(String::toUpperCase);

        System.out.println(list);
    }


    private void doJobStream() {
        List<String> list = makeList();

        List<String> listNew = list.stream().map(String::toUpperCase).collect(Collectors.toList());

        System.out.println(listNew);

    }

    private void dirtyFunction() {
        //Максимально плохой пример

        int[] counter = {0};
        List<String> list = makeList();
        List<String> listNew = new ArrayList<>();

        list.stream()
                .map(str -> listNew.add(str.toLowerCase()))
                .forEach(str -> counter[0]++);

        System.out.println("counter:" + counter[0]);
        System.out.println(listNew);
    }


    ///////////////////
    private static List<String> makeList() {
        int size = 100;
        List<String> list = new ArrayList<>(size);
        for (int idx = 0; idx < size; idx++) {
            list.add("str_val:" + idx);
        }

        return list;
    }
}
