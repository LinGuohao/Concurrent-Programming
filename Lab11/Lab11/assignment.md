## The Sleeping Barber Problem

A barber has one barber's chair in a cutting room and a waiting room containing a number of chairs in it. When the barber finishes cutting a customer's hair, he dismisses the customer and goes to the waiting room to see if there are others waiting. If there are, he brings one of them back to the chair and cuts their hair. If there are none, he returns to the chair and sleeps in it. Each customer, when they arrive, looks to see what the barber is doing. If the barber is sleeping, the customer wakes him up and sits in the cutting room chair. If the barber is cutting hair, the customer stays in the waiting room. If there is a free chair in the waiting room, the customer sits in it and waits their turn. If there is no free chair, the customer leaves.

The simulation of the barber shop is partially implemented in `BarberShop.java`. Your task is to fill in the missing parts which are the barbers sleep and the customers wake up tasks. Implement them using the methods `[wait()](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#wait())` and `[notify()](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#notify())`.

## Enhancement

Interrupting the thread `Barber` is not elegant. Instead, use method `[wait(long timeoutMillis)](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#wait(long))` to limit the length of the barber's sleep.