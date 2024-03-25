package Greenta;
import Models.Charity;
import Services.CharityService;
import Utils.MyConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       MyConnection.getInstance();
        /*======================add charity=============================*/
        Scanner scanner = new Scanner(System.in);
        CharityService cs = new CharityService();

        Charity c = new Charity();

       /* System.out.print("Enter the name of the charity: ");
       c.setName_of_charity(scanner.nextLine());

        System.out.print("Enter the amount donated: ");
        c.setAmount_donated(scanner.nextDouble());

        // Consume newline character
        scanner.nextLine();

        System.out.print("Enter the location: ");
        c.setLocation(scanner.nextLine());

        System.out.print("Enter the picture: ");
       c.setPicture(scanner.nextLine());

        System.out.print("Enter the last date (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();

        // Create a SimpleDateFormat object to parse the date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date lastDate = dateFormat.parse(dateString);
           c.setLast_date(lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Call the addCharity method from the CharityService
      cs.addCharity(c);*/
        /*======================showcharity=============================*/
        List<Charity> charities = cs.showCharity();

        // Display the retrieved charities
        if (charities.isEmpty()) {
            System.out.println("No charities found.");
        } else {
            System.out.println("Charities:");
            for (Charity ch : charities) {
                System.out.println(ch);
            }
        }
        /*======================delete=============================*/

       /* System.out.print("Enter the ID of the charity to delete: ");
        int charityIdToDelete = scanner.nextInt();

        // Call the deleteCharity method from the CharityService
        boolean deleted = cs.deleteCharity(charityIdToDelete);

        // Check if deletion was successful
        if (deleted) {
            System.out.println("Charity with ID " + charityIdToDelete + " deleted successfully.");
        } else {
            System.out.println("Failed to delete charity with ID " + charityIdToDelete + ".");
        }*/
        /*======================update=============================*/


        System.out.print("Enter the ID of the charity to update: ");
        int charityIdToUpdate = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        // Create an instance of Charity to hold the updated information
        Charity updatedCharity = new Charity();

        // Prompt the user to enter the updated information
        System.out.print("Enter the updated name of the charity: ");
        updatedCharity.setName_of_charity(scanner.nextLine());

        System.out.print("Enter the updated amount donated: ");
        updatedCharity.setAmount_donated(scanner.nextDouble());
        scanner.nextLine(); // Consume newline character

        System.out.print("Enter the updated location: ");
        updatedCharity.setLocation(scanner.nextLine());

        System.out.print("Enter the updated picture: ");
        updatedCharity.setPicture(scanner.nextLine());

        System.out.print("Enter the last date (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();

        // Create a SimpleDateFormat object to parse the date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date lastDate = dateFormat.parse(dateString);
            updatedCharity.setLast_date(lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Call the update method from the CharityService
        cs.update(updatedCharity, charityIdToUpdate);


        // Close the scanner
        scanner.close();




        /*======================showcharity=============================*/
        List<Charity> charitiess = cs.showCharity();

        // Display the retrieved charities
        if (charities.isEmpty()) {
            System.out.println("No charities found.");
        } else {
            System.out.println("Charities:");
            for (Charity ch : charitiess) {
                System.out.println(ch);
            }
        }
    }
    }

