package Greenta.Interfaces;

import java.util.ArrayList;

public interface IJob<T> {

    void add (T t );
    ArrayList<T> getAll();

    void update(T t );
    boolean delete (T t);
//findby..

    //getby ...

}
