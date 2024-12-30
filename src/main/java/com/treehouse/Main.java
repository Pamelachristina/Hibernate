package com.treehouse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    // ANSI escape codes for colors
    private static final String RESET = "\u001B[0m";
    private static final String HEADER_COLOR = "\u001B[34m";  // Blue
    private static final String ROW_COLOR_1 = "\u001B[37m";  // White
    private static final String ROW_COLOR_2 = "\u001B[36m";  // Cyan
    private static final String BORDER_COLOR = "\u001B[33m"; // Yellow
    private static final String STAT_COLOR = "\u001B[32m";   // Green
    private static final String ERROR_COLOR = "\u001B[31m";  // Red

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSession()) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println(HEADER_COLOR + "\nCountry Database Management:" + RESET);
                System.out.println("1. View All Countries");
                System.out.println("2. Add a New Country");
                System.out.println("3. Edit Existing Country");
                System.out.println("4. Delete a Country");
                System.out.println("5. Exit");
                System.out.print(STAT_COLOR + "Enter your choice: " + RESET);

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        // View all countries
                        List<Country> countries = session.createQuery("FROM Country", Country.class).list();
                        printFormattedTable(countries);
                        displayStatistics(countries);
                        break;
                    case "2":
                        // Add a new country
                        addCountry(session);
                        break;
                    case "3":
                        // Edit an existing country
                        editCountryData(session);
                        break;
                    case "4":
                        // Delete a country
                        deleteCountry(session);
                        break;
                    case "5":
                        // Exit the application
                        exit = true;
                        System.out.println(STAT_COLOR + "Exiting application. Goodbye!" + RESET);
                        break;
                    default:
                        System.out.println(ERROR_COLOR + "Invalid choice. Please try again." + RESET);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void printFormattedTable(List<Country> countries) {
        System.out.println(BORDER_COLOR + "+" + "-".repeat(11) + "+" + "-".repeat(34) + "+" + "-".repeat(17) + "+" + "-".repeat(20) + "+" + RESET);
        System.out.printf(HEADER_COLOR + "| %-10s | %-32s | %-15s | %-18s |%n" + RESET, "Code", "Name", "Internet Users", "Literacy Rate");
        System.out.println(BORDER_COLOR + "+" + "-".repeat(11) + "+" + "-".repeat(34) + "+" + "-".repeat(17) + "+" + "-".repeat(20) + "+" + RESET);

        boolean useAlternateColor = false;

        for (Country country : countries) {
            String rowColor = useAlternateColor ? ROW_COLOR_2 : ROW_COLOR_1;

            String internetUsers = country.getInternetUsers() != null
                    ? String.format("%.2f", country.getInternetUsers())
                    : "--";

            String literacyRate = country.getAdultLiteracyRate() != null
                    ? String.format("%.2f", country.getAdultLiteracyRate())
                    : "--";

            System.out.printf(rowColor + "| %-10s | %-32s | %-15s | %-18s |%n" + RESET,
                    country.getCode(),
                    country.getName(),
                    internetUsers,
                    literacyRate);

            useAlternateColor = !useAlternateColor;
        }

        System.out.println(BORDER_COLOR + "+" + "-".repeat(11) + "+" + "-".repeat(34) + "+" + "-".repeat(17) + "+" + "-".repeat(20) + "+" + RESET);
    }

    private static void displayStatistics(List<Country> countries) {
        System.out.println(STAT_COLOR + "\nStatistics:" + RESET);
        System.out.println(BORDER_COLOR + "=".repeat(50) + RESET);

        Country maxInternetUsers = countries.stream()
                .filter(c -> c.getInternetUsers() != null)
                .max(Comparator.comparing(Country::getInternetUsers))
                .orElse(null);

        Country minInternetUsers = countries.stream()
                .filter(c -> c.getInternetUsers() != null)
                .min(Comparator.comparing(Country::getInternetUsers))
                .orElse(null);

        if (maxInternetUsers != null && minInternetUsers != null) {
            System.out.printf(STAT_COLOR + "Highest Internet Users: %s (%.2f)%n" + RESET,
                    maxInternetUsers.getName(),
                    maxInternetUsers.getInternetUsers());
            System.out.printf(STAT_COLOR + "Lowest Internet Users: %s (%.2f)%n" + RESET,
                    minInternetUsers.getName(),
                    minInternetUsers.getInternetUsers());
        } else {
            System.out.println(ERROR_COLOR + "No data available for Internet Users." + RESET);
        }

        Country maxLiteracyRate = countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null)
                .max(Comparator.comparing(Country::getAdultLiteracyRate))
                .orElse(null);

        Country minLiteracyRate = countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null)
                .min(Comparator.comparing(Country::getAdultLiteracyRate))
                .orElse(null);

        if (maxLiteracyRate != null && minLiteracyRate != null) {
            System.out.printf(STAT_COLOR + "Highest Literacy Rate: %s (%.2f)%n" + RESET,
                    maxLiteracyRate.getName(),
                    maxLiteracyRate.getAdultLiteracyRate());
            System.out.printf(STAT_COLOR + "Lowest Literacy Rate: %s (%.2f)%n" + RESET,
                    minLiteracyRate.getName(),
                    minLiteracyRate.getAdultLiteracyRate());
        } else {
            System.out.println(ERROR_COLOR + "No data available for Literacy Rate." + RESET);
        }

        System.out.println(BORDER_COLOR + "=".repeat(50) + RESET);
    }

    private static void addCountry(Session session) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n" + HEADER_COLOR + "Add a New Country:" + RESET);

        String countryCode;
        while (true) {
            System.out.print(STAT_COLOR + "Enter country code (3 characters): " + RESET);
            countryCode = scanner.nextLine().toUpperCase();
            if (countryCode.length() == 3) {
                break;
            } else {
                System.out.println(ERROR_COLOR + "Invalid code. Country code must be exactly 3 characters." + RESET);
            }
        }

        System.out.print(STAT_COLOR + "Enter country name: " + RESET);
        String countryName = scanner.nextLine();

        Double internetUsers = null;
        while (true) {
            System.out.print(STAT_COLOR + "Enter internet users (or type 'null' for no data): " + RESET);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("null")) {
                break;
            }
            try {
                internetUsers = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(ERROR_COLOR + "Invalid input. Please enter a valid number or 'null'." + RESET);
            }
        }

        Double literacyRate = null;
        while (true) {
            System.out.print(STAT_COLOR + "Enter literacy rate (or type 'null' for no data): " + RESET);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("null")) {
                break;
            }
            try {
                literacyRate = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(ERROR_COLOR + "Invalid input. Please enter a valid number or 'null'." + RESET);
            }
        }

        Country newCountry = new Country();
        newCountry.setCode(countryCode);
        newCountry.setName(countryName);
        newCountry.setInternetUsers(internetUsers);
        newCountry.setAdultLiteracyRate(literacyRate);

        try {
            Transaction transaction = session.beginTransaction();
            session.save(newCountry);
            transaction.commit();
            System.out.println(STAT_COLOR + "New country added successfully!" + RESET);
        } catch (Exception e) {
            System.out.println(ERROR_COLOR + "Error adding country: " + e.getMessage() + RESET);
        }
    }

    private static void deleteCountry(Session session) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n" + HEADER_COLOR + "Delete a Country:" + RESET);
        System.out.print(STAT_COLOR + "Enter country code to delete: " + RESET);
        String countryCode = scanner.nextLine().toUpperCase();

        Country country = session.get(Country.class, countryCode);

        if (country == null) {
            System.out.println(ERROR_COLOR + "Country with code " + countryCode + " not found." + RESET);
            return;
        }

        System.out.printf(ERROR_COLOR + "Are you sure you want to delete %s? (yes/no): " + RESET, country.getName());
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            try {
                Transaction transaction = session.beginTransaction();
                session.delete(country);
                transaction.commit();
                System.out.println(STAT_COLOR + "Country deleted successfully." + RESET);
            } catch (Exception e) {
                System.out.println(ERROR_COLOR + "Error deleting country: " + e.getMessage() + RESET);
            }
        } else {
            System.out.println(STAT_COLOR + "Deletion canceled." + RESET);
        }
    }

    private static void editCountryData(Session session) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n" + HEADER_COLOR + "Edit Country Data:" + RESET);
        System.out.println("\n".repeat(5)); // Insert 5 blank lines
        System.out.print(STAT_COLOR + "Enter country code to edit: " + RESET);
        String countryCode = scanner.nextLine().toUpperCase();

        Country country = session.get(Country.class, countryCode);

        if (country == null) {
            System.out.println(ERROR_COLOR + "Country with code " + countryCode + " not found." + RESET);
            return;
        }

        System.out.println(STAT_COLOR + "Current Data:" + RESET);
        System.out.printf(ROW_COLOR_1 + "Name: %s%n" + RESET, country.getName());
        System.out.printf(ROW_COLOR_1 + "Internet Users: %.2f%n" + RESET,
                country.getInternetUsers() != null ? country.getInternetUsers() : 0);
        System.out.printf(ROW_COLOR_1 + "Literacy Rate: %.2f%n" + RESET,
                country.getAdultLiteracyRate() != null ? country.getAdultLiteracyRate() : 0);

        System.out.print(STAT_COLOR + "Enter new name (leave blank to keep current, or type 'null' to set to null): " + RESET);
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            country.setName(newName.equalsIgnoreCase("null") ? null : newName);
        }

        System.out.print(STAT_COLOR + "Enter new Internet Users (leave blank to keep current, or type 'null' to set to null): " + RESET);
        String newInternetUsers = scanner.nextLine();
        if (!newInternetUsers.isEmpty()) {
            if (newInternetUsers.equalsIgnoreCase("null")) {
                country.setInternetUsers(null);
            } else {
                try {
                    country.setInternetUsers(Double.parseDouble(newInternetUsers));
                } catch (NumberFormatException e) {
                    System.out.println(ERROR_COLOR + "Invalid input. Internet Users not updated." + RESET);
                }
            }
        }

        System.out.print(STAT_COLOR + "Enter new Literacy Rate (leave blank to keep current, or type 'null' to set to null): " + RESET);
        String newLiteracyRate = scanner.nextLine();
        if (!newLiteracyRate.isEmpty()) {
            if (newLiteracyRate.equalsIgnoreCase("null")) {
                country.setAdultLiteracyRate(null);
            } else {
                try {
                    country.setAdultLiteracyRate(Double.parseDouble(newLiteracyRate));
                } catch (NumberFormatException e) {
                    System.out.println(ERROR_COLOR + "Invalid input. Literacy Rate not updated." + RESET);
                }
            }
        }

        try {
            Transaction transaction = session.beginTransaction();
            session.update(country);
            transaction.commit();
            System.out.println(STAT_COLOR + "Country data updated successfully." + RESET);
        } catch (Exception e) {
            System.out.println(ERROR_COLOR + "Error updating country data: " + e.getMessage() + RESET);
        }
    }
}
