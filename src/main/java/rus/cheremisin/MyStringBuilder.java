package rus.cheremisin;

import java.util.LinkedList;
import java.util.List;

public class MyStringBuilder {
    private String stringValue;
    private List<MyStringBuilder> undoStackList;

    public MyStringBuilder(String value) {
        this.stringValue = value;
        this.undoStackList = new LinkedList<>();
        this.undoStackList.add(this);
    }

    public MyStringBuilder append(String stringValue) {
        MyStringBuilder prevMsb = new MyStringBuilder(this.stringValue);
        undoStackList.add(prevMsb);
        this.stringValue += stringValue;
        return this;
    }

    public MyStringBuilder undo() {
        MyStringBuilder undone;
        if (undoStackList.size() > 1) {
            undone = undoStackList.get(undoStackList.size() - 2);
            stringValue = undoStackList.remove(undoStackList.size() - 1).getStringValue();
        } else {
            return undoStackList.get(0);
        }
        return undone;
    }

    public String getStringValue() {
        return stringValue;
    }
}

