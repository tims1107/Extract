package model;

import java.util.ArrayList;
import java.util.Collections;

public class LoadArray {

    public void testArray() {
        String[] elements = { "cat", "dog", "fish" };

        long t1 = System.currentTimeMillis();

        // Version 1: use addAll method.
        for (int i = 0; i < 10000000; i++) {
            ArrayList<String> list = new ArrayList<>();
            Collections.addAll(list, elements);
            if (list.size() != 3) {
                return;
            }
        }

        long t2 = System.currentTimeMillis();

        // Version 2: use for-loop and add.
        for (int i = 0; i < 10000000; i++) {
            ArrayList<String> list = new ArrayList<>();
            for (String element : elements) {
                list.add(element);
            }
            if (list.size() != 3) {
                return;
            }
        }

        long t3 = System.currentTimeMillis();

        // ... Result.
        System.out.println(t2 - t1);
        System.out.println(t3 - t2);

    }

}