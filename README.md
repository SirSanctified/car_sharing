# car_sharing
This is a program that manages a car-sharing service allowing companies to rent out their cars and find customers

In this project, I will work with the H2 database. It is an open-source lightweight Java database that can be embedded in Java applications or run in the client-server mode. Mainly, the H2 database can be configured to run as an in-memory database, which means that data will not persist on the disk. Because of the embedded database, it is not used for product development but mostly for development and testing purposes.

First I create a single table named COMPANY with the following columns:

    ID with the type INT;
    NAME with the type VARCHAR.

After running the program, it should create the database file in the carsharing/db/ folder, initialize the table described above, and stop.

The database file name is obtained from the command-line arguments. Here is an example of args: -databaseFileName carsharing. If the -databaseFileName argument is not given, then the database file name can be anything, in this case `test`.

Second, I create a table CAR that relates to a particular company. The data in this table should be linked to the COMPANY table with a foreign key.

Since one company can have more than one car but one car can only belong to one company, the appropriate table relation model is One to Many.

The table CAR will have the following columns:

    ID column which should be PRIMARY KEY and AUTO_INCREMENT with the type INT.
    NAME column which should be UNIQUE and NOT NULL with the type VARCHAR.
    COMPANY_ID column which should be NOT NULL with the type INT. This column should be a FOREIGN KEY referring to the ID column of the table COMPANY.
    
The companies are all set and ready to rent out their cars. I then create a log-in option for the customers so that they can rent a car through the platform. I also create a table named CUSTOMER with the following columns:

    ID column which should be PRIMARY KEY and AUTO_INCREMENT with the type INT.
    NAME column which should be UNIQUE and NOT NULL with the type VARCHAR.
    RENTED_CAR_ID which should have the type INT. This column should be a FOREIGN KEY referring to the ID column of the CAR table, and this column can be NULL in case the customer didn't rent a car.

Example

The greater-than symbol followed by a space > represents the user input. Note that it's not part of the input.

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 2

The customer list is empty!

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 3

Enter the customer name:
> First customer
The customer was added!

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 3

Enter the customer name:
> Second customer
The customer was added!

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 2

Customer list:
1. First customer
2. Second customer
0. Back
> 1

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 3

You didn't rent a car!

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 2

You didn't rent a car!

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 0

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 0

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 2

The customer list:
1. First customer
2. Second customer
0. Back
> 1

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 1

Choose a company:
1. Car To Go
2. Drive Now
0. Back
> 1

Choose a car:
1. Hyundai Venue
2. Maruti Suzuki Dzire
0. Back
> 1

You rented 'Hyundai Venue'

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 3

Your rented car:
Hyundai Venue
Company:
Car To Go

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 1

You've already rented a car!

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 2

You've returned a rented car!

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 0

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 0
