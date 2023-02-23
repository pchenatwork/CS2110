package cs2110;

/**
 * A course managed by the CMSμ course management system.  Courses are assumed to meet every day of
 * the week.
 */
public class Course {

    /**
     * Set of all students enrolled in this Course.
     */
    private StudentSet students;

    /**
     * The title of this course (e.g. Object-Oriented Programming and Data Structures).  Must be
     * non-empty.
     */
    private String title;

    /**
     * Number of credit hours students will receive upon completing this course.  Must be
     * non-negative.
     */
    private int credits;

    /**
     * The last name of the professor of this course (e.g. Myers).  Must be non-empty.
     */
    private String prof;

    /**
     * The location of lectures at this course (e.g. Statler Hall room 185).  Must be non-empty.
     */
    private String location;

    /**
     * The start time of this course's daily meetings, expressed as the number of minutes after
     * midnight.  Must be between 0 and 1439, inclusive.
     */
    private int startTimeMin;

    /**
     * The duration of this course's daily meetings, in minutes.  Must be positive, and
     * `startTimeMin + durationMin` must be no greater than 1440.
     */
    private int durationMin;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        if (title.isEmpty()) {
            throw new IllegalStateException("Course title cannot be empty");
        }
        if (credits < 0) {
            throw new IllegalStateException("Course credits must be non-negative");
        }
        if (prof.isEmpty()) {
            throw new IllegalStateException("Professor name cannot be empty");
        }
        if (location.isEmpty()) {
            throw new IllegalStateException("Course location cannot be empty");
        }
        if (startTimeMin < 0 || startTimeMin > 1439) {
            throw new IllegalStateException("Start time must be between 0 and 1439 minutes after midnight");
        }
        if (durationMin <= 0 || startTimeMin + durationMin > 1440) {
            throw new IllegalStateException("Duration must be positive and start time plus duration must not exceed 1440 minutes");
        }
    }

    /**
     * Create a course with title `title`, taught by a professor with last name `profName`, where
     * lectures of duration `duration` minutes start at local time `startHr`:`startMin` and take
     * place in a location described by `location`.  The course counts for `credits` credit hours
     * and initially has no students. Requires `title`, `profName`, and `location` are non-empty,
     * `startHr` is between 0 and 23 (inclusive), `startMin` is between 0 and 59 (inclusive), and
     * `credits` is non-negative. `duration` must be positive and must imply an end time no later
     * than midnight.
     */
    public Course(String title, int credits, String profName, String location,
            int startHr, int startMin, int duration) {
        this.title = title;
        this.credits = credits;
        this.prof = profName;
        this.location = location;
        this.durationMin = duration;
        this.startTimeMin = startHr * 60 + startMin;
        assertInv();
    }

    /**
     * Return the title of this course.
     */
    public String title() {
        return title;
    }

    /**
     * Return the number of credit hours awarded for completing this course.  Will not be negative.
     */
    public int credits() {
        return credits;
    }

    /**
     * Return the location of lectures in this course.
     */
    public String location() {
        return location;
    }

    /**
     * Return the last name of the instructor teaching the course, prefixed by the title "Professor"
     * (separated by a space).
     */
    public String instructor() {
        return "Professor " + prof;
    }

    /**
     * Return the time at which lectures are held for this course in the format hour:min AM/PM using
     * 12-hour time. For example, "11:15 AM", "1:35 PM". Add leading zeros to the minutes if
     * necessary.
     */
    public String formatStartTime() {
        int hour = startTimeMin / 60;
        int minute = startTimeMin % 60;
        String ampm = hour >= 12 ? "PM" : "AM";
        if (hour == 0) {
            hour = 12;
        } else if (hour > 12) {
            hour -= 12;
        }
        return String.format("%d:%02d %s", hour, minute, ampm);
    }

    /**
     * Return whether this course's daily meetings overlap with those of `course` by at least 1
     * minute.  For example:
     * <ul>
     *   <li>A course that starts at 10:00 AM and has a duration of 60 minutes does **not** overlap
     *       with a course that starts at 11:00 AM and has a duration of 60 minutes.
     *   <li>A course that starts at 10:00 AM and has a duration of 61 minutes **does** overlap with
     *       a course that starts at 11:00 AM and has a duration of 60 minutes.
     * </ul>
     */
    public boolean overlaps(Course course) {
        int thisEndMin = this.startTimeMin + this.durationMin;
        int otherEndMin = course.startTimeMin + course.durationMin;
        if (this.startTimeMin <= course.startTimeMin && course.startTimeMin < thisEndMin) {
            return true;
        }
        if (this.startTimeMin < otherEndMin && otherEndMin <= thisEndMin) {
            return true;
        }
        if (this.startTimeMin >= course.startTimeMin && thisEndMin <= otherEndMin) {
            return true;
        }
        return false;
    }

    /**
     * Return whether `student` is enrolled in this course.
     */
    public boolean hasStudent(Student student) {
        return students.contains(student);
    }

    /**
     * Enroll `student` in this course if they were not enrolled already, adjusting their credit
     * count accordingly.  Return whether this causes a change in the enrollment of the course.
     */
    public boolean enrollStudent(Student student) {
        boolean enrolled = students.add(student);
        students.add(student);
        if (enrolled) {
            return false;
        }
        student.adjustCredits(credits);
        students.add(student);
        return true;
    }

    /**
     * Drop Student `student` from this course if they are currently enrolled, adjusting their
     * credit count accordingly.  Return whether this causes a change in the enrollment of the
     * course.
     */
    public boolean dropStudent(Student s) {
        boolean dropped = students.remove(s);
        if (dropped) {
            s.adjustCredits(-credits);
        }
        return dropped;
    }

    /**
     * Return the String representation of the list of students enrolled in this course
     */
    public String formatStudents() {
        return students.toString();
    }
}
