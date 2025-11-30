// Amusement facility interface: Define the methods that must be implemented
public interface RideInterface {
    // Part Three: Queue-related Methods
    void addVisitorToQueue(Visitor visitor);    // Add tourists to the queue
    void removeVisitorFromQueue();              // Remove tourists from the queue
    void printQueue();                          // Print queue visitors

    // Part Four: Methods Related to History
    void addVisitorToHistory(Visitor visitor);  // Add visitors to history
    boolean checkVisitorFromHistory(Visitor visitor);  // Check whether the tourists are in history
    int numberOfVisitors();                     // Return to the number of tourists in history
    void printRideHistory();                    // Print historical visitors (using an iterator)

    // Part Five: Run Cycle Method
    void runOneCycle();                         //Run one amusement cycle
}