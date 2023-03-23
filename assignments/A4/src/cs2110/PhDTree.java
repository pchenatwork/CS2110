package cs2110;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;

/**
 * Stores the academic genealogy of a "root" professor, defined as all professors who earned a PhD
 * while primarily advised by the root professor, as well as everyone those professors similarly
 * advised, and so on.  This genealogy forms a tree, where each node represents a person who earned
 * a PhD, and the edges represent advisor-advisee relationships.  All professor names must be
 * distinct.
 */
public class PhDTree {

    /**
     * The Professor at the root of this PhDTree. i.e. the Professor at this "node" in an academic
     * genealogy tree. All nodes of a PhDTree have a different Professor in them. The professors'
     * names are all distinct - there are no duplicates.
     */
    private Professor professor;

    /**
     * The direct advisees of the professor at the root of this PhDTree. Each element of this set is
     * an advisee of the professor at this node.  The PhDTree nodes reachable via `advisees` form a
     * tree.
     */
    //$$$ reading : https://www.geeksforgeeks.org/treeset-in-java-with-examples
    //*** reading :  https://www.programiz.com/java-programming/set  */
    private SortedSet<PhDTree> advisees;

    /**
     * Assert that the class invariant is satisfied.  Specifically, asserts that all professor names
     * in the tree are distinct and that no node is reachable from more than one parent.
     */
    private void assertInv() {
        Set<String> seenProfs = new HashSet<>();
        Set<PhDTree> seenNodes = new HashSet<>();
        assertInvTraverse(seenProfs, seenNodes);
    }

    /**
     * Recursive helper method for classInv. Traverses the tree from this node, adding all
     * Professors and nodes seen to the respective "seen" sets. Things added must not already be in
     * the set, as that would imply that either the values in the tree are not distinct or that the
     * data structure is not a tree.
     */
    private void assertInvTraverse(Set<String> seenProfs, Set<PhDTree> seenNodes) {
        assert !seenNodes.contains(this) : "node " + this + " is not unique";
        assert !seenProfs.contains(professor.name()) : "prof " + professor + " is not unique";
        seenProfs.add(professor.name());
        seenNodes.add(this);
        for (PhDTree advisee : advisees) {
            advisee.assertInvTraverse(seenProfs, seenNodes);
        }
    }

    /**
     * Create a new PhDTree with `prof` as the root professor and no advisees.
     */
    public PhDTree(Professor prof) throws IllegalArgumentException {
        assert prof != null;
        this.professor = prof;
        // The elements of this set will be iterated in order according to the ordering of the
        // nodes' professors.  Since `PhDTree` is not `Comparable` itself, an anonymous function is
        // used to tell the comparator to look at its professor instead.


        // ### reading : ### https://www.geeksforgeeks.org/hashset-vs-treeset-in-java/ ##
        // ### reading : https://www.baeldung.com/java-tree-set   2.1 Constructor with a Comparator Parameter ###
        // ### reading lumbda expression : https://techndeck.com/java-pass-lambda-as-parameter-to-method-how-to-java8/ 
        // ### https://stackoverflow.com/questions/29699103/treeset-constructor-with-comparator-parameter

        advisees = new TreeSet<>(Comparator.comparing(node -> node.professor));
        assertInv();
    }

    /**
     * Return the Professor at the root of this PhDTree.
     */
    public Professor prof() {
        return professor;
    }

    /**
     * Return the number of direct advisees of the professor at the root of this PhDTree.
     */
    public int numAdvisees() {
        // TODO 1: Implement this method according to its specification.
        // The implementation can be a one-liner.
        ///throw new UnsupportedOperationException();
        return advisees.size();
    }

    /**
     * Return the number of professors in this tree with no advisees of their own.
     */
    public int numLeaves() {
        // Base case: this node is a leaf
        if (numAdvisees() == 0) {
            return 1;
        }

        // ### Recursive method ## Explain ###
        // ### this.numLeaves() = advisee_1.numLeaves() + ... + advisee_N.numLeaves()
        // This is a counting method 
        int totalLeaves = 0;
        for (PhDTree advisee : advisees) {
            totalLeaves += advisee.numLeaves();
        }
        return totalLeaves;
    }

    /**
     * Return a professor in this PhDTree who has at least `minAdvisees` advisees, if one exists.
     * Otherwise, throws `NotFound`.
     */
    public Professor findProlificMentor(int minAdvisees) throws NotFound {

        // ### Recursive method return the first found "Advisee" who has at least 'minAdvisees' ## Explain ###


        // This is a searching method

        // Base case: this node's professor qualifies
        if (numAdvisees() >= minAdvisees) {
            return professor;
        }

        // Recursive case: search each child
        for (PhDTree advisee : advisees) {
            try {
                return advisee.findProlificMentor(minAdvisees);
            } catch (NotFound exc) {
                // Continue (search the next child)
            }
        }

        // Not found under any child; throw an exception ourselves.
        throw new NotFound();
    }

    /**
     * Return the number of professors in this PhDTree.
     */
    public int size() {
        // TODO 2: Implement this method according to its specification.
        // Implementation constraint: This method must be recursive.
        // Hint: the size of a tree is just the sum of the sizes of the subtrees rooted at its
        // children, plus 1.  So call `c.size()` on each immediate child node `c`, add the results,
        // and add 1 more!
        //throw new UnsupportedOperationException();

        /* ### Formula : 
        Size = 1 + SizeOfAllChild 
             = 1 + (advisee_1.size() + ... + advisee_N.size())    
        *### */    
        int iAllChildSize = 0; // All Child Size initialize to 0
        for (PhDTree advisee : advisees) { // Each PhDTree.advisees[] is a PhDTree()
            iAllChildSize += advisee.size();
        }
        return 1 + iAllChildSize;
    }

    /**
     * Return the number of professors along the longest path from the root of this PhDTree to a
     * professor with no advisees (a leaf).  If the professor at the root of this PhDTree has no
     * advisees, its depth is 1.
     */
    public int maxDepth() {
        // TODO 3: Implement this method according to its specification.
        // Implementation constraint: This method must be recursive.
        // Hint: how can you use the result of `c.maxDepth()`, for each child `c`, to obtain the
        // maximum depth from the current node?
        // throw new UnsupportedOperationException();

        /*### * Formula *: 
            maxDepth = 1 + MAX(advisee_1.maxDepth, ..... + advisee_N.maxDepth )
         */
        int iDepth = 0; // Assume maxDepth=0
        for (PhDTree advisee : advisees) {
            if (advisee.maxDepth()>iDepth) iDepth = advisee.maxDepth();
        }
        return 1 + iDepth;
    }

    /**
     * Return the subtree with a professor named `targetName` at the root if such a professor is in
     * this PhDTree. Throws `NotFound` if `target` is not in this PhDTree.
     */
    public PhDTree findTree(String targetName) throws NotFound {
        // TODO 4: Implement this method according to its specification.
        // Implementation constraint: This method must be recursive.
        // Since the method can throw an exception, your recursive calls will need to happen inside
        // a try–catch block so that you can handle their exceptions and do the right thing.
        //throw new UnsupportedOperationException();

        /*#####
         * 
         */
        if (this.professor.name().equals(targetName)) {
            return this;
        } else {
            for (PhDTree advisee : advisees) {
                // loop through every items in PhDTree.advisees[] to find a hit
                try{
                    // using the try-catch to make sure every 'advisee' are examed.. 
                    return advisee.findTree(targetName);
                } 
                catch (NotFound exc) {
                   // do nothing , this is to make sure all sub-routine will be executed
                   // otherwise the NotFound() Exception from sub-routine will break the execution
                }
            }
        }
        // Only the TOP-MOST findTree() call will throw this exception. 
        // All NotFound() Exceptions from sub-routine findTree() call are muted by try-catch{}
        throw new NotFound();
    }

    /**
     * Returns true if this PhDTree contains Professor `targetName` (either at the root or among the
     * root's advising descendants).
     */
    public boolean contains(String targetName) {
        try {
            findTree(targetName);
            return true;
        } catch (NotFound exc) {
            return false;
        }
    }

    /**
     * Extend the subtree rooted at the professor named `advisorName` with a new advisee,
     * `newAdvisee`. Requires `newAdvisee` is not already in this PhDTree. Throws `NotFound` if
     * `advisorName` is not the name of any professor in this PhDTree.
     */
    public void insert(String advisorName, Professor newAdvisee) throws NotFound {
        // TODO 5: Implement this method according to its specification.
        // Implementation constraint: This method should NOT be recursive.
        // Use findTree(), above.  Do not use any methods that are below.
        // DO NOT traverse the tree twice looking for the same professor--don't duplicate work.
        //throw new UnsupportedOperationException();

        // Condition check. 
        // 1. make sure "newAdvisee" is not in the tree yet
        // 2. make sure "advisorName" is node in the tree
        assert !contains(newAdvisee.name()) : "newAdvisee '" + newAdvisee.name() + "' already exists!";
        
        // ** since findTree() already imply Condition 2, the following assert() is redundent **
        // assert contains(advisorName) : "Advisor '" + advisorName + "' doesn't exist!";

        var myTree = findTree(advisorName);

        if (myTree!=null){
            // when the sub PhDTree() is found, attach the newAdvisee as a new PhDTree.advisees[] member
            myTree.advisees.add(new PhDTree(newAdvisee));
            return;
        } else {                
            // advisorName Not found under in any tree nodes; throw an exception
            throw new NotFound();
        }
    }


    /**
     * Return the immediate advisor of the professor named `targetAdviseeName`, or throw `NotFound`
     * if `targetAdviseeName` is not an advising descendant of the professor at the root of this
     * PhDTree.
     */
    public Professor findAdvisor(String targetAdviseeName) throws NotFound {
        // TODO 6: Implement this method according to its specification.
        // Implementation constraint: This method must be recursive.
        //throw new UnsupportedOperationException();
        
        /* ### Formula Notes ###:
         * We have (1) PhDTree.professor
         *         (2) PhDTree.advisees[]
         * Loop through PhDTree.advisees[], 
         *   If there is a hit in PhDTree.advisees[], Advisor == "PhDTree.professor"
         *   Else do the same thing with each PhDTree.advisees[] (each "advisee" is a PhDTree)
         */
        Professor _advisor = null;  // Assume advisor not found
        for (PhDTree advisee : advisees) { // Loop through PhDTree.advisees[], 
            if (advisee.prof().name().equals(targetAdviseeName)){
                // advisee found, return 'PhDTree.professor' and break the loop
                _advisor = professor;
                break; 
            } else {
                // $$$$ recursively look for "Professor" from each 'advisee' (each advisee is a PhDTree())
                try {
                    // $$$$ Same idea as TODO 4: 
                    // $$$$ ignore "NotFound" exception from sub PhDTree search to finish ALL sub routin
                    _advisor = advisee.findAdvisor(targetAdviseeName);
                } 
                catch (NotFound exc) {
                   // $$ do nothing , just ignore the exception from sub PhDTree search
                }
            }
        }
        if (_advisor ==null) throw new NotFound();
        return _advisor;
    }

    /**
     * Return the professors on the path between the root of this PhDTree and the descendant
     * professor named `targetName`.  The path should start with the root advisor and end with
     * professor `targetName`, and each element (except the first) is preceded by their advisor.
     * Throws `NotFound` if there is no such path.
     */
    public List<Professor> findAcademicLineage(String targetName) throws NotFound {
        // TODO 7: Implement this method according to its specification.
        // Implementation constraint: This method must be recursive.
        // Do not use findAdvisor() in this method: it is too inefficient, leading to multiple #### WHY ???? ###
        // traversals of the tree. This method must return an object satisfying the interface
        // `List<Professor>`. Recall that `List<T>` is an interface, so use a class that implements
        // it, like `java.util.LinkedList<T>`.  Choose an implementation that provides your
        // most-used operations efficiently.
        // Hint: The base case is when the root of this PhDTree is `targetName`, in which case the
        // lineage is a list containing only `targetName`.
        ///throw new UnsupportedOperationException();

        // assert contains(targetName) : "TargetName '" + targetName + "' doesn't exists.";

         java.util.LinkedList<Professor> lst = new LinkedList<>();

        /*####
         * Formula: 
         * if (Target is found)
         *  List(ParentTree contains Target)) = 'Professor'  + List (ChildTree Contains Target) 
         * else
         *  EmptyList()
         */
         if (contains(targetName)){
            lst.add(professor);
            if (professor.name().equals(targetName)){
                return lst;
            }
            else {  
                for (PhDTree advisee : advisees) {
                    // Only work on the PhDTree.advisee (a PhDTree() itself) that contain 'targetName'
                    if (advisee.contains(targetName)){
                        // LinkedList.addAll() will append a 'sub-list' to 'lst'
                        lst.addAll(advisee.findAcademicLineage(targetName));
                        break;
                    }
                }
            }
         }
         return lst;
    }

    /**
     * Return the professor at the root of the smallest subtree of this PhDTree that contains
     * professors named `prof1Name` and `prof2Name`, if such a subtree exists. Otherwise, throw
     * `NotFound`.
     */
    public Professor commonAncestor(String prof1Name, String prof2Name) throws NotFound {
        // TODO 8: Implement this method according to its specification.
        // Implementation constraint: Do not use `findAdvisor()` for this: it is too inefficient.
        // Instead, use `findAcademicLineage()` to find the routes to the two advisees, then iterate
        // over the common prefix of the routes.  If iterating using indices, ensure that the data
        // structure can be iterated over efficiently.
        //throw new UnsupportedOperationException();

        /* ================== This implementation return the first hit, which is WRONG !!!! *
        eg : left = N1->N2->N3->N4
            Right = N1->N2->N5->N6
            commonAncesstor = N2 
         * ========================
        List<Professor> left = findAcademicLineage(prof1Name);
        List<Professor> right = findAcademicLineage(prof2Name);
        for(var prof1: left){
            for(var prof2: right){
                if (prof1.equals(prof2)){
                   return prof2;  // Return prof2
                }
            }
        }
        // throw NotFound if code can be executed here
        throw new NotFound();
        //* ================== This implementation return the first hit, WRONG !!!! */

        var left = findAcademicLineage(prof1Name).toArray();
        var right = findAcademicLineage(prof2Name).toArray();
        Professor _commonAncestor = null ; // Assume no common ancesstor
        for(int i = 0; i< left.length; i++){
            if(((Professor)left[i]).equals((Professor)right[i])){
                // If left and right share the same ancesstor, the heading portion should overlap, 
                // the last match is the common ancestor
                _commonAncestor = (Professor)left[i]; 
            }
            else {
                break;
            }
        }
        if (_commonAncestor==null){
            throw new NotFound();
        }
        return _commonAncestor;


    }

    /**
     * Return a (single line) String representation of this PhDTree. If this PhDTree has no advisees
     * (it is a leaf), return the root professor's name. Otherwise, return the root professor's name
     * + "[" + each advisee node's toString(), separated by ", ", followed by "]".
     * <p>
     * Thus, for the following tree:
     *
     * <pre>
     * Depth:
     *   1      Maya_Leong
     *            /     \
     *   2 Matthew_Hui  Curran_Muhlberger
     *           /          /         \
     *   3 Amy_Huang    Andrew_Myers   Tomer_Shamir
     *           \
     *   4    David_Gries
     *
     * Maya_Leong.toString() should print:
     * Maya Leong[Matthew Hui[Amy Huang[David Gries]]], Curran Muhlberger[Andrew Myers, Tomer Shamir]]
     *
     * Matthew_Hui.toString() should print:
     * Matthew Hui[Amy Huang[David Gries]]
     *
     * Andrew_Myers.toString() should print:
     * Andrew Myers
     * </pre>
     */
    @Override
    public String toString() {
        if (advisees.isEmpty()) {
            return professor.name();
        }
        StringBuilder s = new StringBuilder();
        s.append(professor.name())
                .append("[");
        boolean first = true;
        for (PhDTree advisee : advisees) {
            if (!first) {
                s.append(", ");
            }
            first = false;
            s.append(advisee);
        }
        s.append("]");
        return s.toString();
    }

    /**
     * Print each professor in this tree to `out`.  Each professor is printed on their own line in
     * the format NAME - YEAR.
     * <p>
     * For the tree in the specification for `toString()`, its output might be:
     * <pre>
     * Maya Leong - 2005
     * Matthew Hui - 2023
     * ## Amy_Huang  -- ???PCHEN NOTE ### Missing ??? ###
     * David Gries - 1966
     * Curran Muhlberger - 2014
     * Andrew Myers - 1999
     * Tomer Shamir - 2023
     * </pre>
     */
    public void printProfessors(PrintWriter out) {
        // TODO 9: Implement this method according to its specification.
        // Implementation constraint: This method must be recursive.
        // Traverse the tree in PREORDER, respecting the ordering of advisees.

        //$$$$ Reading $$$$$
        //https://www.geeksforgeeks.org/tree-traversals-inorder-preorder-and-postorder/
        //https://faculty.cs.niu.edu/~mcmahon/CS241/Notes/Data_Structures/binary_tree_traversals.html

        /** (1) Pre-order : [parent] -> [all children]  ======*
        out.println(professor.name() + " - " + professor.phdYear());
        for(var advisee : advisees){
            advisee.printProfessors(out);
        }  
        /** (1) Pre-order End ===== */
        /** (2) Post Order : [all children] -> [parent] ===== *
        for(var advisee : advisees){
            advisee.printProfessors(out);
        }
        out.println(professor.name() + " - " + professor.phdYear());        
        /** (2) Post Order End ===== */

        /** (3) In-Order : [child] -> [parent] -> [other child] 
         *  Usually only applicable to binary tree, eg [left child] -> [parent] -> [right child]
         * */
        //  (3) Not implemented here       
        /** (3) In-Order End ===== */


        //## another approach. use a helper function ## Not Tested Yet ##
        printPhDTree(this, out);
    }
    /**
     * Helper 
     * @param node
     * @param out
     */

    private void printPhDTree(PhDTree node, PrintWriter out) {
        out.println(node.professor.name() + " - " + node.professor.phdYear());
        for(var advisee : node.advisees){
            printPhDTree(advisee, out);
        }
    }
}
