package com.zeenko.serializablevsparcelable.models.complex;

import java.io.Serializable;

class Parent implements Serializable {
    private static final long serialVersionUID = 517281327402820056L;
    public byte parentId = 2;
}

class AssociatedClass implements Serializable {
    private static final long serialVersionUID = -6122072575870246479L;
    public byte assId = 3;
}

public class MyClass extends Parent {
    private static final long serialVersionUID = 5079085927629296388L;
    public byte myClassId = 1;
    AssociatedClass assosiatedClass = new AssociatedClass();
}
