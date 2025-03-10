/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/20/2025
 * @time: 07:02 PM
 * @package: com.xeng.Lesson01.pkg_collection_api_enhencements
 */

package com.xeng.Lesson01.pkg_collection_api_enhencements;

import java.util.HashMap;
import java.util.Map;

public class ForEachMapExample {
    public static void main(String[] args) {
        Map<Integer, String> hmap = new HashMap<>();
        hmap.put(1, "Java Spring");
        hmap.put(2, "JavaScript");
        hmap.put(3, "PHP Laravel");
        hmap.put(4, "C# NetCore");

        hmap.forEach((key, value) -> System.out.println(key + ": " + value));

        hmap.clear();
        System.out.println(hmap);
    }
}
