package com.zeenko.serializablevsparcelable.models;

import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.models.simple.MyClass;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        testSerializationOfSimple();
    }

    private static void testSerializationOfSimple() {
        String filename = "file.out";
        MyClass origin = new MyClass();

        // Serialization
        try (FileOutputStream file = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(file)
        ) {
            out.writeObject(origin); // Saving of object in a file
            show("Object has been serialized\nObject: " + origin.toString());
        } catch (IOException ex) {
            show("IOException is caught" + ex);
        }
        MyClass restored;

        // Deserialization
        try ( // Reading the object from a file
              FileInputStream file = new FileInputStream(filename);
              ObjectInputStream in = new ObjectInputStream(file)) {
            restored = (MyClass) in.readObject(); // Method for deserialization of object
            show("Object has been deserialized\nObject: " + restored.toString());
        } catch (IOException ex) {
            show("IOException is caught" + ex);
        } catch (ClassNotFoundException ex) {
            show("ClassNotFoundException is caught" + ex);
        }
    }

    private static void show(String msg) {
        System.out.println(msg != null ? msg : "Message is empty");
    }


    private static void testSerialization() {
        String filename = "file.ser";

        MySerializableClass object = new MySerializableClass();

        TimeUtility timeUtility = new TimeUtility();

        // Serialization
        try (FileOutputStream file = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(file)
        ) {
            timeUtility.start();
            //Saving of object in a file
            // Method for serialization of object
            out.writeObject(object);
            timeUtility.end();

            show("Object has been serialized\nObject: " + object.toString());
        } catch (IOException ex) {
            show("IOException is caught" + ex);
        }
        show("Time : " + timeUtility.getResultInMs() + " ms");

        MySerializableClass object1;

        // Deserialization
        try ( // Reading the object from a file
              FileInputStream file = new FileInputStream(filename);
              ObjectInputStream in = new ObjectInputStream(file)) {


            // Method for deserialization of object
            object1 = (MySerializableClass) in.readObject();

            show("Object has been deserialized\nObject: " + object1.toString());
        } catch (IOException ex) {
            show("IOException is caught" + ex);
        } catch (ClassNotFoundException ex) {
            show("ClassNotFoundException is caught");
        }
    }

    private static void testSerializationParcelable() {
        String filename = "file.ser";

        MyParcelableClass object = new MyParcelableClass();

        TimeUtility timeUtility = new TimeUtility();

        // Serialization
        try (FileOutputStream file = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(file)
        ) {
            timeUtility.start();
            //Saving of object in a file
            // Method for serialization of object
            out.writeObject(object);
            timeUtility.end();

            show("Object has been serialized\nObject: " + object.toString());
        } catch (IOException ex) {
            show("IOException is caught" + ex);
        }
        show("Time : " + timeUtility.getResultInMs() + " ms");

        MyParcelableClass object1;

        // Deserialization
        try ( // Reading the object from a file
              FileInputStream file = new FileInputStream(filename);
              ObjectInputStream in = new ObjectInputStream(file)) {


            // Method for deserialization of object
            object1 = (MyParcelableClass) in.readObject();

            show("Object has been deserialized\nObject: " + object1.toString());
        } catch (IOException ex) {
            show("IOException is caught");
        } catch (ClassNotFoundException ex) {
            show("ClassNotFoundException is caught");
        }
    }

    private static void testArraySerialization() {
        String filename = "file.ser";

        ArrayList<MySerializableClass> objects = new ArrayList<>();//MySerializableClass();
        for (int i = 0; i < 1000; i++) {
            objects.add(new MySerializableClass());
        }
        TimeUtility timeUtility = new TimeUtility();

        // Serialization
        try (FileOutputStream file = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(file)
        ) {
            timeUtility.start();
            //Saving of object in a file
            // Method for serialization of object
            for (MySerializableClass data : objects) {
                out.writeObject(data);
            }
            timeUtility.end();

            show("Object has been serialized\nObject: " + objects.toString());
        } catch (IOException ex) {
            show("IOException is caught" + ex);
        }
        show("Time : " + timeUtility.getResultInMs() + " ms");

        MySerializableClass object1;

        // Deserialization
        try ( // Reading the object from a file
              FileInputStream file = new FileInputStream(filename);
              ObjectInputStream in = new ObjectInputStream(file)) {


            // Method for deserialization of object
            object1 = (MySerializableClass) in.readObject();

            show("Object has been deserialized\nObject: " + object1.toString());
        } catch (IOException ex) {
            show("IOException is caught");
        } catch (ClassNotFoundException ex) {
            show("ClassNotFoundException is caught");
        }
    }
}
