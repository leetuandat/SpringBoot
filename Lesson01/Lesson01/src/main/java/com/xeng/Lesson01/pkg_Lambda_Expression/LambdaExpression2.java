/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/18/2025
 * @time: 07:42 PM
 * @package: com.xeng.Lesson01.pkg_Lambda_Expression
 */

package com.xeng.Lesson01.pkg_Lambda_Expression;

@FunctionalInterface
interface SayHello2 {
    public void sayHello(String name);
}
public class LambdaExpression2 {
    public static void main(String[] args) {
        //Lambda sử dụng 1 tham số
        SayHello2 say1 = (name) -> {
            System.out.println("Hello " + name);
        };
        say1.sayHello("dat");

        //ngắn gọn
        SayHello2 say2 = name -> {
            System.out.println("Hello " + name);
        };
        say2.sayHello("le");

        //ngắn gọn hơn
        SayHello2 say3 = name -> System.out.println("Hello " + name);
        say3.sayHello("tuan");
    }
}
