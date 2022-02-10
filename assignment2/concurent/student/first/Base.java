package concurent.student.first;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Base {

    private static final int STARTER_PEASANT_NUMBER = 5;
    private static final int PEASANT_NUMBER_GOAL = 10;

    // lock to ensure only one unit can be trained at one time
    private final ReentrantLock trainingLock = new ReentrantLock();

    private final String name;
    private final Resources resources = new Resources();
    private final List<Peasant> peasants = Collections.synchronizedList(new LinkedList<>());
    private final List<Building> buildings = Collections.synchronizedList(new LinkedList<>());
    public CountDownLatch countDownLatch = new CountDownLatch(5);
    public volatile boolean pleaseStop = false; 

    public Base(String name) {
        this.name = name;
        // TODO Create the initial 5 peasants - Use the STARTER_PEASANT_NUMBER constant
        ExecutorService peasantsExecutorService = Executors.newFixedThreadPool(STARTER_PEASANT_NUMBER);
        // TODO 3 of them should mine gold
        for (int i = 0; i < 3; i++) {
            peasantsExecutorService.submit(() -> {
                Peasant peasant = Peasant.createPeasant(this);
                peasants.add(peasant);
                peasant.startMining();
            });
        }
        // TODO 1 of them should cut tree
        peasantsExecutorService.submit(() -> {
            Peasant peasant = Peasant.createPeasant(this);
            peasants.add(peasant);
            peasant.startCuttingWood();
        });
        // TODO 1 should do nothing
        peasantsExecutorService.submit(() -> {
            Peasant peasant = Peasant.createPeasant(this);
            peasants.add(peasant);
        });
        // TODO Use the createPeasant() method
        peasantsExecutorService.shutdown();
    }

    public void startPreparation() {
        // TODO Start the building and training preparations on separate threads
        // TODO Tip: use the hasEnoughBuilding method
        // TODO Build 3 farms - use getFreePeasant() method to see if there is a peasant
        // without any work
        Thread buildFarms = new Thread(() -> {
            while (!hasEnoughBuilding(UnitType.FARM, 3) ) {
                //System.out.println(1);
                if(pleaseStop == false){
                Peasant tmpPeasant = getFreePeasant();
                if (tmpPeasant != null) {
                    pleaseStop = true;
                    //System.out.println(1);
                    while(!tmpPeasant.tryBuilding(UnitType.FARM))
                    {

                    }
                    //System.out.println(2);
                }
            }
            }
        });
        buildFarms.start();

        // TODO Create remaining 5 peasants - Use the PEASANT_NUMBER_GOAL constant
        // TODO 5 of them should mine gold
        // TODO 2 of them should cut tree
        // TODO 3 of them should do nothing
        // TODO Use the createPeasant() method
        Thread training = new Thread(() -> {
            int i = 0;
            while (i < 2) {
                Peasant peasant = createPeasant();
                if (peasant != null) {
                    peasant.startMining();
                    peasants.add(peasant);
                    i = i + 1;
                }
            }
            Peasant peasant = createPeasant();
            if (peasant != null) {
                Thread t = new Thread(() -> {
                    peasant.startCuttingWood();
                });
                t.start();
                peasants.add(peasant);
            }
            i = 0;
            while(i<2){
                Peasant p = createPeasant();
                if(p != null){
                    peasants.add(p);
                    i++;
                }
            }

        });
        training.start();

        // TODO Build a lumbermill - use getFreePeasant() method to see if there is a
        // peasant without any work
        Thread bulidLumbermill = new Thread(()->{
            Peasant pp = null;
            while(pp == null ){
                pp = getFreePeasant();
                if(pp!=null){
                  while(true){
                      if(pp.tryBuilding(UnitType.LUMBERMILL)){
                          break;
                      }
                  }
                  break;
                }
            }       
        });
        bulidLumbermill.start();
        
        // TODO Build a blacksmith - use getFreePeasant() method to see if there is a
        // peasant without any work
       
        Thread bulidBlacksmith = new Thread(()->{
            Peasant pp = null;
            while(pp == null ){
                pp = getFreePeasant();
                if(pp!=null){
                  while(true){
                      if(pp.tryBuilding(UnitType.BLACKSMITH)){
                          break;
                      }
                  }
                  break;
                }
            }
            
        });
        bulidBlacksmith.start();
        // TODO Wait for all the necessary preparations to finish
        try {
            buildFarms.join();
            training.join();
            bulidLumbermill.join();
            bulidBlacksmith.join();
            countDownLatch.await();
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        
        // TODO Stop harvesting with the peasants once everything is ready
        synchronized(peasants){
            for (Peasant p : peasants) {
                p.stopHarvesting();
            }
        }
        System.out.println(this.name + " finished creating a base");
        System.out.println(this.name + " peasants: " + this.peasants.size());
        for (Building b : buildings) {
            System.out.println(this.name + " has a  " + b.getUnitType().toString());
        }

    }

    /**
     * Returns a peasants that is currently free.
     * Being free means that the peasant currently isn't harvesting or building.
     *
     * @return Peasant object, if found one, null if there isn't one
     */
    private Peasant getFreePeasant() {
        // TODO implement - use the peasant's isFree() method
        synchronized (peasants) {
            for (Peasant peasant : peasants) {
                if (peasant.isFree()) {
                    return peasant;
                }
            }
        }
        return null;
    }

    /**
     * Creates a peasant.
     * A peasant could only be trained if there are sufficient
     * gold, wood and food for him to train.
     *
     * At one time only one Peasant can be trained.
     *
     * @return The newly created peasant if it could be trained, null otherwise
     */
    private Peasant createPeasant() {
        Peasant result;
        trainingLock.lock();
        if (resources.canTrain(UnitType.PEASANT.goldCost, UnitType.PEASANT.woodCost, UnitType.PEASANT.foodCost)) {

            // TODO 1: Sleep as long as it takes to create a peasant - use sleepForMsec()
            // method
            sleepForMsec(UnitType.PEASANT.buildTime);
            // TODO 2: Remove costs
            resources.removeCost(UnitType.PEASANT.goldCost, UnitType.PEASANT.woodCost);
            // TODO 3: Update capacity
            resources.updateCapacity(UnitType.PEASANT.foodCost);
            // TODO 4: Use the Peasant class' createPeasant method to create the new Peasant
            result = Peasant.createPeasant(this);
            // TODO Remember that at one time only one peasant can be trained
            return result;
            // return result;
        }
        trainingLock.unlock();
        return null;
    }

    public Resources getResources() {
        return this.resources;
    }

    public List<Building> getBuildings() {
        return this.buildings;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Helper method to determine if a base has the required number of a certain
     * building.
     *
     * @param unitType Type of the building
     * @param required Number of required amount
     * @return true, if required amount is reached (or surpassed), false otherwise
     */
    private boolean hasEnoughBuilding(UnitType unitType, int required) {
        // TODO check in the buildings list if the type has reached the required amount
        int res = 0;
        synchronized (buildings) {
            for (Building building : buildings) {
                if (building.getUnitType() == unitType) {
                    res++;
                    
                }
            }
            if(res >= required){
                return true;
            }
        }
        return false;
    }

    private static void sleepForMsec(int sleepTime) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }

}
