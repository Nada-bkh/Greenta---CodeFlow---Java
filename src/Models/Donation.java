package Models;




import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

public class Donation {
    private int id;

    private String address;
    private LocalDateTime date;
    private String first_name;
    private String last_name;
    private int phone_number;
    private double amount;
    private Charity charity;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
 public Donation(){}
    public Donation( int id,String address,LocalDateTime date,String first_name,String last_name,int phone_number,double amount,Charity charity){
   this.charity=charity;
     this.id=id;
     this.address=address;
     this.date=date;
     this.first_name=first_name;
     this.last_name=last_name;
     this.phone_number=phone_number;
     this.amount=amount;

    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }


    public Charity getCharity() {
        return charity;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setFirst_name(String first_name) {
        if (first_name == null || first_name.isEmpty() || !NAME_PATTERN.matcher(first_name).matches()) {
            throw new IllegalArgumentException("try again");
        }
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        if (last_name == null || last_name.isEmpty() || !NAME_PATTERN.matcher(last_name).matches()) {
            throw new IllegalArgumentException("try again");
        }
        this.last_name = last_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setCharity(Charity charity) {
        this.charity = charity;
    }

    public void setPhone_number(int phone_number) {
        String phoneNumberString = String.valueOf(phone_number);
        if (!PHONE_PATTERN.matcher(phoneNumberString).matches()) {
            throw new IllegalArgumentException("try again");
        }
        this.phone_number = phone_number;
    }



    @Override
    public String toString() {
     StringBuilder stringBuilder=new StringBuilder();

       stringBuilder.append( "Donation{" )
               .append("id=").append ( id )
               .append(", address='").append(address )
               .append(", date=").append(  date )
               .append(", first_name='" ).append( first_name)
               .append(", last_name='") .append( last_name)
               .append(", phone_number=") .append( phone_number)
               .append(", amount=" ).append( amount );
               stringBuilder.append("}\n");
       return stringBuilder.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null )
            return false;
        if(getClass() != o.getClass())
            return false;
      final  Donation donation = (Donation) o;

      if(!Objects.equals(this.id,donation.id)){return false;}
      return Objects.equals(this.date,donation.date);}


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);

        hash = 11 * hash + Objects.hashCode(this.date);
        return hash;    }
}
