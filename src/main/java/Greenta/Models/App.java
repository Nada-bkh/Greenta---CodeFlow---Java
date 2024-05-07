package greenta.models;

public class App {
    private int id, jobtitle_id;
    private String firstname, lastname, email, pdf;





    public App(int id, int jobtitle_id, String firstname, String lastname, String email, String pdf) {
        this.id = id;
        this.jobtitle_id = jobtitle_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pdf = pdf;
    }

    public App() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobtitle_id() {
        return jobtitle_id;
    }

    public void setJobtitle_id(int jobtitle_id) {
        this.jobtitle_id = jobtitle_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", jobtitle_id=" + jobtitle_id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", pdf='" + pdf + '\'' +
                '}';
    }
}