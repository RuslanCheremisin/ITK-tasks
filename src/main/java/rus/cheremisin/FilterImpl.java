package rus.cheremisin;

public class FilterImpl<T> implements Filter<T>{
    @Override
    public T apply(T o) {
        //some work
        return o;
    }
}
