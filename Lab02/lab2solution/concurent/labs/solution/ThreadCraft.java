package concurent.labs.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Cheap version of Warcraft 3
 *
 * The simulation runs until the player builds the number of houses
 * specified in {@link concurent.labs.solution.Configuration}
 * Responsible for starting and ending threads, implementing
 * the logic of each kinds of thread
 *
 * The threads to be implemented involves
 * 1, Builder - responsible for building the houses
 * 2, Miner - responsible for mining gold
 * 3, Logging - responsible for logging the state of the world
 *
 */
public class ThreadCraft {

    private static Resources resources = new Resources();
    // list for threads so we can access them when joining
    private static List<Thread> peons = new ArrayList<>();

    public static void main(String[] args) {

        // starting miner threads
        for (int i = 0; i < Configuration.NUMBER_OF_MINERS; ++i){
            Thread miner = new Thread(() -> mineAction());
            peons.add(miner);
            miner.start();
        }

        // starting builder threads
        for (int i = 0; i < Configuration.NUMBER_OF_BUILDERS; ++i){
            Thread builder = new Thread(() -> buildAction());
            peons.add(builder);
            builder.start();
        }

        // starting logging thread
        new Thread(() -> loggingAction()).start();

        // waiting for builders and miners to finish their tasks
        for(Thread peon: peons){
            try {
                peon.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Simulation over");
        logStatus();
    }

    /**
     * Should be used by miner threads
     *
     * Keeps mining until the goldmine runs out
     */
    private static void mineAction(){
        while(resources.getGoldmineCapacity() > 0){
            resources.tryToMineGold();
            sleepForMsec(Configuration.MINING_FREQUENCY);
        }
        System.out.println("Miner finished mining");
    }

    /**
     * Should be used by builder threads
     *
     * Keeps building houses when it has enough gold to do so
     * until reaches the house limit
     */
    private static void buildAction(){
        while(!isOver()){
            if(resources.tryToBuildHouse()){
                sleepForMsec(Configuration.BUILD_TIME);
            } else {
                sleepForMsec(Configuration.SLEEP_TIME);
            }
        }
        System.out.println("Builder finished building");
    }

    /**
     * Periodically logs the state of the world
     */
    private static void loggingAction(){
        while(!isOver()){
            logStatus();
            sleepForMsec(Configuration.LOGGING_FREQUENCY);
        }
    }

    /**
     * Logs current amount of gold, houses and state of goldmine
     */
    private static void logStatus(){
        System.out.println(
                "Gold: " + resources.getGold() +
                "\nHouses: " + resources.getHouses() +
                "\nGoldmine: " + resources.getGoldmineCapacity() +
                "\n**************");
    }

    /**
     * Determines if the simulation has reached its end
     * @return
     */
    private static boolean isOver(){
        return resources.getHouses() == Configuration.HOUSE_LIMIT;
    }

    /**
     * Sleeping idle on the given thread for a couple of milliseconds.
     * If something interrupts this sleep, log the exception
     * @param msec
     */
    public static void sleepForMsec(int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " got interrupted");
            e.printStackTrace();
        }
    }
}
