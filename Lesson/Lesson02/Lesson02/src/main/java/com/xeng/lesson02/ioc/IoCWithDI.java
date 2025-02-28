/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: SpringBoot
 * @date: 2/25/2025
 * @time: 05:14 PM
 * @package: com.xeng.lesson02.ioc
 */

package com.xeng.lesson02.ioc;

class IoCService {
    public void serve() {
        System.out.println("Serving IoC Service");
    }
}

class IoCClient {
    private IoCService iocService;

    //Dùng DI để truyền vào thay vì tự tạo nó
    public IoCClient(IoCService service) {
        this.iocService = service;
    }

    public void doSomething() {
        iocService.serve();
    }
}

public class IoCWithDI {
    public static void main(String[] args) {
        //Tạo đối tượng Service và truyền nó vào Client
        IoCService service = new IoCService();
        IoCClient client = new IoCClient(service);
        client.doSomething();
    }
}
