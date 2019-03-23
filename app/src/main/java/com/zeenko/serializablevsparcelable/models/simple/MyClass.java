package com.zeenko.serializablevsparcelable.models.simple;

import java.io.Serializable;

class Parent implements Serializable {
    private static final long serialVersionUID = 517281327402820056L;
    public byte parentId = 2;
    static {
        System.out.println("[Parent] static block");
    }
    public Parent() {
        System.out.println("[Parent] constructor");
    }
}

class AssociatedClass implements Serializable {
    private static final long serialVersionUID = -6122072575870246479L;
    public byte assId = 3;

    static {
        System.out.println("[AssociatedClass] static block");
    }

    public AssociatedClass() {
        System.out.println("[AssociatedClass] constructor");
    }
}

public class MyClass extends Parent {
    private static final long serialVersionUID = 5079085927629296388L;

    static {
        System.out.println("[MyClass] static block");
    }

    public byte myClassId = 1;
    AssociatedClass assosiatedClass = new AssociatedClass();

    public MyClass() {
        System.out.println("[AssociatedClass] constructor");
    }
}
