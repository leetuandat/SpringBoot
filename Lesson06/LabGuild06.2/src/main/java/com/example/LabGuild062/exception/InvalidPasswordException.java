/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06.2
 * @date: 3/18/2025
 * @time: 05:23 PM
 * @package: com.example.LabGuild062.exception
 */

package com.example.LabGuild062.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
