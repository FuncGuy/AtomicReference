package com.jenkov.io;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedRefTest {

    public static void main(String[] args) {

        String initialRef = "initial value referenced";
        int initialStamp = 0;

        //current reference = "initial value referenced"
        //current stamp = 0

        AtomicStampedReference<String> atomicStringReference =
                new AtomicStampedReference<>(initialRef, initialStamp);

        String newRef = "new value referenced";
        int newStamp = initialStamp + 1;

        //below statement is returning true reason

        // public boolean compareAndSet(V   expectedReference,
        //                                 V   newReference,
        //                                 int expectedStamp,
        //                                 int newStamp) {
        //        Pair<V> current = pair;
        //        return
        //            expectedReference == current.reference &&
        //            expectedStamp == current.stamp &&
        //            ((newReference == current.reference &&
        //              newStamp == current.stamp) ||
        //             casPair(current, Pair.of(newReference, newStamp)));
        //    }

        // expectedReference  = initial value referenced
        // current.reference = initial value referenced
        // so set to new value "new value reference"
        // timestamp to 1
        boolean exchanged = atomicStringReference
                .compareAndSet(initialRef, newRef, initialStamp, newStamp);
        System.out.println("exchanged: " + exchanged);  //true

        // initialRef = initial value referenced
        // current.reference = new value referenced
        // so mismatch don't set value.
        // current.stamp = 1
        // no need to check for stamp since it is a short circuit && it fail at first check.
        exchanged = atomicStringReference
                .compareAndSet(initialRef, "new string", newStamp, newStamp + 1);
        System.out.println("exchanged: " + exchanged);  //false

        // current.reference = new value referenced
        // expectedReference  = new value referenced
         // first condition passes [reference check passed ]
        // now check for stamp
        //current.stamp = 1
        //current.stamp != initialStamp
        // so fails
        exchanged = atomicStringReference
                .compareAndSet(newRef, "new string", initialStamp, newStamp + 1);
        System.out.println("exchanged: " + exchanged);  //false

        // current.reference = new value referenced
        // expectedReference  = new value referenced
        // first condition passes [reference check passed ]
        // now check for stamp
        // current.stamp = 1
        // NOW

        //new stamp = 0 + 1 = 1
        //match
        // current.stamp == newStamp
        // Both the condition passes update to new reference i.e to "new string"

        exchanged = atomicStringReference
                .compareAndSet(newRef, "new string", newStamp, newStamp + 1);
        System.out.println("exchanged: " + exchanged);  //true

        System.out.println("HAHAHAHAHA-------");

        System.out.println("HAHAHAHAHA Reference = " + atomicStringReference.getReference()); //new string

        System.out.println("HAHAHAHAHA Stamp = " + atomicStringReference.getStamp()); // 2


    }
}
