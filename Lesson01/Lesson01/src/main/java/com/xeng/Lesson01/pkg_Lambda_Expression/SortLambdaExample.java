/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/18/2025
 * @time: 07:33 PM
 * @package: com.xeng.Lesson01.pkg_Lambda_Expression
 */

package com.xeng.Lesson01.pkg_Lambda_Expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortLambdaExample {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java SpringBoot",
                "C# NetCore", "PHP", "JavaScript");
        //sắp xếp theo biểu thức lamda
        Collections.sort(list, (String list1, String list2) -> list1.compareTo(list2));
        for (String str : list) {
            System.out.println(str);
        }
    }
}
