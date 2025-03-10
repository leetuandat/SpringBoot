/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/18/2025
 * @time: 08:03 PM
 * @package: com.xeng.Lesson01.pkg_Lambda_Expression
 */

package com.xeng.Lesson01.pkg_Lambda_Expression;

import java.util.*;
import java.util.stream.Stream;

class Book {
    int id;
    String name;
    float price;

    public Book() {}

    public Book(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

public class LambdaExpression5 {
    public static void main(String[] args) {
        List<Book> books = new ArrayList<Book>();
        books.add(new Book(1, "Lập trình Java", 9.95f));
        books.add(new Book(2, "Java SpringBoot", 19.95f));
        books.add(new Book(3, "PHP Lavarel", 12.95f));
        books.add(new Book(4, "NetCore API", 29.95f));
        books.add(new Book(5, "JavaScript", 19.95f));

        Stream<Book> filter = books.stream().filter(x->x.price > 15);
        filter.forEach(System.out::println);

        System.out.println("======================");
        books.stream().filter(x -> x.name.toUpperCase().contains("C")).forEach(System.out::println);

        System.out.println("======================");
        books.stream().filter(x -> x.name.length() < 15).forEach(System.out::println);
    }
}
