package concurrent.labs;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Soldier {

    protected static int idGenerator = 0;
    protected int id = idGenerator++;
    protected Soldier opponent;
    protected int maxAttackPoint = 20;
    protected int minAttackPoint = 15;
    protected int health = 100;
    protected boolean isDead = false;

    public void attackOpponent(){
        if(!isDead) opponent.loseHealth(getAttackPoint());
        if(opponent.isDead()) {
            setOpponent(null);
        } else {
            opponent.defend();
        }
    }

    public void defend(){
        if(!isDead) opponent.loseHealth(getAttackPoint());
        if(opponent.isDead()) {
            setOpponent(null);
        }
    }

    public void loseHealth(int amount){
        health -= amount;
        if(health <= 0) {
            isDead = true;

            synchronized (this) {
                this.notify();
            }
        }
    }

    public int getId(){
        return id;
    }

    public Soldier getOpponent() {
        return opponent;
    }

    public void setOpponent(Soldier opponent) {
        this.opponent = opponent;
    }

    public boolean isFree(){
        return Objects.isNull(opponent);
    }

    public int getAttackPoint() {
        return ThreadLocalRandom.current().nextInt(minAttackPoint, maxAttackPoint);
    }

    public boolean isDead() {
        return isDead;
    }

}
