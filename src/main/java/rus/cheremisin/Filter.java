package rus.cheremisin;

public interface Filter<T> {
    public T apply(T t);
}
