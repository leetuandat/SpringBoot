/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/18/2025
 * @time: 07:49 PM
 * @package: com.xeng.Lesson01.pkg_Lambda_Expression
 */

package com.xeng.Lesson01.pkg_Lambda_Expression;

@FunctionalInterface
interface Calculator1 {
    int add(int a, int b);
}
@FunctionalInterface
interface Calculator2 {
    void add(int a, int b);
}

public class LambdaExpression3 {
    public static void main(String[] args) {

        Calculator1 calc1 = (int a, int b) -> a+b;
        System.out.println(calc1.add(11, 22));

        Calculator1 calc2 = (a, b) -> a + b;
        System.out.println(calc2.add(11, 33));

        Calculator2 calc3 = (a, b) -> System.out.println(a+b);
        calc3.add(11, 100);

        Calculator2 calc4 = (a, b) -> {
            int sum = a+b;
            System.out.println(a+"+"+b+"="+sum);
        };
        calc4.add(22, 200);
    }
}
