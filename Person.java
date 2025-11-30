/**
 * Abstract superclass representing a general "Person" in the theme park management system
 * Serves as a base class for specialized subclasses: Employee (staff) and Visitor (guests)
 * Encapsulates common attributes and behaviors shared by all people in the system
 * Abstract class特性: Cannot be instantiated directly; must be extended by concrete subclasses
 */
public abstract class Person {
    // Core common attributes for all persons (encapsulated as private fields for data integrity)
    private String name;          // Full name of the person (e.g., "John Doe", "Jane Smith")
    private int age;              // Age of the person (non-negative integer per validation rules)
    private String contactNumber; // Contact phone number (for emergency communication or notifications)

    /**
     * Default constructor (no-argument constructor)
     * Initializes all common attributes with default "Unknown" or 0 values
     * Used when no specific details are provided during object creation
     */
    public Person() {
        this.name = "Unknown";       // Default name if not specified
        this.age = 0;                // Default age (represents "unspecified")
        this.contactNumber = "Unknown"; // Default contact if not provided
    }

    /**
     * Parameterized constructor
     * Initializes all common attributes with user-provided values
     * Ensures complete setup of the Person's core details during object creation
     * @param name Full name of the person
     * @param age Age of the person (to be validated in setAge() method)
     * @param contactNumber Contact phone number (e.g., "123-456-7890")
     */
    public Person(String name, int age, String contactNumber) {
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
    }

    // Getter and Setter methods (implement encapsulation: controlled access to private fields)
    // Ensure data integrity by restricting direct modification of attributes

    /**
     * Retrieves the person's full name
     * @return String: Full name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the person's full name
     * @param name New full name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the person's age
     * @return int: Age of the person (non-negative)
     */
    public int getAge() {
        return age;
    }

    /**
     * Updates the person's age with data validation
     * Business rule: Age cannot be a negative number (complies with real-world logic)
     * Prints an error message if invalid input is provided
     * @param age New age to assign (must be ≥ 0)
     */
    public void setAge(int age) {
        // Simple validation: Reject negative age values
        if (age >= 0) {
            this.age = age; // Update age only if validation passes
        } else {
            System.out.println("Error: Age cannot be negative"); // Error feedback for invalid input
        }
    }

    /**
     * Retrieves the person's contact phone number
     * @return String: Contact number (e.g., "123-456-7890")
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Updates the person's contact phone number
     * @param contactNumber New contact number to assign
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Overrides the default toString() method
     * Returns a human-readable string representation of the Person object
     * Combines all common attributes for easy debugging, logging, and user-facing displays
     * @return String: Formatted string with name, age, and contact number
     */
    @Override
    public String toString() {
        return "Person [Name: " + name + ", Age: " + age + ", Contact: " + contactNumber + "]";
    }
}