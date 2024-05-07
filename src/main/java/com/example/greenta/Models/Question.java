package com.example.greenta.Models;

public class Question {
    private int id;
    private int quizid_id;
    private String question;
    private String reponse;
    private int reponseCorrecte;
    private int note;

    public Question() {
    }

    public Question(int quizid_id, String question, String reponse, int reponseCorrecte, int note) {
        this.quizid_id = quizid_id;
        this.question = question;
        this.reponse = reponse;
        this.reponseCorrecte = reponseCorrecte;
        this.note = note;
    }

    public Question(int id, int quizid_id, String question, String reponse, int reponseCorrecte, int note) {
        this.id = id;
        this.quizid_id = quizid_id;
        this.question = question;
        this.reponse = reponse;
        this.reponseCorrecte = reponseCorrecte;
        this.note = note;
    }

    public int getReponseCorrecte() {
        return reponseCorrecte;
    }

    public void setReponseCorrecte(int reponseCorrecte) {
        this.reponseCorrecte = reponseCorrecte;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", quizid_id=" + quizid_id +
                ", question='" + question + '\'' +
                ", reponse='" + reponse + '\'' +
                ", note=" + note +
                '}'+"\n";
    }
}
