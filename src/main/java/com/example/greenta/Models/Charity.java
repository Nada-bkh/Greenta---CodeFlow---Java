package com.example.greenta.Models;




import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class Charity {
    private Integer id;
    private double amount_donated;
    private String name_of_charity;
    private String picture;
    private Date last_date;
    private String location;
    private Set<Donation> donations;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    public Charity(){}
    public Charity(int id,double amount_donated,String name_of_charity,String picture,Date last_date,String location){
         this.id=id;
         this.amount_donated=amount_donated;
         this.name_of_charity=name_of_charity;
         this.last_date=last_date;
         this.picture=picture;
         this.location=location;
    }

    public int getId() {
        return id;
    }

    public  Date getLast_date() {
        return last_date;
    }


    public double getAmount_donated() {
        return amount_donated;
    }

    public String getName_of_charity() {
        return name_of_charity;
    }

    public String getLocation() {
        return location;
    }

    public String getPicture() {
        return picture;
    }

    public Set<Donation> getDonations() {
        return donations;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount_donated(double amount_donated) {
        this.amount_donated = amount_donated;
    }

//    public void setLast_date(Date last_date) {
//        Date currentDate = new Date();
//        if (last_date.before(currentDate)) {
//            throw new IllegalArgumentException("The last date of donation cannot be less than the current date.");
//        }
//        this.last_date = last_date;
//    }
public void setLast_date(Date last_date) {
    this.last_date = last_date;
}

    public void setName_of_charity(String name_of_charity) {
//        if (name_of_charity == null || name_of_charity.isEmpty() || !NAME_PATTERN.matcher(name_of_charity).matches()) {
//            throw new IllegalArgumentException("try again");
//        }
        this.name_of_charity = name_of_charity;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setDonations(Set<Donation> donations) {
         this.donations = donations;
    }

    @Override
    public String toString() {
        return "Charity{" +
                "id=" + id +
                ", amount_donated=" + amount_donated +
                ", name_of_charity='" + name_of_charity + '\'' +
                ", picture='" + picture + '\'' +
                ", last_date=" + last_date +
                ", location='" + location + '\'' +
                ", donations=" + donations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Charity charity = (Charity) o;
        return id == charity.id && Double.compare(amount_donated, charity.amount_donated) == 0 && Objects.equals(name_of_charity, charity.name_of_charity) && Objects.equals(picture, charity.picture) && Objects.equals(last_date, charity.last_date) && Objects.equals(location, charity.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount_donated, name_of_charity, picture, last_date, location);
    }
}
