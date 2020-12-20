package com.southwind.test;

import com.southwind.entity.Student;
import com.southwind.ioc.ApplicationContext;
import com.southwind.ioc.impl.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Student student = (Student)applicationContext.getBean("student");
        System.out.println(student);
    }
}
