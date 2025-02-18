/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/18/2025
 * @time: 07:58 PM
 * @package: com.xeng.Lesson01.pkg_Lambda_Expression
 */

package com.xeng.Lesson01.pkg_Lambda_Expression;

import java.util.*;

public class LambdaExpression4 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java SpringBoot",
                "C# NetCore", "PHP", "JavaScript");

        //Using lambda expression
        list.forEach(item -> System.out.println(item));

        System.out.println("=====================");
        list.forEach(System.out::println);

        System.out.println("=====================");
        list.stream().filter(x -> x.length() > 10).forEach(System.out::println);

        System.out.println("=====================");
        list.stream().filter(x->x.toUpperCase().contains("C")).forEach(System.out::println);
    }
}
