package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connector implements Connectible{
    Connection conn = null;
    Statement st = null;
    @Override
    public void connect(String url) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection(url);
        st = conn.createStatement();
        conn.setAutoCommit(true);
    }

    @Override
    public void createCompanyTable() throws SQLException {
        st.execute("ALTER TABLE IF EXISTS company " +
                "ALTER COLUMN id RESTART WITH 1");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY (" +
                "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR UNIQUE NOT NULL )");
    }

    @Override
    public void createCarTable() throws SQLException {
        st.execute("ALTER TABLE IF EXISTS CAR " +
                "ALTER COLUMN id RESTART WITH 1");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS CAR (" +
                "`ID` INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "`NAME` VARCHAR UNIQUE NOT NULL ," +
                "`COMPANY_ID` INTEGER NOT NULL," +
                "FOREIGN KEY (COMPANY_ID)" +
                " REFERENCES COMPANY (ID))");
    }

    @Override
    public void createCustomerTable() throws SQLException {
        st.execute("ALTER TABLE IF EXISTS customer " +
                "ALTER COLUMN id RESTART WITH  1");
        st.executeUpdate("CREATE TABLE  IF NOT EXISTS customer(" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                " name VARCHAR UNIQUE NOT NULL, " +
                "rented_car_id INTEGER," +
                " FOREIGN KEY (rented_car_id) REFERENCES car(id));");
    }

    @Override
    public List<Company> getCompanies() throws SQLException {
        String query = "SELECT * FROM COMPANY ORDER BY ID";
        ResultSet resultSet = st.executeQuery(query);
        List<Company> companies = new ArrayList<>();
        while (resultSet.next()) {
            companies.add(new Company(resultSet.getString(2), resultSet.getInt(1)));
        }
        return companies;
    }

    @Override
    public List<Car> getCars(int companyId) throws SQLException {
        String query = String.format("SELECT * FROM CAR " +
                "WHERE COMPANY_ID = %d " +
                "ORDER BY ID", companyId);
        ResultSet rs = st.executeQuery(query);
        List<Car> cars = new ArrayList<>();
        while (rs.next()) {
            cars.add(new Car(rs.getInt(1), rs.getString(2), rs.getInt(3)));
        }
        return cars;
    }

    @Override
    public List<Customer> getCustomers() throws SQLException {
        ResultSet resultSet = st.executeQuery("SELECT * FROM customer " +
                "ORDER BY id");
        List<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(new Customer(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
        }
        return customers;
    }

    @Override
    public void createCompany(String name) throws SQLException {
        String query = String.format("INSERT INTO COMPANY (name)" +
                " VALUES ('%s');", name);
        st.executeUpdate(query);
        System.out.println("The company was created!");
        System.out.println();
    }

    @Override
    public void addCar(String carName, int companyId) throws SQLException {
        String query = String.format("INSERT INTO CAR (NAME, COMPANY_ID) " +
                "VALUES('%s', '%d');", carName, companyId);
        st.executeUpdate(query);
        System.out.println("The car was added!");
        System.out.println();
    }

    @Override
    public void createCustomer(String name) throws SQLException {
       String query = String.format("INSERT INTO CUSTOMER (name) " +
               "VALUES('%s')", name);
       st.executeUpdate(query);
        System.out.println("The customer was added!");
        System.out.println();
    }

    @Override
    public void closeConnection() throws SQLException {
        if (conn!= null && st != null) {
            st.close();
            conn.close();
        }
    }

    @Override
    public void rentCar(int carId, int customerId) throws SQLException {
        String query = String.format("UPDATE customer " +
                "SET rented_car_id = %d " +
                "WHERE id = %d", carId, customerId);
        st.executeUpdate(query);
    }

    @Override
    public void returnRentedCar(int customerId) throws SQLException {
        String query = String.format("UPDATE customer " +
                "SET rented_car_id = NULL " +
                "WHERE id = %d", customerId);
        st.executeUpdate(query);
    }

    @Override
    public List<Car> getRentableCars() throws SQLException {
        List<Car> rentableCars = new ArrayList<>();
        String query = "SELECT car.id, car.name, car.company_id " +
                "FROM car LEFT JOIN customer" +
                " On car.id = customer.rented_car_id " +
                "WHERE customer.name IS NULL";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            rentableCars.add(new Car(rs.getInt(1), rs.getString(2), rs.getInt(3)));
        }
        return rentableCars;
    }

    @Override
    public List<String> showMyRentedCar(int customerId) throws SQLException {
        String companyQuery;
        String carQuery;
        int carId;
        int companyId;
        List<String> carDetails = new ArrayList<>();
        String customerQuery = String.format("SELECT rented_car_id FROM customer " +
                "WHERE id = %d", customerId);
        ResultSet rs = st.executeQuery(customerQuery);
        rs.next();
        carId = rs.getInt(1);
        carQuery = String.format("SELECT * FROM car" +
                " WHERE id = %d", carId);
        rs = st.executeQuery(carQuery);
        rs.next();
        carDetails.add(rs.getString(2));
        companyId = rs.getInt(3);
        companyQuery = String.format("SELECT name FROM company " +
                "where id = %d", companyId);
        rs = st.executeQuery(companyQuery);
        rs.next();
        carDetails.add(rs.getString(1));
        return carDetails;
    }
}
