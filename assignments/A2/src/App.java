public class App {
    public static void main(String[] args) throws Exception {

        var x = new Student("F1", "L1");
        var y = x.toString();
        //runCMSuTest();
        //runCourseTest();
        //runStudentSetTest();
        runEqualsDemo();
    }
    private static void runCMSuTest() {   
        var cms = new CMSu();

        var s11= new Student("F1", "L1");
        var s12= new Student("F1", "L2");
        var s21= new Student("F2", "L1");
        var s22= new Student("F2", "L2");
        cms.addStudent(s11); 
        cms.addStudent(s12);
        cms.addStudent(s21);
        cms.addStudent(s22);

        var c1 = new Course("CS2110", 3, "Paul", "14210 Barclary", 
            14, 45, 16);        
        var c2 = new Course("CS2111", 5, "Holly", "617 Liberty", 
            14, 30, 15);

        cms.addCourse(c1);
        cms.addCourse(c2);

        c1.enrollStudent(s11);
        c1.enrollStudent(s12);
        c1.dropStudent(s11);
        c2.enrollStudent(s11);
        c2.enrollStudent(s12);
        c2.enrollStudent(s21);
        c2.enrollStudent(s22);
     
        var auditCredits = cms.auditCredits(6);
        var b = cms.checkCreditConsistency();
        
        System.out.println("checkCreditConsistency = " + String.valueOf(b));

    }
    private static void runCourseTest(){
        var c1 = new Course("CS2110", 3, "Paul", "14210 Barclary", 
            14, 45, 16);        
        var c2 = new Course("CS2111", 4, "Holly", "617 Liberty", 
            14, 30, 15);
        System.out.println("Course 1 = " + c1.formatStartTime());
        System.out.println("Course 2 = " + c2.formatStartTime());
        System.out.println("Course overlap = " +  Boolean.toString(c1.overlaps(c2)));

        var s11= new Student("F1", "L1");
        var s12= new Student("F1", "L2");
        var s21= new Student("F2", "L1");
        var s22= new Student("F2", "L2");

        var b1 = c1.dropStudent(s11);
        c1.enrollStudent(s11);
        c1.enrollStudent(s12);
        c1.dropStudent(s11);
        c2.enrollStudent(s11);
        c2.enrollStudent(s12);
        c2.enrollStudent(s21);
        c2.enrollStudent(s22);
                   
        System.out.println("Course 1 = " + c1.toString() + "; students =" + c1.formatStudents() );
        System.out.println("Course 2 = " + c2.toString()  + "; students =" + c2.formatStudents());

    }
    private static void runStudentSetAddRemoveTest(){
        var x = new StudentSet();
        x.add(new Student("f1", "l1"));   
        x.add(new Student("f1", "l2")); 
        x.add(new Student("f2", "l1"));
        x.add(new Student("f2", "l2"));  
        x.remove(new Student("f2", "l2"));             
        System.out.println(x.toString() );
    }
    private static void runEqualsDemo(){
        // To show object "equals" concept
        String x1 = "abc";
        String x2 = "abc";
        boolean b1 = x1.equals(x2);
        var s1 = new Student("paul", "chen");
        var s2 = new Student("paul", "chen");
        var s3 = s1;
        var bok = s1.equals(s2);
        System.out.println(s1.toString() + "=" + s2.toString() + " ?? " + Boolean.toString(bok) );
        System.out.println("s1.hashCode() =" + s1.hashCode() );
        System.out.println("s2.hashCode() =" + s2.hashCode() );
        System.out.println("s3.hashCode() =" + s3.hashCode() );
    }
}

