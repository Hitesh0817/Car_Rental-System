import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Car class
class Car {
    private String carId;
    private String brand;
    private String model;
    private double pricePerDay;
    private boolean available;

    public Car(String carId, String brand, String model, double pricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.available = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void rent() {
        this.available = false;
    }

    public void returnCar() {
        this.available = true;
    }

    @Override
    public String toString() {
        return carId + " - " + brand + " " + model + " ($" + pricePerDay + "/day) - " +
                (available ? "Available" : "Rented");
    }
}

// Customer class
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

// Rental class
class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    @Override
    public String toString() {
        return customer.getName() + " rented " + car.getBrand() + " " + car.getModel() +
                " for " + days + " day(s). Total: $" + (car.getPricePerDay() * days);
    }
}

// CarRentalSystem class
class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        car.rent();
        Rental rental = new Rental(car, customer, days);
        rentals.add(rental);
    }

    public void returnCar(String carId) {
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar().getCarId().equalsIgnoreCase(carId)) {
                rental.getCar().returnCar();
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
            System.out.println("Car returned by " + rentalToRemove.getCustomer().getName());
        } else {
            System.out.println("Car is not currently rented.");
        }
    }

    public void viewAllCars() {
        System.out.println("\n=== All Cars ===");
        for (Car car : cars) {
            System.out.println(car);
        }
    }

    public void viewCurrentRentals() {
        System.out.println("\n=== Current Rentals ===");
        if (rentals.isEmpty()) {
            System.out.println("No active rentals.");
        } else {
            for (Rental rental : rentals) {
                System.out.println(rental);
            }
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental Menu =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View All Cars");
            System.out.println("4. View Current Rentals");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    String customerId = "CUS" + (customers.size() + 1);
                    Customer customer = new Customer(customerId, name);
                    addCustomer(customer);

                    System.out.println("\nAvailable Cars:");
                    for (Car car : cars) {
                        if (car.isAvailable()) {
                            System.out.println(car);
                        }
                    }

                    System.out.print("Enter the Car ID to rent: ");
                    String carId = scanner.nextLine();

                    Car selectedCar = null;
                    for (Car car : cars) {
                        if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
                            selectedCar = car;
                            break;
                        }
                    }

                    if (selectedCar != null) {
                        System.out.print("Enter number of rental days: ");
                        int days;

                        try {
                            days = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number of days.");
                            continue;
                        }

                        double total = selectedCar.getPricePerDay() * days;
                        System.out.printf("Total price: $%.2f\n", total);

                        System.out.print("Confirm rental? (Y/N): ");
                        String confirm = scanner.nextLine();

                        if (confirm.equalsIgnoreCase("Y")) {
                            rentCar(selectedCar, customer, days);
                            System.out.println("Car rented successfully!");
                        } else {
                            System.out.println("Rental canceled.");
                        }
                    } else {
                        System.out.println("Car ID not found or car not available.");
                    }
                }

                case 2 -> {
                    System.out.print("Enter Car ID to return: ");
                    String carId = scanner.nextLine();
                    returnCar(carId);
                }

                case 3 -> viewAllCars();

                case 4 -> viewCurrentRentals();

                case 5 -> {
                    System.out.println("Thank you for using the Car Rental System.");
                    return;
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        CarRentalSystem system = new CarRentalSystem();

        system.addCar(new Car("C001", "Toyota", "Camry", 60));
        system.addCar(new Car("C002", "Honda", "Civic", 55));
        system.addCar(new Car("C003", "Mahindra", "Thar", 150));

        system.menu();
    }
}
