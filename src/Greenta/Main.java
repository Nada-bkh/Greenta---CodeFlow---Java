package Greenta;
import Models.Charity;
import Models.Donation;
import Services.CharityService;
import Services.DonationService;
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
        DonationService ds = new DonationService();

        Charity c = new Charity();
/*
        while (true) {
            Charity c = new Charity();

            System.out.print("Enter the name of the charity: ");
            c.setName_of_charity(scanner.nextLine());

            System.out.print("Enter the amount donated: ");
            c.setAmount_donated(scanner.nextDouble());

            // Consume newline character
            scanner.nextLine();

            System.out.print("Enter the location: ");
            c.setLocation(scanner.nextLine());

            System.out.print("Enter the picture file path: ");
            String picturePath = scanner.nextLine();
            c.setPicture(picturePath);

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


            cs.addCharity(c);


            System.out.print("Do you want to add another charity? (yes/no): ");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("yes")) {
                break; // Exit the loop if the user does not want to add another charity
            }
        }*/
        /*======================showcharity=============================*/
        List<Charity> charities = cs.showCharity();

        // Display the retrieved charities
        if (charities.isEmpty()) {
            System.out.println("No charities found.");
        }
        else {
            System.out.println("Charities:");
            for (Charity ch : charities) {
                System.out.println(ch);
            }}
          /* for (int i = 0; i < charities.size(); i++) {
                System.out.println((i + 1) + ". " + charities.get(i).getName_of_charity());
            }
        }
        System.out.print("Select a charity (enter the corresponding number): ");
        int charityIndex = scanner.nextInt();
        if (charityIndex < 1 || charityIndex > charities.size()) {
            System.out.println("Invalid charity selection.");
            return;}
        Charity selectedCharity = charities.get(charityIndex - 1);

       /* System.out.print("Select a charity (enter the corresponding number): ");
        int charityIndex = scanner.nextInt();
        if (charityIndex < 1 || charityIndex > charities.size()) {
            System.out.println("Invalid charity selection.");
            return;}
           Charity selectedCharity = charities.get(charityIndex - 1);*/
            /*======================delete=============================*/

       /*System.out.print("Enter the ID of the charity to delete: ");
        int charityIdToDelete = scanner.nextInt();

        // Call the deleteCharity method from the CharityService
        boolean deleted = cs.deleteCharity(charityIdToDelete);

        // Check if deletion was successful
        if (deleted) {
            System.out.println("Charity with ID " + charityIdToDelete + " deleted successfully.");
        } else {
            System.out.println("Failed to delete charity with ID " + charityIdToDelete + ".");
        }
        /*======================update=============================*/

/*
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



        */

            /*======================add donation=============================*/





           /* Donation donationToAdd = new Donation();


          System.out.print("Enter first name: ");
        donationToAdd.setFirst_name(scanner.nextLine());

        System.out.print("Enter last name: ");
        donationToAdd.setLast_name(scanner.nextLine());

        System.out.print("Enter address: ");
        donationToAdd.setAddress(scanner.nextLine());

        System.out.print("Enter phone number: ");
        int phoneNumber = scanner.nextInt();
        donationToAdd.setPhone_number(phoneNumber);

        System.out.print("Enter donation amount: ");
        double amount = scanner.nextDouble();
        donationToAdd.setAmount(amount);


       ds.addDonation(donationToAdd, selectedCharity);*/

            /*======================show donations=============================*/
            // Close the scanner*/
            List<Donation> donations = ds.showDonation();
            for (Donation donation : donations) {
                System.out.println(donation);
            }



            /*======================delete donation=============================*/

           /* System.out.print("Enter the ID of the donation to delete: ");
            int donationIdToDelete = scanner.nextInt();


            boolean deleted = ds.deleteDonation(donationIdToDelete);*/
        /*======================donation count=============================*/
        int totalDonationCount = ds.donationAllCount();


        System.out.println("Total number of donations: " + totalDonationCount);
        /*======================find charity by id=============================*/

      /*  System.out.print("Enter the ID of the charity you want to find: ");
        int charityIdToFind = scanner.nextInt();

        Charity foundCharity = cs.showCharityById(charityIdToFind);
        if (foundCharity != null) {
            System.out.println("Charity found by ID: " + foundCharity.getName_of_charity());
        } else {
            System.out.println("Charity with ID " + charityIdToFind + " not found.");
        }*/

        /*======================Test charity with most donations=============================*/

        List<Charity> allCharities = cs.showCharity(); // Assuming this method works correctly
        cs.charityWithMostDonation(allCharities);
        /*======================Test orderCharitiesByDonationCount=============================*/

        List<Charity> orderedCharities = cs.orderCharitiesByDonationCount();
        System.out.println("Charities ordered by donation count:");
        for (Charity charity : orderedCharities) {
            System.out.println(charity.getName_of_charity() + ": " + cs.donationCount(charity.getId()));}

            scanner.close();
        }


    }

