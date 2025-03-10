/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/20/2025
 * @time: 06:37 PM
 * @package: com.xeng.Lesson01.pkg_stream_api
 */

package com.xeng.Lesson01.pkg_stream_api;

import java.util.Arrays;
import java.util.List;

public class StreamExample {
    List<Integer> integerList = Arrays.asList(11, 22, 55, 33, 66);
    //Đếm các số chẵn
    //Không dùng stream
    public void withoutStream() {
        int count = 0;
        for (Integer integer : integerList) {
            if (integer % 2 == 0) {
                count++;
            }
        }
        System.out.println("WithoutStream -> Số phần tử chẵn: " + count);
    }

    //Dùng Stream
    public void withStream() {
        long count = integerList.stream().filter(num -> num % 2 == 0).count();
        System.out.println("WithStream -> Số phần tử chẵn: " + count);
    }

    public static void main(String[] args) {
        StreamExample streamExample = new StreamExample();
        streamExample.withoutStream();
        streamExample.withStream();
    }
}
