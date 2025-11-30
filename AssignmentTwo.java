/**
 * Main test class for the Theme Park Management System
 * Focuses on testing all core functionalities required by the assignment
 * Contains test methods corresponding to each assignment part (Part 3, 4A, 4B, 5, 6, 7)
 * Executes comprehensive test cases to verify:
 * - Queue management (FIFO operations)
 * - Ride history tracking (unique entries + duplicate prevention)
 * - Sorted ride history (custom sorting rules)
 * - Ride cycle execution (operator validation, capacity control, queue processing)
 * - Ride history export (to CSV-formatted text file for persistent storage)
 * - Ride history import (from CSV file exported by exportRideHistory())
 */
public class AssignmentTwo {
    /**
     * Part 3: Test Queue Management Functionality
     * Follows the exact assignment requirements to validate FIFO queue operations:
     * 1. Create a ride operator (Employee) and an amusement ride (Ride)
     * 2. Create 5 valid Visitor objects (meets the "at least 5 visitors" requirement)
     * 3. Add all visitors to the ride's waiting queue (FIFO order is enforced)
     * 4. Print the waiting queue to confirm visitors are added in the correct sequence
     */
    public void partThree() {
        System.out.println("\n=== Part 3: Queue Management Test ===");

        // 1. Create ride operator (Employee) and amusement ride (Ride)
        // Employee constructor params: name, age, contactNumber, employeeId, rideSpecialization
        Employee operator = new Employee("Mike Johnson", 40, "555-1234", "EMP002", "Water Ride");
        // Ride constructor params: rideName, maxCapacity (3 visitors per cycle), assigned operator
        Ride waterRide = new Ride("Splash Mountain", 3, operator);

        // 2. Create 5 test visitors (fulfills the "at least 5 visitors" assignment requirement)
        // Visitor constructor params: name, age, contactNumber, ticketId (unique), visitDate (format: YYYY-MM-DD)
        Visitor v1 = new Visitor("Alice Brown", 18, "555-5678", "TICKET002", "2025-11-21");
        Visitor v2 = new Visitor("Bob Wilson", 22, "555-9012", "TICKET003", "2025-11-21");
        Visitor v3 = new Visitor("Charlie Davis", 30, "555-3456", "TICKET004", "2025-11-21");
        Visitor v4 = new Visitor("Diana Evans", 25, "555-7890", "TICKET005", "2025-11-21");
        Visitor v5 = new Visitor("Ethan Foster", 19, "555-2345", "TICKET006", "2025-11-21");

        // 3. Add all visitors to the ride's waiting queue (FIFO order guaranteed by Queue interface)
        waterRide.addVisitorToQueue(v1);
        waterRide.addVisitorToQueue(v2);
        waterRide.addVisitorToQueue(v3);
        waterRide.addVisitorToQueue(v4);
        waterRide.addVisitorToQueue(v5);

        // 4. Print the waiting queue to verify visitors are added in the original order
        waterRide.printQueue();

        // Print a separator message to clearly indicate the test step (after removing 2 visitors from the queue)
        System.out.println("\n--- After Removing 2 Visitors ---");

        // Remove the first 2 visitors from the queue (FIFO order: removes the earliest added visitors first)
        // Queue.remove() (without parameters) retrieves and removes the head of the queue (complies with FIFO principle)
        waterRide.removeVisitorFromQueue();
        waterRide.removeVisitorFromQueue();

        // Print the updated queue to verify that the first 2 visitors have been removed
        // Expected result: The queue contains the remaining visitors in their original order (excluding the first 2)
        waterRide.printQueue();
    }

    /**
     * Part 4A: Test Ride History Management Functionality
     * Verifies core ride history features as required by the assignment:
     * 1. Create an amusement ride (no operator assigned temporarily for isolated testing)
     * 2. Add 5 unique visitors to the ride's history (enforces duplicate prevention)
     * 3. Validate duplicate detection (check if a visitor exists in history via ticket ID)
     * 4. Print the complete ride history to confirm data integrity and uniqueness
     */
    public void partFourA() {
        System.out.println("\n=== Part 4A: Ride History Test ===");

        // 1. Create an amusement ride (operator set to null temporarily for this test)
        // Ride constructor params: rideName, maxCapacity (4 visitors per cycle), operator (null)
        Ride rollerCoaster = new Ride("Speed Demon", 4, null);

        // 2. Create 5 test visitors with unique ticket IDs and add to ride history
        // Visitor constructor params: name, age, contactNumber, ticketId (unique), visitDate (format: YYYY-MM-DD)
        Visitor v1 = new Visitor("Frank Green", 28, "555-6789", "TICKET007", "2025-11-22");
        Visitor v2 = new Visitor("Grace Hall", 33, "555-0123", "TICKET008", "2025-11-22");
        Visitor v3 = new Visitor("Henry Hughes", 24, "555-4567", "TICKET009", "2025-11-22");
        Visitor v4 = new Visitor("Ivy Jones", 29, "555-8901", "TICKET010", "2025-11-22");
        Visitor v5 = new Visitor("Jack King", 31, "555-2345", "TICKET011", "2025-11-22");

        // Add visitors to history (duplicate entries blocked by addVisitorToHistory() logic)
        rollerCoaster.addVisitorToHistory(v1);
        rollerCoaster.addVisitorToHistory(v2);
        rollerCoaster.addVisitorToHistory(v3);
        rollerCoaster.addVisitorToHistory(v4);
        rollerCoaster.addVisitorToHistory(v5);

        // 3. Validate duplicate prevention: Check if v1 is already recorded in history
        // Uses ticketId (unique identifier) to avoid false duplicates from same name/age
        boolean isDuplicate = rollerCoaster.checkVisitorFromHistory(v1);
        // Verify duplicate prevention: Check if the first visitor (Frank Green) is already recorded in the ride history
        // Uses ticket ID (unique identifier) for accurate duplicate detection (avoids false positives from same name/age)
        System.out.println("Is v1 (Frank Green) already in history? " + isDuplicate);
        // Print the total number of unique visitors in the ride history to confirm data integrity
        // Expected result: Equals the number of unique visitors added (no duplicates counted)
        System.out.println("Total visitors in history: " + rollerCoaster.numberOfVisitors());

        // 4. Print complete ride history to confirm all unique visitors are recorded
        rollerCoaster.printRideHistory();
    }

    /**
     * Part 4B: Test Sorted Ride History Functionality
     * Verifies the custom sorting feature for ride history as required by the assignment:
     * 1. Create an amusement ride and add 5 visitors with mixed visit dates (to test sorting logic)
     * 2. Print the ride history BEFORE sorting (shows original addition order)
     * 3. Execute sorting using the custom VisitorComparator (applies predefined rules)
     * 4. Print the ride history AFTER sorting to verify correct order is applied
     */
    public void partFourB() {
        System.out.println("\n=== Part 4B: Sorted Ride History Test ===");

        // 1. Create an amusement ride (Ferris wheel) with max capacity 6 (operator set to null for this test)
        // Ride constructor params: rideName, maxCapacity (6 visitors per cycle), operator (null)
        Ride FerrisWheel = new Ride("Giant Ferris Wheel", 6, null);

        // 2. Create 5 test visitors with mixed visit dates (includes same and different dates)
        // Purpose: Validate sorting logic (e.g., date ascending + age descending for matching dates)
        // Visitor constructor params: name, age, contactNumber, ticketId (unique), visitDate (format: YYYY-MM-DD)
        Visitor v1 = new Visitor("Luna Moore", 22, "555-1111", "TICKET012", "2025-11-23");
        Visitor v2 = new Visitor("Mason Nelson", 45, "555-2222", "TICKET013", "2025-11-22");
        Visitor v3 = new Visitor("Nora Ortiz", 30, "555-3333", "TICKET014", "2025-11-23");
        Visitor v4 = new Visitor("Oscar Perez", 35, "555-4444", "TICKET015", "2025-11-23");
        Visitor v5 = new Visitor("Penelope Quinn", 28, "555-5555", "TICKET016", "2025-11-21");

        // Add visitors in intentionally mixed order to demonstrate sorting effectiveness
        FerrisWheel.addVisitorToHistory(v1);
        FerrisWheel.addVisitorToHistory(v2);
        FerrisWheel.addVisitorToHistory(v3);
        FerrisWheel.addVisitorToHistory(v4);
        FerrisWheel.addVisitorToHistory(v5);

        // 3. Print ride history BEFORE sorting (shows original addition order)
        System.out.println("\n--- Before Sorting ---");
        FerrisWheel.printRideHistory();

        // 4. Sort ride history using custom VisitorComparator (applies assignment's sorting rules)
        FerrisWheel.sortRideHistory();

        // 5. Print ride history AFTER sorting to verify correct order is applied
        System.out.println("\n--- After Sorting ---");
        FerrisWheel.printRideHistory();
    }

    /**
     * Part 5: Test Ride Cycle Functionality
     * Verifies the complete end-to-end ride cycle logic as required by the assignment:
     * 1. Validate operator presence (ride cannot run without an operator)
     * 2. Process waiting queue according to ride capacity (max visitors per cycle)
     * 3. Transfer riders from queue to ride history after cycle completion
     * 4. Update cycle count and verify queue depletion over multiple cycles
     * Simulates two consecutive cycles to test capacity limits and queue management
     */
    public void partFive() {
        System.out.println("\n=== Part 5: Ride Cycle Test ===");

        // 1. Create a ride operator (required for the ride to run a cycle)
        // Employee constructor params: name, age, contactNumber, employeeId, rideSpecialization
        Employee coasterOperator = new Employee("Sarah Lee", 32, "555-6666", "EMP003", "Roller Coaster");

        // 2. Create an amusement ride with max capacity of 4 visitors per cycle
        // Ride constructor params: rideName, maxCapacity (4 visitors/cycle), assigned operator
        Ride speedCoaster = new Ride("Velocity X", 4, coasterOperator);

        // 3. Add 6 visitors to the waiting queue (exceeds max capacity to test partial processing)
        // Purpose: Verify one cycle only processes up to max capacity (4 visitors)
        Visitor v1 = new Visitor("Quinn Reed", 25, "555-7777", "TICKET017", "2025-11-24");
        Visitor v2 = new Visitor("Ryan Scott", 27, "555-8888", "TICKET018", "2025-11-24");
        Visitor v3 = new Visitor("Stella Taylor", 23, "555-9999", "TICKET019", "2025-11-24");
        Visitor v4 = new Visitor("Tyler Walker", 30, "555-0000", "TICKET020", "2025-11-24");
        Visitor v5 = new Visitor("Uma Young", 26, "555-1111", "TICKET021", "2025-11-24");
        Visitor v6 = new Visitor("Victor Zhang", 29, "555-2234", "TICKET022", "2025-11-24");

        speedCoaster.addVisitorToQueue(v1);
        speedCoaster.addVisitorToQueue(v2);
        speedCoaster.addVisitorToQueue(v3);
        speedCoaster.addVisitorToQueue(v4);
        speedCoaster.addVisitorToQueue(v5);
        speedCoaster.addVisitorToQueue(v6);

        // 4. Print initial queue state (before any cycles run)
        System.out.println("\n--- Initial Queue ---");
        speedCoaster.printQueue();

        // 5. Run first ride cycle (expected: processes 4 visitors, queue retains 2)
        speedCoaster.runOneCycle();

        // 6. Print state after first cycle (queue + updated ride history)
        System.out.println("\n--- After First Cycle ---");
        speedCoaster.printQueue();    // Expected: 2 remaining visitors
        speedCoaster.printRideHistory(); // Expected: 4 visitors added to history

        // 7. Run second ride cycle (expected: processes remaining 2 visitors, queue empties)
        System.out.println("\n=====================");
        speedCoaster.runOneCycle();

        // 8. Print final state after two cycles
        System.out.println("\n--- After Second Cycle ---");
        speedCoaster.printQueue();    // Expected: Empty queue
        speedCoaster.printRideHistory(); // Expected: All 6 visitors in history
        System.out.println("\nTotal cycles run: " + speedCoaster.getNumOfCycles()); // Expected: 2
    }

    /**
     * Part 6: Test Ride History Export Functionality
     * Verifies the ride history export feature (CSV-formatted text file) as required by the assignment:
     * 1. Create a ride with an operator and populate ride history with test visitors (reuses Part 5 logic)
     * 2. Sort the ride history (optional step to ensure exported data is organized)
     * 3. Execute the export method to generate a CSV file (auto-named with ride name + current date)
     * 4. Verify export success/failure and print result for validation
     * Ensures exported file is compatible with spreadsheet tools (Excel, Google Sheets)
     */
    public void partSix() {
        System.out.println("\n=== Part 6: Export Ride History Test ===");

        // 1. Create a ride with operator and populate ride history (reuse established test data pattern)
        // Employee constructor params: name, age, contactNumber, employeeId, rideSpecialization
        Employee operator = new Employee("David Clark", 38, "555-4444", "EMP004", "Ferris Wheel");
        // Ride constructor params: rideName, maxCapacity (6 visitors/cycle), assigned operator
        Ride ferrisWheel = new Ride("Giant Ferris Wheel", 6, operator);

        // 2. Add test visitors directly to ride history (skip queue for isolated export testing)
        // Visitor constructor params: name, age, contactNumber, ticketId (unique), visitDate (format: YYYY-MM-DD)
        Visitor v1 = new Visitor("Olivia Martinez", 24, "555-5555", "TICKET023", "2025-11-25");
        Visitor v2 = new Visitor("Liam Anderson", 31, "555-6666", "TICKET024", "2025-11-25");
        Visitor v3 = new Visitor("Emma Thomas", 27, "555-7777", "TICKET025", "2025-11-26");
        Visitor v4 = new Visitor("Noah Hernandez", 29, "555-8888", "TICKET026", "2025-11-26");
        Visitor v5 = new Visitor("Ava Moore", 22, "555-9999", "TICKET027", "2025-11-26");

        // Add visitors to history (duplicate prevention enforced by addVisitorToHistory())
        ferrisWheel.addVisitorToHistory(v1);
        ferrisWheel.addVisitorToHistory(v2);
        ferrisWheel.addVisitorToHistory(v3);
        ferrisWheel.addVisitorToHistory(v4);
        ferrisWheel.addVisitorToHistory(v5);

        // 3. Sort ride history (optional: improves readability of exported CSV data)
        ferrisWheel.sortRideHistory();

        // 4. Execute export to CSV file (auto-generates filename and handles I/O operations)
        boolean exportSuccess = ferrisWheel.exportRideHistory();

        // 5. Print export result to confirm success or failure
        System.out.println("\nExport Result: " + (exportSuccess ? "Success" : "Failed"));
    }

    /**
     * Part 7: Test Ride History Import Functionality
     * Verifies the ride history import feature (from CSV file exported by exportRideHistory()):
     * 1. Create an amusement ride with empty ride history (for isolated import testing)
     * 2. Specify the file path of the CSV file generated in Part 6 (critical for test success)
     * 3. Execute import method and validate the count of successfully imported visitors
     * 4. Print the sorted imported ride history to confirm data integrity and correctness
     * Dependencies: Requires successful execution of Part 6 to generate a valid CSV file
     */
    public void partSeven() {
        System.out.println("\n=== Part 7: Import Ride History Test ===");

        // 1. Create an amusement ride with empty ride history (isolates import functionality testing)
        // Employee constructor params: name, age, contactNumber, employeeId, rideSpecialization
        Employee operator = new Employee("Sophia Wilson", 33, "555-1122", "EMP005", "Carousel");
        // Ride constructor params: rideName, maxCapacity (8 visitors per cycle), assigned operator
        Ride carousel = new Ride("Merry-Go-Round", 8, operator);

        // 2. Critical step: Specify the file path of the CSV exported in Part 6
        // Note: After running Part 6, the console will print "File path: [full/path/of/file.txt]"
        // Replace the placeholder below with the actual file path from Part 6's output
        String importFilePath = "Giant_Ferris_Wheel_RideHistory_2025-12-01.txt";

        // 3. Execute import and capture the number of successfully imported visitors
        int importedCount = carousel.importRideHistory(importFilePath);

        // 4. Validate and visualize import result
        if (importedCount > 0) {
            System.out.println("Total imported visitors: " + carousel.numberOfVisitors());
            carousel.sortRideHistory();
            System.out.println("--- Imported Ride History ---");
            carousel.printRideHistory();
        }
    }

    /**
     * Main method - Program entry point
     * Initializes the test class and executes all required assignment test methods in sequential order
     * Validates end-to-end functionality of the Theme Park Management System by running tests for:
     * Queue management, ride history tracking, sorted history, ride cycles, CSV export, and CSV import
     * Execution order is critical (Part 7 depends on Part 6's exported CSV file)
     * @param args Command line arguments (not used in this test suite)
     */
    public static void main(String[] args) {
        System.out.println("=== Full Assignment Test ===");
        // Create an instance of the test class to access non-static test methods
        // (Non-static methods require an object instance to be invoked)
        AssignmentTwo assignment = new AssignmentTwo();

        assignment.partThree();   // Test Queue Management functionality (Part 3)
        assignment.partFourA();   // Test Ride History tracking (Part 4A)
        assignment.partFourB();   // Test Sorted Ride History (Part 4B)
        assignment.partFive();    // Test Ride Cycle execution (Part 5)
        assignment.partSix();     // Test Ride History export to CSV (Part 6)
        assignment.partSeven();   // Test Ride History import from CSV (Part 7)
        // Note: Execution order is critical - Part 7 depends on a valid CSV file generated by Part 6
    }
}