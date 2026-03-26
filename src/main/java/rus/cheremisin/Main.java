package rus.cheremisin;

import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("hi");

        String[] arr = {"a","a","a","b"};
        System.out.println(countElements(arr));

    }

    public static Map<String, Integer> countElements(String[] keys) {
        Map<String, Integer> map = new HashMap<>();
        for (String str: keys) {
            Integer value = map.get(str);
            map.put(str, value == null ? 1 : map.get(str) + 1);
        }
        return map;
    }
}