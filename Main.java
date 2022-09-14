package carsharing;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static java.sql.Types.NULL;

public class Main {
    Scanner scanner = new Scanner(System.in);
    Connector connector = new Connector();

    public static void main(String[] args) throws SQLException {
        String dbName = "test";
        Status state = Status.STARTUP;
        Main run = new Main();
        String dbUrl;
        if (args.length >= 2 && "-databaseFileName".equals(args[0])) {
            dbName = args[1];
        }
        dbUrl = "jdbc:h2:./src/carsharing/db/" + dbName;

        try {
            run.connector.connect(dbUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        run.connector.createCompanyTable();
        run.connector.createCarTable();
        run.connector.createCustomerTable();
        do {
            switch (state) {
                case STARTUP:
                    state = Status.MAIN;
                    break;
                case MAIN:
                    state = run.mainMenu();
                    break;
                case MANAGER_LOGIN:
                    state = run.managerLogin();
                    break;
                case LIST_COMPANIES:
                    state = run.listCompanies();
                    break;
                case CREATE_COMPANY:
                    state = run.createCompany();
                    break;
                case CUSTOMER_LOGIN:
                    state = run.customerLogin();
                    break;
                case CREATE_CUSTOMER:
                    state = run.createCustomer();
                    break;
            }
            
        } while (state != Status.SHUTDOWN);

        run.connector.closeConnection();
    }

    Status createCustomer() throws SQLException {
        String name;
        System.out.println("Enter the customer name:");
        name = scanner.nextLine();
        connector.createCustomer(name);
        return Status.MAIN;
    }

    Status mainMenu() {
        System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");
        int opt = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        switch (opt) {
            case 1:
                return Status.MANAGER_LOGIN;
            case 2:
                return Status.CUSTOMER_LOGIN;
            case 3:
                return Status.CREATE_CUSTOMER;
            case 0:
                return Status.SHUTDOWN;
        }
        return Status.MAIN;
    }

    Status managerLogin() {
        System.out.println("1. Company list\n2. Create a company\n0. Back");
        int input = scanner.nextInt();
        System.out.println();
        switch (input) {
            case 1:
                return Status.LIST_COMPANIES;
            case 2:
                return Status.CREATE_COMPANY;
            case 0:
                return Status.MAIN;
            default:
                return Status.MANAGER_LOGIN;
        }

    }
    Status listCompanies() throws SQLException {
        int choice;
        List<Company> companies = connector.getCompanies();
        if (companies.size() > 0) {
            System.out.println("Choose the company:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, companies.get(i).getName());
            }

            System.out.println("0. Back");
            choice = scanner.nextInt();
            if (choice == 0) {
                return Status.MANAGER_LOGIN;
            } else {
                companyMenu(choice);
                }
        } else {
            System.out.println("The company list is empty!");
        }
        System.out.println();
        return Status.MANAGER_LOGIN;
    }

    Status createCompany() throws SQLException {
        scanner.nextLine();
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        connector.createCompany(name);
        return Status.MANAGER_LOGIN;
    }

    void listCars(int companyId) throws SQLException {
        List<Car> cars = connector.getCars(companyId);
        System.out.println("Car list:");
        if (cars.size() != 0) {
            for (int i = 1; i <= cars.size(); i++) {
                System.out.printf("%d. %s", i, cars.get(i - 1).getName());
            }
        } else {
            System.out.println("The car list is empty!");
        }
        System.out.println();
    }

    void companyMenu(int choice) throws SQLException {
        List<Company> companies = connector.getCompanies();
        int companyId = companies.get(choice - 1).getId();
        System.out.printf("'%s' company\n", companies.get(choice - 1).getName());
        do {
            System.out.println("1. Car list\n2. Create a car\n0. Back");
            choice = scanner.nextInt();
            System.out.println();
            switch (choice) {
                case 1:
                    listCars(companyId);
                    break;
                case 2:
                    addCar(companyId);
                    break;
                case 0:
                    return;
                default:
                    choice = -1;
            }
        } while (choice != -1);
    }

    void addCar(int companyId) throws SQLException {
        String name;
        System.out.println("Enter the car name:");
        scanner.nextLine();
        name = scanner.nextLine();
        connector.addCar(name, companyId);
    }

    Status customerLogin() throws SQLException {
        int choice;
        List<Customer> customers = connector.getCustomers();
        if (customers.size() != 0) {
            System.out.println("Customer list:");
            for (int i = 1; i <= customers.size(); i++) {
                System.out.printf("%d. %s\n", i, customers.get(i - 1).getName());
            }
            System.out.println("0. Back");
            choice = scanner.nextInt();
            if (choice == 0) {
                return Status.MAIN;
            } else {
                customerMenu(customers, choice);
            }
        } else {
            System.out.println("The customer list is empty!");
        }
        return Status.MAIN;
    }

    private void customerMenu(List<Customer> customers, int choice) throws SQLException {
        int option;
        do{
            int customerId = customers.get(choice - 1).getId();
            var rentedCarId = customers.get(choice - 1).getRentedCarId();
            System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    rentMenu(customerId, rentedCarId);
                    break;
                case 2:
                    returnRentedCar(customerId, rentedCarId);
                    break;
                case 3:
                    myRentedCar(customerId, rentedCarId);
                    break;
            }
            customers = connector.getCustomers();
        } while (option != 0);
    }

    private Status rentMenu(int customerId, int rentedCarId) throws SQLException {
        int choice;
        List<Company> companies = connector.getCompanies();
        List<Car> cars;
        if (rentedCarId != NULL) {
            System.out.println("You've already rented a car!");
            return Status.CUSTOMER_LOGIN;
        }
        if (companies.size() > 0) {
            System.out.println("Choose the company:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, companies.get(i).getName());
            }
            System.out.println("0. Back");
            choice = scanner.nextInt();
            if (choice == 0) {
                return Status.MAIN;
            } else {
                cars = connector.getRentableCars();
                System.out.println("Choose a car:");
                if (cars.size() != 0) {
                    for (int i = 1; i <= cars.size(); i++) {
                        System.out.printf("%d. %s\n", i, cars.get(i - 1).getName());
                    }
                    System.out.println("0. Back");
                    choice = scanner.nextInt();
                    if (choice == 0) {
                        return Status.MAIN;
                    } else {
                        int carId = cars.get(choice - 1).getId();
                        connector.rentCar(carId, customerId);
                        System.out.printf("You rented '%s'\n", cars.get(choice - 1).getName());
                    }
                } else {
                    System.out.println("The car list is empty!");
                }
            }
        } else {
            System.out.println("The company list is empty!");
        }
        System.out.println();
        return Status.CUSTOMER_LOGIN;
    }
    void returnRentedCar(int customerId, int rentedCarId) throws SQLException {
        if (rentedCarId == NULL) {
            System.out.println("You didn't rent a car!");
        } else {
            connector.returnRentedCar(customerId);
            System.out.println("You've returned a rented car!");
        }
        System.out.println();
    }

    void myRentedCar(int customerId, int rentedCarId) throws SQLException {
        List<String> carDetails;
        if (rentedCarId == NULL) {
            System.out.println("You didn't rent a car!");
        } else {
            carDetails = connector.showMyRentedCar(customerId);
            System.out.printf("Your rented car:\n%s\nCompany:\n%s\n", carDetails.get(0), carDetails.get(1));
        }
        System.out.println();
    }
}