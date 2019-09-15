package compare;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeveralUse {
    public static void main(String[] args) {
        SeveralUse use = new SeveralUse();

        use.doJobList(makeList());
        use.doJobStream(makeList().stream());
    }

    private void doJobList(List<String> list) {
        List<String> listUpper = new ArrayList<>(list.size());
        List<String> listLower = new ArrayList<>(list.size());

        for (String s : list) {
            listUpper.add(s.toUpperCase());
        }
        System.out.println(listUpper);

        for (String s : list) {
            listLower.add(s.toLowerCase());
        }
        System.out.println(listLower);
    }


    private void doJobStream(Stream<String> stream) {
        List<String> listUpper = stream.map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(listUpper);
/*
        List<String> listLower = stream.map(String::toLowerCase).collect(Collectors.toList());
        System.out.println(listLower);
*/
    }




    //////////////
    private static List<String> makeList() {
        int size = 100;
        List<String> list = new ArrayList<>(size);
        for (int idx = 0; idx < size; idx++) {
            list.add("str_VAL:" + idx);
        }

        return list;
    }
}
