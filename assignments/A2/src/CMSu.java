package cs2110;

import java.io.PrintStream;

/*
 * Assignment metadata
 * Name and NetID: TODO (TODO)
 * Hours spent on assignment: TODO
 */

/**
 * An instance of a Course Management System tracking course enrollment for a single semester.
 */
public class CMSu {

    /**
     * List of all the courses managed by this Course Management System (CMS).  The index of a
     * course in the array is used as unique public identifier.  Only the first `nCourses` elements
     * are valid; remaining elements are null.
     */
    private Course[] courses;

    /**
     * Number of courses in `courses`.
     */
    private int nCourses;

    /**
     * List of all the students tracked by this CMS.  The index of a student in the array is used as
     * a unique public identifier.  Only the first `nStudents` elements are valid; remaining
     * elements are null.
     * <p>
     * For each student, the value returned by `credits()` must be equal to the sum of the credits
     * of all Courses that student is enrolled in.  All code in this package must respect this
     * invariant for students managed by a `CMSu`.
     */
    private Student[] students;

    /**
     * Number of students in `students`.
     */
    private int nStudents;

    /**
     * Create a new instance tracking no courses and no students.  Will have sufficient capacity for
     * at least 100 courses and 1000 students.
     */
    public CMSu() {
        courses = new Course[100];
        students = new Student[1000];
    }

    /**
     * Return whether this CMS has sufficient remaining capacity to manage an additional course.
     */
    public boolean canAddCourse() {
        return nCourses < courses.length;
    }

    /**
     * Add a course that this CMS should manage. Requires `course` is not already managed by this
     * CMS and this CMS has sufficient remaining capacity to manage an additional course.
     */
    public void addCourse(Course course) {
        assert canAddCourse();
        courses[nCourses] = course;
        nCourses += 1;
    }

    /**
     * Return whether `id` is a valid course identifier for use in `getCourse()`.
     */
    public boolean isValidCourseId(int id) {
        return id >= 0 && id < nCourses;
    }

    /**
     * Return the course corresponding to identifier `id`, as presented by `printCourses()`.
     * Requires `id` is a valid course ID.
     */
    public Course getCourse(int id) {
        assert isValidCourseId(id);
        return courses[id];
    }

    /**
     * Print a table of course identifiers and course titles to `out`.
     */
    public void printCourses(PrintStream out) {
        for (int i = 0; i < nCourses; ++i) {
            out.printf("%2d: %s\n", i, courses[i].title());
        }
    }

    /**
     * Return whether this CMS has sufficient remaining capacity to track an additional student.
     */
    public boolean canAddStudent() {
        return nStudents < students.length;
    }

    /**
     * Add a student that this CMS should track. Requires `student` is not already tracked by this
     * CMS and this CMS has sufficient remaining capacity to track an additional student.
     */
    public void addStudent(Student student) {
        assert canAddStudent();
        students[nStudents] = student;
        nStudents += 1;
    }

    /**
     * Return whether `id` is a valid student identifier for use in `getStudents()`.
     */
    public boolean isValidStudentId(int id) {
        return id >= 0 && id < nStudents;
    }

    /**
     * Return the student corresponding to identifier `id`, as presented by `printStudents()`.
     * Requires `id` is a valid student ID.
     */
    public Student getStudent(int id) {
        assert isValidStudentId(id);
        return students[id];
    }

    /**
     * Print a table of student identifiers and student names to `out`.
     */
    public void printStudents(PrintStream out) {
        for (int i = 0; i < nStudents; ++i) {
            out.printf("%3d: %s\n", i, students[i].fullName());
        }
    }

    /**
     * Return whether any of the courses that `student` is enrolled in have overlapping meeting
     * times.
     */
    public boolean hasConflict(Student student) {
        // TODO 24: Implement this method according to its specification
        throw new UnsupportedOperationException();
    }

    /**
     * Return the set of students tracked by this CMS who are enrolled in more than `creditLimit`
     * credits.
     */
    public StudentSet auditCredits(int creditLimit) {
        // TODO 25: Implement this method according to its specification
        throw new UnsupportedOperationException();
    }

    /**
     * Return whether the number of credits that each student tracked by this CMS thinks they are
     * enrolled in matches the total of the credits offered by each course managed by this CMS that
     * considers the student to be enrolled.  This serves as a consistency check for the CMS's
     * data.  Relaxed precondition: the invariant regarding credit consistency between courses and
     * students may be violated (in which case this returns false).
     */
    public boolean checkCreditConsistency() {
        // TODO 26: Implement this method according to its specification
        throw new UnsupportedOperationException();
    }

}
