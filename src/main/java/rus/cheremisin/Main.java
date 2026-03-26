package rus.cheremisin;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        MyStringBuilder msb = new MyStringBuilder("hello");
        System.out.println(msb.getStringValue());
        msb.append(", world!");
        System.out.println(msb.getStringValue());
        msb.append(" I'm Rus!");
        System.out.println(msb.getStringValue());
        msb.undo();
        System.out.println(msb.getStringValue());
        msb.undo();
        System.out.println(msb.getStringValue());
        msb.undo();
        System.out.println(msb.getStringValue());
    }
}