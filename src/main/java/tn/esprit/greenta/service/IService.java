package tn.esprit.greenta.service;

import tn.esprit.greenta.entity.Cour;

import java.util.List;

public interface IService<T> {
    void ajouter(T t);
    void modifier(T t);
    void supprimer(int id);
    List<T> afficher();
}
