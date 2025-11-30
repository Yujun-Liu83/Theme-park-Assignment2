import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
// New imports: Required for file handling, date formatting, and exception handling
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
// New imports: Required for file reading functionality
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets; // Handles character encoding to avoid garbled Chinese characters
import java.util.Iterator;
import java.time.format.DateTimeParseException;

/**
 * Ride class - Implements RideInterface to manage amusement ride operations in a theme park
 * Encapsulates ride properties, FIFO waiting queue, unique ride history, and end-to-end operational logic
 * Supports core functionalities:
 * - Queue management (add/remove/print visitors in FIFO order)
 * - Ride history tracking (add unique visitors, check duplicates, print/sort history)
 * - Complete ride cycle execution (precondition checks, visitor boarding, ride simulation, history update)
 * - Ride history export (exports structured history to CSV-formatted text file with auto-generated filename)
 * - Ride history import (imports valid visitors from CSV files exported by exportRideHistory(), with strict data validation)
 */
public class Ride implements RideInterface {
    // Immutable fields (final modifier: reference cannot be reassigned after initialization)
    private final String rideName;          // Unique name of the ride (e.g., "Speed Demon", "Velocity X")
    private final int maxCapacity;          // Maximum number of visitors per ride cycle (≥1, validated on init)
    private final Queue<Visitor> waitingLine;  // FIFO queue for visitors waiting to ride (LinkedList for efficient FIFO ops)
    private final LinkedList<Visitor> rideHistory; // Stores unique visitors who have ridden (explicit Visitor generic type)

    // Mutable fields (no final modifier: values can be updated during runtime)
    private Employee operator;              // Assigned ride operator (can be reassigned if needed)
    private int numOfCycles;                // Total number of completed ride cycles (auto-increments after each cycle)

    /**
     * Parameterized constructor to initialize all ride properties
     * Validates max capacity to ensure it's at least 1 (defaults to 2 if invalid)
     * Initializes collection objects (queue/history) to avoid null pointer exceptions
     * @param rideName Unique name of the ride
     * @param maxCapacity Maximum visitors per cycle (falls back to 2 if value is less than 1)
     * @param operator Employee assigned to operate the ride (can be null initially)
     */
    public Ride(String rideName, int maxCapacity, Employee operator) {
        this.rideName = rideName;
        // Enforce minimum capacity constraint to ensure operational validity; fallback to 2 if input is invalid
        this.maxCapacity = (maxCapacity >= 1) ? maxCapacity : 2;
        this.operator = operator;
        // LinkedList is chosen for queue due to O(1) time complexity for add/remove operations at both ends, ideal for FIFO
        this.waitingLine = new LinkedList<>();
        // LinkedList is selected for history to support efficient sorting, iteration, and element insertion
        this.rideHistory = new LinkedList<>();
        this.numOfCycles = 0; // Initialize completed cycles to zero
    }

    // ------------------- Fixed Sorting Method (Generic Type Matching) -------------------

    /**
     * Sorts the ride history using custom rules defined in the VisitorComparator class
     * Handles empty history case with user-friendly error feedback to avoid unnecessary operations
     * Explicitly specifies Comparator<Visitor> to resolve generic type mismatch issues
     * Ensures type safety and compatibility with the rideHistory collection
     */
    public void sortRideHistory() {
        if (rideHistory.isEmpty()) {
            System.out.println("Error: Cannot sort empty ride history for " + rideName);
            return;
        }
        // Explicitly cast to Comparator<Visitor> to resolve generic type inference issues and ensure type safety
        Collections.sort(rideHistory, (Comparator<Visitor>) new VisitorComparator());
        System.out.println("Success: Ride history for " + rideName + " sorted");
    }

    // ------------------- New: exportRideHistory() Method -------------------
    /**
     * Exports the ride history to a CSV-formatted text file for persistent storage and analysis
     * Auto-generates filename with ride name and current date to ensure uniqueness and readability
     * Handles edge cases (empty history, file I/O errors) with user-friendly feedback
     * File format: Comma-Separated Values (CSV) - compatible with spreadsheet tools (Excel, Google Sheets)
     * Filename structure: [SanitizedRideName]_RideHistory_YYYY-MM-DD.txt
     * @return boolean: true if export is successful; false if export fails (empty history or I/O error)
     */
    public boolean exportRideHistory() {
        System.out.println("\n=== Exporting Ride History for " + rideName + " ===");

        // Prevent unnecessary file I/O operation by checking for empty history first
        if (rideHistory.isEmpty()) {
            System.out.println("Error: Cannot export empty ride history!");
            return false;
        }

        // Generate unique filename to avoid overwrites and ensure cross-system compatibility
        LocalDate today = LocalDate.now(); // Use date without time component for filename consistency
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // ISO standard format for readability
        String dateStr = today.format(dateFormat);
        // Sanitize ride name by replacing non-alphanumeric/non-underscore characters to avoid file path errors
        String safeRideName = rideName.replaceAll("[^a-zA-Z0-9_]", "_");
        String fileName = safeRideName + "_RideHistory_" + dateStr + ".txt";

        // Try-with-resources auto-closes BufferedWriter, preventing resource leaks and simplifying error handling
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write CSV header to define column structure for spreadsheet compatibility
            writer.write("Name,Age,ContactNumber,TicketId,VisitDate");
            writer.newLine();

            // Convert non-string attributes (e.g., age) to string before CSV construction to maintain format consistency
            for (Visitor visitor : rideHistory) {
                String csvRow = String.join(",",
                        visitor.getName(),
                        String.valueOf(visitor.getAge()), // Explicit conversion to avoid type mismatch in CSV
                        visitor.getContactNumber(),
                        visitor.getTicketId(),
                        visitor.getVisitDate()
                );
                writer.write(csvRow);
                writer.newLine();
            }

            System.out.println("Success! Ride history exported to: " + fileName);
            // Print absolute path using user.dir system property to help users locate the file easily
            System.out.println("File path: " + System.getProperty("user.dir") + "/" + fileName);
            return true;

        } catch (IOException e) {
            // Catch and report file I/O exceptions (e.g., permission issues, disk full) with meaningful message
            System.out.println("Error exporting ride history: " + e.getMessage());
            return false;
        }
    }

    // ------------------- New: importRideHistory() Method -------------------
    /**
     * Imports ride history from a CSV-formatted text file (generated by exportRideHistory() for compatibility)
     * Implements strict data validation to ensure only valid, complete visitor records are imported
     * Skips header line, empty lines, and invalid entries (with detailed error feedback for debugging)
     * Uses UTF-8 character encoding to support Chinese characters and avoid garbled text
     * @param filePath Full or relative path of the CSV file to import (e.g., "Giant_Ferris_Wheel_RideHistory_2025-11-27.txt")
     * @return int: Number of successfully imported visitors (excludes skipped/invalid entries)
     */
    public int importRideHistory(String filePath) {
        System.out.println("\n=== Importing Ride History from: " + filePath + " ===");

        int successCount = 0;
        int skipCount = 0;
        int lineNumber = 0;

        // Validate input filePath to prevent null/empty path errors
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("Error: File path cannot be empty!");
            return 0;
        }

        // Specify UTF-8 encoding explicitly to support multi-byte characters (e.g., Chinese) and avoid garbled text
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip CSV header line (matches the format written in exportRideHistory())
                if (lineNumber == 1) {
                    System.out.println("Skipped header line: " + line);
                    continue;
                }

                // Trim whitespace and skip empty lines to avoid processing invalid blank entries
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty()) {
                    System.out.println("Line " + lineNumber + ": Empty line, skipped");
                    skipCount++;
                    continue;
                }

                // Split line by comma (CSV standard delimiter) to extract fields
                String[] fields = trimmedLine.split(",");

                // Validate field count matches export format (5 fields required) to ensure data integrity
                if (fields.length != 5) {
                    System.out.println("Line " + lineNumber + ": Invalid field count (" + fields.length + "/5 required), skipped");
                    skipCount++;
                    continue;
                }

                // Trim individual fields to remove accidental whitespace and sanitize input
                String name = fields[0].trim();
                String ageStr = fields[1].trim();
                String contactNumber = fields[2].trim();
                String ticketId = fields[3].trim();
                String visitDate = fields[4].trim();

                // Validate visit date format against ISO_LOCAL_DATE (YYYY-MM-DD) to ensure chronological consistency
                try {
                    LocalDate.parse(visitDate, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    System.out.println("Line " + lineNumber + ": Invalid visit date format (" + visitDate + "), must be YYYY-MM-DD, skipped");
                    skipCount++;
                    continue;
                }

                // Validate mandatory fields (Name/TicketID/VisitDate) to prevent incomplete visitor records
                if (name.isEmpty() || ticketId.isEmpty() || visitDate.isEmpty()) {
                    System.out.println("Line " + lineNumber + ": Mandatory fields (Name/TicketID/VisitDate) cannot be empty, skipped");
                    skipCount++;
                    continue;
                }

                // Validate age is a non-negative integer (logical constraint for visitors)
                int age;
                try {
                    age = Integer.parseInt(ageStr); // Parse string to integer; handles numeric format errors
                    if (age < 0) {
                        System.out.println("Line " + lineNumber + ": Invalid age - Cannot be negative (" + ageStr + "), skipped");
                        skipCount++;
                        continue;
                    }
                } catch (NumberFormatException e) {
                    // Handle non-integer age values (e.g., strings, decimals) with clear error message
                    System.out.println("Line " + lineNumber + ": Invalid age format (" + ageStr + ") - Must be an integer, skipped");
                    skipCount++;
                    continue;
                }

                // All validations passed: Create Visitor object and add to history
                Visitor visitor = new Visitor(name, age, contactNumber, ticketId, visitDate);
                rideHistory.add(visitor);
                successCount++;
                System.out.println("Line " + lineNumber + ": Successfully imported - " + visitor.getName() + " (Ticket ID: " + visitor.getTicketId() + ")");
            }

            // Print import summary to provide transparency on processing results
            System.out.println("\n=== Import Completed ===");
            System.out.println("Total lines processed (excluding header): " + (lineNumber - 1));
            System.out.println("Successfully imported visitors: " + successCount);
            System.out.println("Skipped invalid/empty lines: " + skipCount);
            System.out.println("Updated ride history size: " + rideHistory.size());

        } catch (IOException e) {
            // Handle file access errors (e.g., file not found, permission denied) with actionable feedback
            System.out.println("Error importing file: " + e.getMessage());
        }

        return successCount;
    }

    // ------------------- Part4A: Ride History Management Methods -------------------

    /**
     * Adds a unique visitor to the ride history (prevents duplicate entries via ticket ID)
     * Skips null visitors to maintain data integrity and provides console feedback
     * @param visitor Visitor to add to history (must have a unique ticket ID to be added)
     */
    @Override
    public void addVisitorToHistory(Visitor visitor) {
        // Double validation: Ensure non-null input and unique ticket ID to maintain history data integrity
        if (visitor != null && !checkVisitorFromHistory(visitor)) {
            rideHistory.add(visitor);
            System.out.println("Added " + visitor.getName() + " to history");
        } else if (visitor != null) {
            System.out.println(visitor.getName() + " is already in history");
        }
    }

    /**
     * Checks if a visitor exists in the ride history using ticket ID as the unique identifier
     * Safely handles null cases for input visitor, stored visitors, and their ticket IDs
     * Prevents NullPointerException and ensures reliable duplicate detection
     * @param visitor Visitor to verify against the ride history
     * @return boolean: true if visitor exists in history (matching ticket ID), false otherwise
     */
    @Override
    public boolean checkVisitorFromHistory(Visitor visitor) {
        if (visitor == null) return false; // Null input safety: Avoid NPE by returning false immediately
        // Null-safe checks for stored Visitor and TicketID to prevent NullPointerException during iteration
        for (Visitor v : rideHistory) {
            if (v != null && v.getTicketId() != null
                    && v.getTicketId().equals(visitor.getTicketId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the ride history in a structured, human-readable format
     * Displays comprehensive details for each visitor (Person and Visitor-specific attributes)
     * Shows a user-friendly message if the history is empty
     */
    @Override
    public void printRideHistory() {
        System.out.println("\nRide History for " + rideName + ":");
        if (rideHistory.isEmpty()) {
            System.out.println("  No visitors in history");
            return;
        }
        int index = 1;
        // Iterator is used to support potential element removal during iteration (future extensibility) and stable traversal
        Iterator<Visitor> iterator = rideHistory.iterator();
        while (iterator.hasNext()) {
            Visitor v = iterator.next();
            if (v != null) {
                System.out.println(index + ". Person [Name: " + v.getName()
                        + ", Age: " + v.getAge()
                        + ", Contact: " + v.getContactNumber()
                        + "] | Visitor [Ticket ID: " + v.getTicketId()
                        + ", Visit Date: " + v.getVisitDate() + "]");
                index++;
            }
        }
    }

    /**
     * Returns the total number of unique visitors in the ride history
     * @return int: Total count of unique visitors recorded in the ride history
     */
    @Override
    public int numberOfVisitors() {
        return rideHistory.size();
    }

    // ------------------- Part3: Queue Management Methods -------------------

    /**
     * Adds a visitor to the waiting queue in FIFO (First-In-First-Out) order
     * Skips null visitors to maintain queue integrity and avoid runtime errors
     * @param visitor Visitor to add to the waiting queue
     */
    @Override
    public void addVisitorToQueue(Visitor visitor) {
        // Null check prevents adding invalid null elements to the queue, avoiding issues in subsequent operations
        if (visitor != null) {
            waitingLine.add(visitor);
            System.out.println("Success: " + visitor.getName() + " added to queue for " + rideName);
        } else {
            System.out.println("Error: Cannot add null visitor to queue");
        }
    }

    /**
     * Removes and returns the first visitor from the waiting queue (FIFO order)
     * Uses poll() to safely handle empty queues (returns null instead of throwing NoSuchElementException)
     */
    @Override
    public void removeVisitorFromQueue() {
        // poll() is used instead of remove() to handle empty queue gracefully (no exception thrown)
        if (!waitingLine.isEmpty()) {
            Visitor removed = waitingLine.poll();
            System.out.println("Success: " + removed.getName() + " removed from queue");
        } else {
            System.out.println("Error: Queue is empty, cannot remove visitor");
        }
    }

    /**
     * Prints the current waiting queue in a structured, human-readable format
     * Displays queue size and comprehensive details for each waiting visitor
     * Shows a user-friendly message if the queue is empty
     */
    @Override
    public void printQueue() {
        System.out.println("\nQueue for " + rideName + " (size: " + waitingLine.size() + "):");
        if (waitingLine.isEmpty()) {
            System.out.println("  No visitors in queue");
            return;
        }
        int index = 1;
        for (Visitor v : waitingLine) {
            // Print both inherited (Person) and specific (Visitor) attributes for complete visitor context
            System.out.println(index + ". Person [Name: " + v.getName()
                    + ", Age: " + v.getAge()
                    + ", Contact: " + v.getContactNumber()
                    + "] | Visitor [Ticket ID: " + v.getTicketId()
                    + ", Visit Date: " + v.getVisitDate() + "]");
            index++;
        }
    }

    // ------------------- Enhanced runOneCycle() Method -------------------

    /**
     * Executes a complete ride cycle with precondition checks, visitor boarding, and post-cycle updates
     * Follows the assignment's requirements for end-to-end ride operation:
     * 1. Validate critical preconditions (operator assigned + visitors in queue)
     * 2. Board visitors up to the ride's maximum capacity (FIFO order)
     * 3. Simulate ride operation with operator information
     * 4. Add boarded visitors to ride history (reuses existing duplicate prevention logic)
     * 5. Update cycle count and print cycle summary
     */
    @Override
    public void runOneCycle() {
        System.out.println("\n=== Starting One Cycle of " + rideName + " ===");

        // Critical precondition checks: Ensure ride has an operator and waiting visitors before proceeding
        if (this.operator == null) {
            System.out.println("Error: Cannot run " + rideName + " - No operator assigned!");
            return;
        }
        if (waitingLine.isEmpty()) {
            System.out.println("Error: Cannot run " + rideName + " - No visitors in queue!");
            return;
        }

        // Calculate number of visitors to board: Cap at max capacity or use remaining queue size (whichever is smaller)
        int availableSlots = this.maxCapacity;
        int visitorsToBoard = Math.min(availableSlots, waitingLine.size());
        List<Visitor> boardedVisitors = new ArrayList<>(); // Temporary storage for cycle-specific visitors

        // Board visitors in FIFO order: poll() removes and retrieves head of queue (compliant with queue behavior)
        for (int i = 0; i < visitorsToBoard; i++) {
            Visitor visitor = waitingLine.poll();
            boardedVisitors.add(visitor);
            System.out.println("Boarded: " + visitor.getName() + " (Ticket ID: " + visitor.getTicketId() + ")");
        }

        // Simulate ride operation with operator context
        System.out.println("\n" + rideName + " is running! Enjoy the ride, visitors!");
        System.out.println("Operator: " + operator.getName() + " (Specialization: " + operator.getRideSpecialization() + ")");

        // Reuse addVisitorToHistory() to leverage existing duplicate prevention logic (avoid code redundancy)
        for (Visitor v : boardedVisitors) {
            addVisitorToHistory(v);
        }
        this.numOfCycles++; // Increment cycle count only after successful ride completion

        // Print cycle summary to provide transparency on operation results
        System.out.println("\n=== Cycle " + numOfCycles + " Completed For " + rideName + " ===");
        System.out.println("Total visitors boarded this cycle: " + visitorsToBoard);
        System.out.println("Total cycles run: " + numOfCycles);
        System.out.println("Remaining visitors in queue: " + waitingLine.size());
    }

    // ------------------- New: Getter Method for Cycle Count -------------------

    /**
     * Returns the total number of completed ride cycles
     * Provides access to the numOfCycles field for test validation and reporting
     * @return int: Total number of successfully completed ride cycles
     */
    public int getNumOfCycles() {
        return this.numOfCycles;
    }
}