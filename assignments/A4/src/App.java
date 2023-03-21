import cs2110.*;
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        var prof1 = new Professor("A", 1900);
        var prof2 = new Professor("B", 1900);

        if (prof1.compareTo(prof2) > 0) {
            System.out.println("Prof 1 > Prof 2");
        } else {
            System.out.println("Prof 1 < Prof 2");
        }

    }
}
