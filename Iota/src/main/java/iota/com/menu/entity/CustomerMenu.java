package iota.com.menu.entity;

import iota.com.menu.Menu;
import iota.com.menu.MenuItem;
import iota.com.model.Booking;
import iota.com.model.Customer;
import iota.com.model.Gender;
import iota.com.service.BookingManager;
import iota.com.service.CustomerManager;
import iota.com.utils.ValidationUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private final CustomerManager customerManager;
    private final BookingManager bookingManager; // To show related bookings for customers
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // For handling birthdate input/output

    public CustomerMenu(CustomerManager customerManager, BookingManager bookingManager) {
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
    }

    public void showMenu() {
        Menu menu = new Menu("Customer Management");
        menu.add(new MenuItem("Add New Customer", this::addNewCustomer));
        menu.add(new MenuItem("List All Customers", this::listAllCustomers));
        menu.add(new MenuItem("Find Customer by ID", this::findCustomerById));
        menu.add(new MenuItem("Delete Customer", this::deleteCustomer));
        menu.add(new MenuItem("Update Customer", this::updateCustomer));
        menu.add(new MenuItem("Back to Main Menu", () -> System.out.println("Returning to the main menu...")));
        menu.execute();
    }

    private void addNewCustomer() {
        try {
            System.out.print("Enter customer's name: ");
            String name = scanner.nextLine();
            ValidationUtils.validateName(name, "Customer Name");

            System.out.print("Enter customer's email: ");
            String email = scanner.nextLine();
            ValidationUtils.validateEmail(email, "Customer Email");

            System.out.print("Enter customer's phone number (optional): ");
            String phone = scanner.nextLine();
            ValidationUtils.validatePhone(phone, "Phone number");

            System.out.print("Enter customer's gender (Male/Female/Other): ");
            String genderInput = scanner.nextLine();
            ValidationUtils.validateEnum(Gender.class, genderInput, "Gender");
            Gender gender = Gender.valueOf(genderInput.toUpperCase());

            System.out.print("Enter customer's birthdate (yyyy-MM-dd): ");
            String birthdateInput = scanner.nextLine();
            ValidationUtils.validateDate(birthdateInput, "Birthdate");
            Date birthdate = dateFormat.parse(birthdateInput);

            Customer customer = new Customer(name, email, phone, gender, birthdate);
            customerManager.addCustomer(customer);
            System.out.println("Customer added successfully: " + customer);
        } catch (Exception e) {
            System.err.println("Error while adding a customer: " + e.getMessage());
        }
    }

    private void listAllCustomers() {
        try {
            List<Customer> customers = customerManager.findAllCustomers();
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
            } else {
                for (Customer customer : customers) {
                    System.out.println(customer.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to list customers: " + e.getMessage());
        }
    }

    private void findCustomerById() {
        try {
            System.out.print("Enter Customer ID: ");
            String idInput = scanner.nextLine();
            long id = Long.parseLong(idInput);
            ValidationUtils.validatePositive(id, "Customer ID");

            Customer customer = customerManager.findCustomerById(id);

            if (customer != null) {
                System.out.println(customer.toString());

                // Display related bookings, if any
                List<Booking> bookings = bookingManager.getBookingsForCustomer(id);
                if (bookings != null && !bookings.isEmpty()) {
                    System.out.println("\n--- Bookings for this customer ---");
                    bookings.forEach(System.out::println);
                } else {
                    System.out.println("\nNo bookings found for this customer.");
                }
            } else {
                System.out.println("No customer found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error while fetching customer: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        try {
            System.out.print("Enter Customer ID to delete: ");
            String idInput = scanner.nextLine();
            long id = Long.parseLong(idInput);
            ValidationUtils.validatePositive(id, "Customer ID");

            customerManager.deleteCustomer(id); // Remove from DB
            System.out.println("Customer deleted successfully!");
        } catch (Exception e) {
            System.err.println("Failed to delete customer: " + e.getMessage());
        }
    }

    private void updateCustomer() {
        try {
            System.out.print("Enter Customer ID to update: ");
            String idInput = scanner.nextLine();
            long id = Long.parseLong(idInput);
            ValidationUtils.validatePositive(id, "Customer ID");

            Customer customer = customerManager.findCustomerById(id);

            if (customer == null) {
                System.out.println("No customer found with ID: " + id);
                return;
            }

            // Update customer details
            System.out.println("Updating details for: " + customer);
            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            ValidationUtils.validateName(name, "Customer Name");
            customer.setName(name);

            System.out.print("Enter new email (leave blank to keep current): ");
            String email = scanner.nextLine();
            ValidationUtils.validateEmail(email, "Customer Email");
            customer.setEmail(email);

            System.out.print("Enter new phone number (leave blank to keep current): ");
            String phone = scanner.nextLine();
            ValidationUtils.validatePhone(phone, "Phone number");
            customer.setPhone(phone);

            System.out.print("Enter new gender (leave blank to keep current): ");
            String genderInput = scanner.nextLine();
            ValidationUtils.validateEnum(Gender.class, genderInput, "Gender");
            customer.setGender(Gender.valueOf(genderInput.toUpperCase()));

            System.out.print("Enter new birthdate (yyyy-MM-dd) (leave blank to keep current): ");
            String birthdateInput = scanner.nextLine();
            if (!birthdateInput.trim().isEmpty()) {
                ValidationUtils.validateDate(birthdateInput, "Birthdate");
                Date birthdate = dateFormat.parse(birthdateInput);
                customer.setBirthdate(birthdate);
            }

            customerManager.updateCustomer(customer); // Persist updated data
            System.out.println("Customer updated successfully: " + customer);
        } catch (Exception e) {
            System.err.println("Error while updating customer: " + e.getMessage());
        }
    }
}
