package tn.esprit.greenta.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Quiz {
    private int id;
    private int courid_id;
    private String titre;
    private LocalDateTime created_at;
    private int duree;
    private int note;
    private int nbrq;

    public Quiz() {
    }

    public Quiz(int courid_id, String titre, LocalDateTime created_at, int duree, int note, int nbrq) {
        this.courid_id = courid_id;
        this.titre = titre;
        this.created_at = created_at;
        this.duree = duree;
        this.note = note;
        this.nbrq = nbrq;
    }

    public Quiz(int id, int courid_id, String titre, LocalDateTime created_at, int duree, int note, int nbrq) {
        this.id = id;
        this.courid_id = courid_id;
        this.titre = titre;
        this.created_at = created_at;
        this.duree = duree;
        this.note = note;
        this.nbrq = nbrq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourid_id() {
        return courid_id;
    }

    public void setCourid_id(int courid_id) {
        this.courid_id = courid_id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getNbrq() {
        return nbrq;
    }

    public void setNbrq(int nbrq) {
        this.nbrq = nbrq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quiz quiz)) return false;
        return getCourid_id() == quiz.getCourid_id() && Objects.equals(getTitre(), quiz.getTitre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourid_id(), getTitre());
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", courid_id=" + courid_id +
                ", titre='" + titre + '\'' +
                ", created_at=" + created_at +
                ", duree=" + duree +
                ", note=" + note +
                ", nbrq=" + nbrq +
                '}';
    }
}
