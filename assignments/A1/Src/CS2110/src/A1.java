//package cs2110;

/*
 * Assignment metadata
 * Name and NetID: TODO (TODO)
 * Hours spent on assignment: TODO
 */
import java.lang.Math;
/**
 * Collection of misc. static functions for showcasing the capabilities of Java in a procedural
 * context.
 */
public class A1 {

    /**
     * Return the area of a regular polygon with `nSides` sides of length `sideLength`. Units of
     * result are the square of the units of `sideLength`. Requires `nSides` is at least 3,
     * `sideLength` is non-negative.
     */
    public static double polygonArea(int nSides, double sideLength) {
		if (nSides <3 || sideLength <=0) 
			throw new UnsupportedOperationException("nSides needs to be at least 3, or sideLength needs to be greater than 0");
		/** My formula ** 
        // [<<area of polygon>>] = [<<Area of Triangle>>] * nSides
		// [<<Area of Triangle>>] = (sideLength / 2) *  [<< height of triangle>>]
		// [<<height of triangle>>] = (sideLength / 2) / Math.tangent (360/(nSides*2))
        // *** tested works too ***/
		//return (sideLength / 2.0) * ((sideLength / 2.0) / Math.tan (Math.toRadians(360.0/(nSides*2.0)))) * nSides ;
        
        /* Using formular from hand-out */
        return (1.0/4.0) * Math.pow(sideLength, 2) * (nSides / Math.tan(Math.PI/nSides)) ;
    }

    /**
     * Return the next term in the Collatz sequence after the argument.  If the argument is even,
     * the next term is it divided by 2.  If the argument is odd, the next term is 3 times it plus
     * 1.  Requires magnitude of odd `x` is less than `Integer.MAX_VALUE/3` (otherwise overflow is
     * possible).
     */

    /**
     * Return the sum of the Collatz sequence starting at `seed` and ending at 1 (inclusive).
     * Requires `seed` is positive, sum does not overflow.
     */
    public static int collatzSum(int seed) {
        // Implementation constraint: Use a while-loop.  Call `nextCollatz()` to
        // advance the sequence.

        // TODO: Implement this method according to its specifications.
        //throw new UnsupportedOperationException();

        // condition check, seed needs to be greater than 0
        if (seed < 0)
            return 0; 
            //throw new UnsupportedOperationException("Seed needs to be positive integer.");

        int sum = seed;
        int nextNum = nextCollatz(seed);
        while (nextNum > 1) {
            // Overflow check
            if (Integer.MAX_VALUE - nextNum > sum){
                sum += nextNum;
                nextNum = nextCollatz(nextNum);                
            } 
            else  
                throw new UnsupportedOperationException("Overflow happens");
        }
        sum += nextNum;
        return sum;
    }
    // Declare and implement a method named `nextCollatz()` that takes one int argument and
    // returns an int.
    private static int nextCollatz(int x){            
        if (x%2 == 0) // Even 
            return x / 2;
        else // Odd 
        {
            if (x> Integer.MAX_VALUE / 3)  // overflow check
                throw new UnsupportedOperationException("Overflow happens");
            return x*3+1 ;    
        }            
    }

    /**
     * Return the median value among `{a, b, c}`.  The median has the property that at least half of
     * the elements are less than or equal to it and at least half of the elements are greater than
     * or equal to it.
     */
    public static int med3(int a, int b, int c) {
        // if a>= min(b,c) AND a<=max(b,c) then a
        if (a >= (b<c? b: c) && a <= (b>c? b: c) )
            return a;
            
        if (b >= (a<c? a: c) && b <= (a>c? a: c) )
            return b;
        return c;

        /***** !!!! Doesn't work !!!! 
        // Step 1: finding the Min and Max of the three values
        int iMin = a;
        int iMax = a;
        if (b<= iMin) 
            iMin = b;
        else if (b>= iMax) 
            iMax = b;
        if (c<= iMin) 
            iMin = c;
        else if (c>= iMax) 
            iMax = c;
        
        // Step 2: return the value that is greater or equal to iMin AND less or equal to iMax
        if (a>=iMin && a<=iMax) return a;
        if (b>=iMin && b<=iMax) return b;
        return c;
        *****/
    }

    /**
     * Return whether the closed intervals `[lo1, hi1]` and `[lo2, hi2]` overlap.  Two intervals
     * overlap if there exists a number contained in both of them.  Notation: the interval `[lo,
     * hi]` contains all numbers greater than or equal to `lo` and less than or equal to `hi`.
     * Requires `lo1` is less than or equal to `hi1` and `lo2` is less than or equal to `hi2`.
     */
    public static boolean intervalsOverlap(int lo1, int hi1, int lo2, int hi2) {
        // Implementation constraint: Use a single return statement to return
        // the value of a Boolean expression; do not use an if-statement.		
		return  (lo2 >= lo1 && lo2 <= hi1) ||
				(hi2 >= lo1 && hi2 <= hi1) ||
				(lo2 <= lo1 && hi2 >= hi1) ;
    }

    /**
     * Return the approximation of pi computed from the sum of the first `nTerms` terms of the
     * Madhava-Leibniz series.  This formula states that pi/4 = 1 - 1/3 + 1/5 - 1/7 + 1/9 - ...
     * Requires `nTerms` is non-negative.
     */
    public static double estimatePi(int nTerms) {
        // Implementation constraint: Use a for-loop.  Do not call any other
        // methods (including `Math.pow()`).
        
        // Condition check
        if (nTerms <=0) return 0;
        
        int iFactor;  // -1 or 1 
        double sum = 0;
        for (int n = 0; n<nTerms; n++){
            if (n % 2 == 0 ) 
                iFactor = 1;
            else
                iFactor = -1;
            // implicit casting to double
            double denominator = (2 * n + 1) * iFactor;
            
            sum += 1 / denominator   ;         
        }
        return sum * 4;
    }

    /**
     * Returns whether the sequence of characters in `s` is equal (case-sensitive) to that sequence
     * in reverse order.
     */
    public static boolean isPalindrome(String s) {
        // Implementation constraint: Use the `charAt()` and `length()` methods
        // of the `String` class.		
		int lg = s.length();
        
        if (lg==0) return true; // Condition check, empty string assume a Palindrome

		for (int i = 0; i <= (lg -1 )/2; i++)
		{
			if (s.charAt(i) != s.charAt(lg-1-i)) return false;
		}
		return true;
    }

    /**
     * Return an order confirmation message in English containing the order ID and the number of
     * items it contains.  Message shall handle item plurality properly (e.g. "1 item" vs. "3
     * items") and shall surround the order ID in single quotes. Examples:
     * <pre>
     * formatConfirmation("123ABC", 1) should return
     *   "Order '123ABC' contains 1 item."
     * formatConfirmation("XYZ-999", 3)" should return
     *   "Order 'XYZ-999' contains 3 items."
     * </pre>
     * Requires `orderId` only contains digits, hyphens, or letters 'A' - 'Z'; `itemCount` is
     * non-negative.
     */
    public static String formatConfirmation(String orderId, int itemCount) {
        // Implementation constraint: Use Java's ternary operator (`?:`) to give "item" the
        // appropriate plurality.        
        return "Order '" + orderId + "' contains " + Integer.toString(itemCount) + " item" + ((itemCount > 1) ? "s" : "");
    }

    // Declare, document, and implement a `main()` method calling the above methods and
    // printing a result.
    public static void main(String[] args)
    {
        int age = 20;
        assert age <= 18; //  : "Cannot Vote";
        System.out.println("The voter's age is " + age);

        // double polygonArea(int nSides, double sideLength) 
        int nSides = 6;
        double sideLength = 10;
        System.out.println("polygonArea(" + Integer.toString(nSides) + ", " + String.valueOf(sideLength) +  ") => " 
                                                + String.valueOf(polygonArea( nSides,  sideLength)));

        // int collatzSum(int seed)
        int seed = 3;
        System.out.println("collatzSum(" + Integer.toString(seed) + ") => " 
                                                + String.valueOf(collatzSum(seed)));

        // testing int med3(int a, int b, int c) 
        int a=1;
        int b=1;
        int c=1;
        System.out.println("med3(" + Integer.toString(a) + "," + Integer.toString(b) + "," + Integer.toString(c) + ") => " 
                                                + String.valueOf(med3( a,  b,  c)));

        // testing boolean intervalsOverlap(int lo1, int hi1, int lo2, int hi2) 
        int lo1 =-1;
        int hi1 =-2;
        int lo2 =-1;
        int hi2 = 4;
        System.out.println("intervalsOverlap(" + Integer.toString(lo1) + "," + Integer.toString(hi1) + ","
                                               + Integer.toString(lo2) + "," + Integer.toString(hi2) + ") => " 
                                               + String.valueOf(intervalsOverlap( lo1,  hi1,  lo2,  hi2)));

        // testing  estimatePi(int nTerms) 
        int nTerms = 222222222;
        System.out.println("estimatePi(" + Integer.toString(nTerms) + ") => " + Double.toString(estimatePi(nTerms)));

        // testing isPalindrome()
        var sPalindrome = "aAa";
        System.out.println("isPalindrome(\"" + sPalindrome + "\") => " + String.valueOf(isPalindrome(sPalindrome)));

        // testing formatConfirmation()
        var orderId = "blar blar ";
        int itemCount = 2;
        System.out.println("formatConfirmation(\"" + orderId + "\", " + Integer.toString(itemCount) + ") => " + 
            formatConfirmation(orderId, itemCount));        
    }
}
