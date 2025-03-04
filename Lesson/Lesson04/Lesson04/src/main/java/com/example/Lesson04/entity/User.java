/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson04
 * @date: 2/28/2025
 * @time: 07:10 PM
 * @package: com.example.Lesson04.entity
 */

package com.example.Lesson04.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Generated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String email;
    private int age;
}
