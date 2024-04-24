package tn.esprit.greenta.entity;

import java.time.LocalDateTime;

public class Epreuve {
    private int id;
    private int quizid_id;
    private LocalDateTime date_p;
    private int note;
    private boolean etat;

    public Epreuve() {
    }

    public Epreuve(int quizid_id, LocalDateTime date_p, int note, boolean etat) {
        this.quizid_id = quizid_id;
        this.date_p = date_p;
        this.note = note;
        this.etat = etat;
    }

    public Epreuve(int id, int quizid_id, LocalDateTime date_p, int note, boolean etat) {
        this.id = id;
        this.quizid_id = quizid_id;
        this.date_p = date_p;
        this.note = note;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizid_id() {
        return quizid_id;
    }

    public void setQuizid_id(int quizid_id) {
        this.quizid_id = quizid_id;
    }

    public LocalDateTime getDate_p() {
        return date_p;
    }

    public void setDate_p(LocalDateTime date_p) {
        this.date_p = date_p;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Epreuve{" +
                "id=" + id +
                ", quizid_id=" + quizid_id +
                ", date_p=" + date_p +
                ", note=" + note +
                ", etat=" + etat +
                '}'+"\n";
    }
}
