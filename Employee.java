/**
 * Employee class - Subclass of Person, representing staff members in the theme park (primarily ride operators)
 * Inherits core human attributes (name, age, contact number) from the Person superclass
 * Adds employee-specific properties to manage staff identification and operational responsibilities
 */
public class Employee extends Person {
    // Employee-specific attributes (encapsulated as private fields to ensure data security)
    private String employeeId;         // Unique identifier for the employee (e.g., "EMP001")
    // Serves as a primary key for staff management and ride-operator association
    private String rideSpecialization; // The specific type of amusement ride the employee is qualified to operate
    // Examples: "Roller Coaster", "Water Ride", "Ferris Wheel"

    /**
     * Default constructor (no-argument constructor)
     * Invokes the superclass (Person) default constructor to initialize inherited attributes
     * Sets employee-specific fields to default "Unknown" values when no parameters are provided
     */
    public Employee() {
        super(); // Calls the no-arg constructor of the Person parent class
        this.employeeId = "Unknown";         // Default employee ID if not specified
        this.rideSpecialization = "Unknown"; // Default specialization if not specified
    }

    /**
     * Parameterized constructor
     * Initializes both inherited attributes (via Person's parameterized constructor) and employee-specific attributes
     * Enables complete setup of an Employee object with all required details
     * @param name Full name of the employee (inherited from Person)
     * @param age Age of the employee (inherited from Person, with validation in the superclass)
     * @param contactNumber Contact phone number of the employee (inherited from Person)
     * @param employeeId Unique employee ID (e.g., "EMP001") - should be system-generated for uniqueness
     * @param rideSpecialization The ride type the employee is certified to operate (e.g., "Roller Coaster")
     */
    public Employee(String name, int age, String contactNumber, String employeeId, String rideSpecialization) {
        super(name, age, contactNumber); // Passes inherited attributes to the Person constructor
        this.employeeId = employeeId;         // Assigns unique staff identifier
        this.rideSpecialization = rideSpecialization; // Assigns operational specialization
    }

    // Getter and Setter methods (implement encapsulation: controlled access to private fields)

    /**
     * Retrieves the employee's unique ID
     * @return String: Unique employee identifier (e.g., "EMP001")
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Updates the employee's unique ID
     * @param employeeId New unique ID (should be validated for uniqueness in a production system)
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Retrieves the employee's ride operation specialization
     * @return String: Certified ride type (e.g., "Water Ride")
     */
    public String getRideSpecialization() {
        return rideSpecialization;
    }

    /**
     * Updates the employee's ride operation specialization
     * @param rideSpecialization New certified ride type (reflects staff training/qualifications)
     */
    public void setRideSpecialization(String rideSpecialization) {
        this.rideSpecialization = rideSpecialization;
    }

    /**
     * Overrides the toString() method from the Person superclass
     * Combines inherited attribute details (from Person) with employee-specific information
     * Provides a human-readable string representation for debugging, logging, and user-facing displays
     * @return String: Formatted string with complete employee details
     */
    @Override
    public String toString() {
        return super.toString() + " | Employee [ID: " + employeeId + ", Specialization: " + rideSpecialization + "]";
    }
}