package cs2110;

/**
 * A student tracked by the CMSμ course management system.
 */
public class Student {
    // TODO 1: Add fields and write the class invariant.
    // Declare fields to represent the following pieces of state.  Your fields will hold information
    // describing each Student.  Make the fields private and include comments describing how each
    // one relates to the class's state and how their values are constrained (the class invariant).
    // If fields are never reassigned by the class's methods, they should be declared `final`.
    // * First name of this Student (may not be empty or null).
    // * Last name of this Student (may not be empty or null).
    // * Number of credits student is currently enrolled in (integer; may not be negative).
    // The number of credits is also involved in an invariant of the `CMSu` class:
    // it must be equal to the sum of the credits of all Courses this student is enrolled in.  This
    // invariant cannot be checked or enforced within `Student` itself, but other code in the
    // package must be aware of it and respect it.
    // Reminder: You should delete this comment block after completing the TODO.  You are welcome to
    // borrow text from it when writing your field specifications, however.

    private final string _firstName;  /* final to make this field immutable */
    private final string _lastName;  /* final */
    private int _credits;

    
    /**
     * Return the First Name of the Student, May not be empty or null
     */
    public String FirstName() {
        return _firstName;
    }
    /**
     * Return the Last Name of the Student,  May not be empty or null
     */
    public String LastName() {
        return _lastName;
    }
    /**
     * Number of credits student is currently enrolled in (integer; may not be negative).
     */
    public int Credits() {
        return _credits;
    }


    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        // TODO 2: Implement this method by asserting the invariants you identified above.
        // In CS 2110, we assume an implicit invariant that fields are not null unless otherwise
        // specified (as with method preconditions).  It is a good habit to check for this
        // explicitly.
        assert (_firstName !=null && _firstName.trim().length() > 0 ) : "First name of the student is empty.";
        assert (_lastName  !=null && _lastName.trim().length()>0 ) : "Last name of the student is empty.";
        //throw new UnsupportedOperationException();
    }

    /**
     * Create a new Student with first name `firstName` and last name `lastName` who is not enrolled
     * for any credits.  Requires firstName and lastName are not empty.
     */
    public Student(String firstName, String lastName) {
        // TODO 3: Implement this method according to its specification
        // Assert that all preconditions are met.
        // Assert that the class invariant is satisfied before returning.
        // (These two assertions are not, in general, redundant - one pertains to arguments provided
        // by the client, while the other pertains to the work done by the constructor.)
        // Note: If a field's initial value is independent of constructor arguments, it is legal to
        // initialize it when it is declared (or even to rely on a default value), but prefer to
        // assign it in the constructor so that the whole state is initialized in one place.
        //throw new UnsupportedOperationException();

        _firstName = firstName; _lastName = lastName;
        assertInv();
    }

    /**
     * Return the first name of this Student.  Will not be empty.
     */
    public String firstName() {
        // TODO 4A: Implement this method according to its specification
        //throw new UnsupportedOperationException();
        return _firstName;
    }

    /**
     * Return the last name of this Student.  Will not be empty.
     */
    public String lastName() {
        // TODO 4B: Implement this method according to its specification
        //throw new UnsupportedOperationException();
        return _lastName;
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
        // TODO 5: Implement this method according to its specification
        //throw new UnsupportedOperationException();
        return _credits; 
    }

    /**
     * Change the number of credits this student is enrolled in by `deltaCredits`. For example, if
     * this student were enrolled in 12 credits, then `this.adjustCredits(3)` would result in their
     * credits changing to 15, whereas `this.adjustCredits(-4)` would result in their credits
     * changing to 8.  Requires that the change would not cause the student's credits to become
     * negative.
     */
    void adjustCredits(int deltaCredits) {
        // This method has default visibility to prevent code in other packages from directly
        // adjusting a student's credits.
        // TODO 7: Implement this method according to its specification
        // Assert that all preconditions are met.
        // Assert that the class invariant is satisfied before returning.
        // throw new UnsupportedOperationException();
        if ((_credits + deltaCredits) < 0)
            throw new Exception("The change would cause the student's credits to become negative.");
        //?? assert (_credits + deltaCredits) >=0 : "The change would cause the student's credits to become negative."; 
        _credits = credits() + deltaCredits;
    }

    /**
     * Return the full name of this student as its string representation.
     */
    @Override
    public String toString() {
        return fullName();
    }
}
