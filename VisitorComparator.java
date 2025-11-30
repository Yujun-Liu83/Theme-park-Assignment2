import java.util.Comparator;

/**
 * Custom Comparator class for sorting Visitor objects
 * Implements Comparator<Visitor> to ensure type safety and avoid generic mismatch issues
 * Sorting Rule: Sorts Visitor objects in ascending alphabetical order by their full name
 * (A→Z, case-insensitive for standard String comparison in Java)
 */
public class VisitorComparator implements Comparator<Visitor> {

    /**
     * Compares two Visitor objects based on the predefined sorting rule (name ascending)
     * Includes null safety handling to prevent NullPointerException during comparison
     *
     * @param v1 The first Visitor to compare
     * @param v2 The second Visitor to compare
     * @return int: Negative integer if v1's name comes before v2's; Positive integer if v1's name comes after v2's;
     * 0 if names are equal OR either/both Visitors are null (null-safe fallback)
     */
    @Override
    public int compare(Visitor v1, Visitor v2) {
        // Handle null cases: Return 0 (considered equal) if either Visitor is null to avoid NullPointerException
        if (v1 == null || v2 == null) return 0;

        // 1. Primary sorting rule: Sort by visit date in ascending order (earlier dates first)
        // Use String.compareTo() since visit dates follow ISO format (YYYY-MM-DD), ensuring correct lexicographical order matches chronological order
        int dateCompare = v1.getVisitDate().compareTo(v2.getVisitDate());
        // If dates are different, return the date comparison result to determine order
        if (dateCompare != 0) return dateCompare;

        // 2. Secondary sorting rule: If visit dates are the same, sort by age in descending order (older visitors first)
        // Integer.compare(a, b) returns positive if a > b; reverse v2.getAge() and v1.getAge() to achieve descending order
        return Integer.compare(v2.getAge(), v1.getAge());
    }
}