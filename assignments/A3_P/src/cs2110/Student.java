package cs2110;

/**
 * A student tracked by the CMSμ course management system.
 */
public class Student {
    private final String firstName;
    private final String lastName;
    private int numCredits;

    // Class invariant:
    // - firstName is not null and not an empty string
    // - lastName is not null and not an empty string
    // - numCredits is a non-negative integer
    // - numCredits must be equal to the sum of the credits of all Courses this student is enrolled in

    public Student(String firstName, String lastName, int numCredits) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numCredits = numCredits;
    }

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert firstName != null : "First name cannot be null";
        assert !firstName.isEmpty() : "First name cannot be empty";
        assert lastName != null : "Last name cannot be null";
        assert !lastName.isEmpty() : "Last name cannot be empty";
        assert numCredits >= 0 : "Number of credits cannot be negative";
    }

    /**
     * Create a new Student with first name `firstName` and last name `lastName` who is not enrolled
     * for any credits.  Requires firstName and lastName are not empty.
     */
    public Student(String firstName, String lastName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        numCredits = 0;
        assertInv();
    }

    /**
     * Return the first name of this Student.  Will not be empty.
     */
    public String firstName() {
        return firstName;
    }

    /**
     * Return the last name of this Student.  Will not be empty.
     */
    public String lastName() {
        return lastName;
    }

    /**
     * Return the full name of this student, formed by joining their first and last names separated
     * by a space.
     */
    public String fullName() {
        // Observe that, by invoking methods instead of referencing this fields, this method was
        // implemented without knowing how you will name your fields.
        return firstName() + " " + lastName();
    }

    /**
     * Return the number of credits this student is currently enrolled in.  Will not be negative.
     */
    public int credits() {
        return numCredits;
    }

    /**
     * Change the number of credits this student is enrolled in by `deltaCredits`. For example, if
     * this student were enrolled in 12 credits, then `this.adjustCredits(3)` would result in their
     * credits changing to 15, whereas `this.adjustCredits(-4)` would result in their credits
     * changing to 8.  Requires that the change would not cause the student's credits to become
     * negative.
     */
    void adjustCredits(int deltaCredits) {
        int newCredits = numCredits + deltaCredits;
        if (newCredits < 0) {
            throw new IllegalArgumentException("Cannot adjust credits to a negative value");
        }
        numCredits = newCredits;
        assertInv();
        // This method has default visibility to prevent code in other packages from directly
        // adjusting a student's credits.
        // Assert that all preconditions are met.
        // Assert that the class invariant is satisfied before returning.
    }

    /**
     * Return the full name of this student as its string representation.
     */
    @Override
    public String toString() {
        return fullName();
    }
}
