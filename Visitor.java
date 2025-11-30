/**
 * Visitor class - Subclass of Person, representing visitors to the theme park
 * Inherits common human attributes (name, age, contact number) from the Person superclass
 * Adds visitor-specific attributes and behaviors tailored to theme park guest management
 */
public class Visitor extends Person {
    // Visitor-specific attributes (encapsulated as private fields for data integrity)
    private String ticketId;    // Unique ticket identifier for the visitor (e.g., "TICK001")
    // Serves as a primary key for visitor tracking in queues/history
    private String visitDate;   // Date of the visitor's park entry (format: YYYY-MM-DD, e.g., "2025-12-01")
    // Used for validating visit validity and sorting history records

    /**
     * Default constructor (no-argument constructor)
     * Calls the superclass (Person) default constructor to initialize inherited attributes
     * Sets visitor-specific attributes to default "Unknown" values
     */
    public Visitor() {
        super(); // Invokes the no-arg constructor of the Person class
        this.ticketId = "Unknown";  // Default ticket ID if not specified
        this.visitDate = "Unknown"; // Default visit date if not specified
    }

    /**
     * Parameterized constructor
     * Initializes both inherited attributes (via superclass constructor) and visitor-specific attributes
     * Ensures complete initialization of the Visitor object with user-provided data
     * @param name Full name of the visitor (inherited from Person)
     * @param age Age of the visitor (inherited from Person, with validation in superclass)
     * @param contactNumber Contact phone number of the visitor (inherited from Person)
     * @param ticketId Unique ticket ID (e.g., "TICK001")
     * @param visitDate Entry date in "YYYY-MM-DD" format (e.g., "2025-12-01")
     */
    public Visitor(String name, int age, String contactNumber, String ticketId, String visitDate) {
        super(name, age, contactNumber); // Passes inherited attributes to Person's parameterized constructor
        this.ticketId = ticketId;        // Assigns unique ticket ID
        this.visitDate = visitDate;      // Assigns visit date (format enforced by usage in other components)
    }

    // Getter and Setter methods (implement encapsulation: controlled access to private fields)

    /**
     * Retrieves the visitor's unique ticket ID
     * @return String: Unique ticket identifier (e.g., "TICK001")
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * Updates the visitor's ticket ID
     * @param ticketId New unique ticket ID (should be system-generated to ensure uniqueness)
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * Retrieves the visitor's park entry date
     * @return String: Visit date in "YYYY-MM-DD" format (e.g., "2025-12-01")
     */
    public String getVisitDate() {
        return visitDate;
    }

    /**
     * Updates the visitor's park entry date
     * @param visitDate New entry date (recommended format: "YYYY-MM-DD" for consistency)
     */
    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    /**
     * Overrides the toString() method from the Person superclass
     * Combines inherited attribute string (from Person) with visitor-specific details
     * Provides a human-readable representation of the Visitor object for debugging/logging
     * @return String: Formatted string with complete visitor information
     */
    @Override
    public String toString() {
        return super.toString() + " | Visitor [Ticket ID: " + ticketId + ", Visit Date: " + visitDate + "]";
    }
}