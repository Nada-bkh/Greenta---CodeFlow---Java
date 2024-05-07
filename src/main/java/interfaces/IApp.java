package interfaces;

import java.util.ArrayList;

public interface IApp<T> {

    void add (T t );
    ArrayList<T> getAll();

    void update(T t );
    boolean delete (T t);
//findby..

    //getby ...

}