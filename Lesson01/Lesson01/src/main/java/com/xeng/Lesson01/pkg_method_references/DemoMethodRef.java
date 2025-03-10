/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/18/2025
 * @time: 08:23 PM
 * @package: com.xeng.Lesson01.pkg_method_references
 */

package com.xeng.Lesson01.pkg_method_references;

@FunctionalInterface
interface ExecuteFunction {
    public int execute(int a, int b);
}

class MathUtils {
    public MathUtils() {}
    public MathUtils(String str) {
        System.out.println("MathUtils: " + str);
    }

    public static int sum(int a, int b) {
        return a + b;
    }

    public static int minus(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }
}

public class DemoMethodRef {
    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        int sum = doAction(a, b, MathUtils::sum);//MathUtils.sum(a, b)
        System.out.printf("%d + %d = %d\n", a, b, sum);

        int minus = doAction(a, b, MathUtils::minus);
        System.out.printf("%d - %d = %d\n", a, b, minus);

//        int multiply = doAction(a, b, MathUtils::multiply);
        //dung khi non-static
        MathUtils math = new MathUtils();
        int multiply = doAction(a, b, math::multiply);
        System.out.printf("%d * %d = %d\n", a, b, multiply);
    }
    public static int doAction(int a, int b, ExecuteFunction func) {
        return func.execute(a, b);
    }
}