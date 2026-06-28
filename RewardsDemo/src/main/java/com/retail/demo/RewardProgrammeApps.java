package com.retail.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.retail.demo.dto.Customer;

// Main Application Class
public class RewardProgrammeApps {
    private static final Map<String, Customer> customerDatabase = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final double POINTS_PER_DOLLAR = 1.0; // 1 point per $1 spent
    private static final int REDEMPTION_THRESHOLD = 100;  // 100 points = $5 discount
    private static final double DISCOUNT_VALUE = 5.0;

    public static void main(String[] args) {
        System.out.println("=== Welcome to the Java Rewards Programme ===");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntegerInput();

            switch (choice) {
                case 1 -> registerCustomer();
                case 2 -> recordPurchase();
                case 3 -> redeemPoints();
                case 4 -> checkBalance();
                case 5 -> {
                    System.out.println("Thank you for using the Rewards Program. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose between 1 and 5.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Register New Customer");
        System.out.println("2. Record a Purchase (Earn Points)");
        System.out.println("3. Redeem Points for Discount");
        System.out.println("4. Check Customer Rewards Balance");
        System.out.println("5. Exit");
        System.out.print("Select an option: ");
    }

    private static void registerCustomer() {
        System.out.print("Enter customer phone number (Unique ID): ");
        String phone = scanner.nextLine().trim();

        if (customerDatabase.containsKey(phone)) {
            System.out.println("Error: A customer with this phone number already exists.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();

        Customer newCustomer = new Customer(name, phone);
        customerDatabase.put(phone, newCustomer);
        System.out.println("Success: Customer '" + name + "' registered successfully! (Welcome Bonus: 10 points)");
    }

    private static void recordPurchase() {
        Customer customer = findCustomer();
        if (customer == null) return;

        System.out.print("Enter purchase amount ($): ");
        double amount = getDoubleInput();

        if (amount <= 0) {
            System.out.println("Invalid amount. Transaction cancelled.");
            return;
        }

        int pointsEarned = (int) (amount * POINTS_PER_DOLLAR);
        customer.addPoints(pointsEarned);
        System.out.printf("Success: Processed $%.2f transaction. Earned %d points.\n", amount, pointsEarned);
    }

    private static void redeemPoints() {
        Customer customer = findCustomer();
        if (customer == null) return;

        if (customer.getPointsBalance() < REDEMPTION_THRESHOLD) {
            System.out.println("Insufficient points. Needs at least " + REDEMPTION_THRESHOLD + " points to redeem.");
            System.out.println("Current Balance: " + customer.getPointsBalance() + " points.");
            return;
        }

        customer.deductPoints(REDEMPTION_THRESHOLD);
        System.out.printf("Success! Redeemed %d points for a $%.2f discount on this visit.\n", REDEMPTION_THRESHOLD, DISCOUNT_VALUE);
        System.out.println("Remaining Balance: " + customer.getPointsBalance() + " points.");
    }

    private static void checkBalance() {
        Customer customer = findCustomer();
        if (customer == null) return;

        System.out.println("\n--- Customer Profile ---");
        System.out.println("Name: " + customer.getName());
        System.out.println("Phone: " + customer.getPhoneNumber());
        System.out.println("Loyalty Balance: " + customer.getPointsBalance() + " points");
    }

    private static Customer findCustomer() {
        System.out.print("Enter customer phone number: ");
        String phone = scanner.nextLine().trim();
        Customer customer = customerDatabase.get(phone);

        if (customer == null) {
            System.out.println("Customer not found. Please register first.");
        }
        return customer;
    }

    private static int getIntegerInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        int num = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        return num;
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Enter a valid amount: ");
            scanner.next();
        }
        double val = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer
        return val;
    }
}

// Customer Data Model

