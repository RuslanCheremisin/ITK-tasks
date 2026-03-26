package rus.cheremisin;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("hi");

        String[] strArr = {"a","b","c","d","e"};
        Filter<Object> filter = new FilterImpl<>();
        applyToArray(strArr, filter);
        System.out.println(Arrays.toString(strArr));

    }

    public static Object[] applyToArray (Object[] arr, Filter<Object> filter) {
        return Arrays.stream(arr).map(o -> filter.apply(o)).toArray();

    }
}