package concurrent.labs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleSimulator {

    private static final int MAX_NUMBER_OF_THREADS = 100; // per executor
    private static final int MAX_NUMBER_OF_ATTACKERS = 100;
    private static final int MAX_NUMBER_OF_DEFENDERS = 92;
    private static final AtomicInteger CASTLE_HEALTH = new AtomicInteger(1000);
    private static final AtomicInteger defenderCounter = new AtomicInteger(0);
    private static final AtomicInteger attackerCounter = new AtomicInteger(0);
    private static final List<Defender> defenders = Collections.synchronizedList(new ArrayList<>());
    private static final List<Attacker> attackers = Collections.synchronizedList(new ArrayList<>());

    private static AtomicBoolean hasStarted = new AtomicBoolean(false);

    public static void main(String[] args) {
        // Create a new attacker and a new defender every 1 sec
        // Use the createSoldier(boolean isAttacker) method to create the appropriate type of soldiers
        // Advice: use scheduled executor

        // Waiting until we have a couple of soldiers on both sides
        sleepForMsec(5000);
        hasStarted.set(true);

        // Log the game status every 5 seconds using the logStatus() method

        // Invoke endGameIfNeeded() periodically to see if the game is over
        // Alternative solution: Use wait-notify to invoke endGameIfNeeded()
    }

    public static void createSoldier(final boolean isAttacker) {
        if (isAttacker) {
            if (attackerCounter.get() < MAX_NUMBER_OF_ATTACKERS) {
                Attacker attacker = new Attacker();
                attackers.add(attacker);
                attackerCounter.incrementAndGet();
                // Create a clean up mechanism - Whenever an attacker dies, it sends a notify() on itself
                // When an attacker is dead, remove it from the attackers collection
                // Use cleanupAction(Attacker attacker)

                // Invoke processAttack(Attacker attacker) on a separate thread for the newly created attacker
            }
        } else {
            if (defenderCounter.get() < MAX_NUMBER_OF_DEFENDERS) {
                Defender defender = new Defender();
                defenders.add(defender);
                defenderCounter.incrementAndGet();
                // Create a clean up mechanism - Whenever a defender dies, it sends a notify() on itself
                // When a defender is dead, remove it from the defenders collection
                // Use cleanupAction(Defender defender)
            }
        }

        if (attackerCounter.get() == MAX_NUMBER_OF_ATTACKERS &&
            defenderCounter.get() == MAX_NUMBER_OF_DEFENDERS) {
            // Make sure no threads will be stuck trying to create any new soldiers
            System.out.println("Finished creating soldiers");
        }
    }

    public static void processAttack(Attacker attacker) {
        // No need to change anything in this method
        // While an attacker is alive and there is a castle to attack, attack
        while (!attacker.isDead() && CASTLE_HEALTH.get() > 0) {
            if (hasStarted.get()) {
                synchronized (defenders) {
                    // If there is any defender who currently doesnt have an opponent, it will prevent
                    // the attacker from attacking the castle
                    if (!defenders.isEmpty() && attacker.isFree()) {
                        Optional<Defender> def = defenders.stream().filter(Defender::isFree).findFirst();
                        def.ifPresent(d -> {
                            d.setOpponent(attacker);
                            attacker.setOpponent(d);
                        });
                    }
                }
                if (attacker.isFree()) {
                    // Attack castle if doesnt have an opponent
                    CASTLE_HEALTH.set(CASTLE_HEALTH.get() - attacker.getAttackPoint());
                } else {
                    attacker.attackOpponent();
                }
            }
            sleepForMsec(100);
        }
    }

    private static void cleanupAction(Attacker attacker) {
        try {
            synchronized (attacker) {
                attacker.wait();
                attackers.remove(attacker);
                //System.out.println("Attacker " + attacker.getId() + " has died");
            }
        } catch (InterruptedException e) {
        }
    }

    private static void cleanupAction(Defender defender) {
        try {
            synchronized (defender) {
                defender.wait();
                defenders.remove(defender);
                //System.out.println("Defender " + defender.getId() + " has died");
            }
        } catch (InterruptedException e) {
        }
    }

    private static void endGameIfNeeded() {
        boolean finished = false;
        if (CASTLE_HEALTH.get() <= 0) {
            System.out.println("Attackers won!");
            finished = true;
        }
        if (attackers.isEmpty() && attackerCounter.get() == MAX_NUMBER_OF_ATTACKERS) {
            System.out.println("Defenders won!");
            finished = true;
        }
        if (finished) {
            logStatus();
            // Make sure the application can exit via terminating and/or shutting down everything
        }
    }

    private static void logStatus() {
        System.out.println("Castle health is at " + CASTLE_HEALTH.get());
        System.out.println("Number of attackers " + attackers.size());
        System.out.println("Number of defenders " + defenders.size());
        System.out.println("Created " + defenderCounter.get() + " defenders and " + attackerCounter.get() + " attackers");
        System.out.println("*********************************************");
    }

    private static void sleepForMsec(int msec) {
        try {
            TimeUnit.MILLISECONDS.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
