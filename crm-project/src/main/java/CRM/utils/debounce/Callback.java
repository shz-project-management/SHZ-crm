package CRM.utils.debounce;

public interface Callback<T> {
    public void call(T key);
}