import java.util.ArrayList;

import jminusminus.CLEmitter;

import static jminusminus.CLConstants.*;

/**
 * This class programmatically generates the class file for the following Java application:
 * 
 * <pre>
 * public class IsPrime {
 *     // Entry point.
 *     public static void main(String[] args) {
 *         int n = Integer.parseInt(args[0]);
 *         boolean result = isPrime(n);
 *         if (result) {
 *             System.out.println(n + " is a prime number");
 *         } else {
 *             System.out.println(n + " is not a prime number");
 *         }
 *     }
 *
 *     // Returns true if n is prime, and false otherwise.
 *     private static boolean isPrime(int n) {
 *         if (n < 2) {
 *             return false;
 *         }
 *         for (int i = 2; i <= n / i; i++) {
 *             if (n % i == 0) {
 *                 return false;
 *             }
 *         }
 *         return true;
 *     }
 * }
 * </pre>
 */
public class GenIsPrime {
    public static void main(String[] args) {
        CLEmitter e = new CLEmitter(true);

        // Create an ArrayList instance to store modifiers
        ArrayList<String> modifiers = new ArrayList<String>();

        // public class IsPrime {
        modifiers.add("public");
        e.addClass(modifiers, "IsPrime", "java/lang/Object", null, true);

        // public static void main(String[] args) {
        modifiers.clear();
        modifiers.add("public");
        modifiers.add("static");
        e.addMethod(modifiers, "main", "([Ljava/lang/String;)V", null, true);

        // int n = Integer.parseInt(args[0]);
        e.addNoArgInstruction(ALOAD_0);
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(AALOAD);
        e.addMemberAccessInstruction(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I");
        e.addNoArgInstruction(ISTORE_1);

        // int result (0 or 1) = isPrime (n );
        e.addNoArgInstruction(ILOAD_1);
        e.addMemberAccessInstruction(INVOKESTATIC, "IsPrime", "isPrime", "(I)I");
        e.addNoArgInstruction(ISTORE_2);

        // if (result == 0) branch to "NOTprime"
        e.addNoArgInstruction(ILOAD_2);
        e.addBranchInstruction(IFEQ, "NOTprime");

        // if (result == 1)
        // System.out.println (n + " is a prime number ")
        // Get System.out on stack
        e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //    sb = new StringBuffer();
        e.addReferenceInstruction(NEW, "java/lang/StringBuffer");
        e.addNoArgInstruction(DUP);
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/StringBuffer", "<init>", "()V");
        // sb.append(n);
        e.addNoArgInstruction(ILOAD_1);
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(I)Ljava/lang/StringBuffer;");
        e.addLDCInstruction(" is a prime number");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
        // System.out.println(sb.toString());
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "toString", "()Ljava/lang/String;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        // return;
        e.addNoArgInstruction(RETURN);

        // NOTprime
        e.addLabel("NOTprime");
        // System.out.println (n + " is not a prime number ")
        // Get System.out on stack
        e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //    sb = new StringBuffer();
        e.addReferenceInstruction(NEW, "java/lang/StringBuffer");
        e.addNoArgInstruction(DUP);
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/StringBuffer", "<init>", "()V");
        // sb.append(n);
        e.addNoArgInstruction(ILOAD_1);
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(I)Ljava/lang/StringBuffer;");
        e.addLDCInstruction(" is not a prime number");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
        // System.out.println(sb.toString());
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "toString", "()Ljava/lang/String;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        // return;
        e.addNoArgInstruction(RETURN);

        // private static int isPrime(int n) {
        modifiers.clear();
        modifiers.add("private");
        modifiers.add("static");
        e.addMethod(modifiers, "isPrime", "(I)I", null, true);

        // if (n >= 2) branch to A
        e.addNoArgInstruction(ILOAD_0);
        e.addNoArgInstruction(ICONST_2);
        e.addBranchInstruction(IF_ICMPGE, "A");
        // return False
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(IRETURN);
        // A
        e.addLabel("A");
        // i = 2
        e.addNoArgInstruction(ICONST_2);
        e.addNoArgInstruction(ISTORE_1);
        // D
        e.addLabel("D");
        // if i > (n / i) branch to B
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(ILOAD_0);
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(IDIV);
        e.addBranchInstruction(IF_ICMPGT, "B");
        // if n % i != 0 branch to C
        e.addNoArgInstruction(ILOAD_0);
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(IREM);
        e.addBranchInstruction(IFNE, "C");
        // return False
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(IRETURN);
        // C
        e.addLabel("C");
        // i++
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(ICONST_1);
        e.addNoArgInstruction(IADD);
        e.addNoArgInstruction(ISTORE_1);
        // branch to D
        e.addBranchInstruction(GOTO, "D");
        // B
        e.addLabel("B");
        e.addNoArgInstruction(ICONST_1);
        e.addNoArgInstruction(IRETURN);


        e.write();
    }
}
