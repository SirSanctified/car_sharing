package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface Connectible {
    void connect(String url) throws ClassNotFoundException, SQLException;
    void createCompanyTable() throws SQLException;
    void createCarTable() throws SQLException;
    void createCustomerTable() throws SQLException;
    List<Company> getCompanies() throws SQLException;
    List<Car> getCars(int companyId) throws SQLException;
    List<Customer> getCustomers() throws SQLException;
    void createCompany(String query) throws SQLException;
    void addCar(String carName, int companyId) throws SQLException;
    void createCustomer(String name) throws SQLException;
    void closeConnection() throws SQLException;
    void rentCar(int carId, int customerId) throws SQLException;
    void returnRentedCar(int customerId) throws SQLException;
    List<String> showMyRentedCar(int customerId) throws SQLException;
    List<Car> getRentableCars() throws SQLException;
}
