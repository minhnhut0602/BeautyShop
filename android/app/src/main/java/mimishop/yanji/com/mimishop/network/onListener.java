package mimishop.yanji.com.mimishop.network;

/**
 * Created by KimCholJu on 6/1/2015.
 */
public class onListener<T>  {
    public static interface ErrorListener<T> {
        void onErrorResponse(T t);
    }

    public static interface Listener <T> {
        void onResponse(T t);
    }
}
