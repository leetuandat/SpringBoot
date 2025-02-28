/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson02
 * @date: 2/21/2025
 * @time: 08:11 PM
 * @package: com.xeng.lesson02.ioc
 */

package com.xeng.lesson02.ioc;

class Service {
    public void serve() {
        System.out.println("Service is serving");
    }
}

class Client {
    private Service service;

    public Client() {
        service = new Service();
    }

    public void doSomething() {
        service.serve();
    }
}

public class NonIoC {
    public static void main(String[] args) {
        Client client = new Client();
        client.doSomething();
    }
}
