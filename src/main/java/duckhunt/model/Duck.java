package duckhunt.model;

import java.awt.Rectangle;
import java.util.Random;

/**
 * 
 * @author Vittorio Polverino
 */
public class Duck {

    private static final int START_Y_COORDINATE = 391;
    private final Rectangle rectangle;
    private final int speed;

    public Duck() {
        rectangle = new Rectangle(80, 80);
        rectangle.setLocation(randomX(), START_Y_COORDINATE);
        speed = randomSpeed();
    }

    public int getSpeed() {
        return speed;
    }

    public void setX(int pX) {
        this.rectangle.x = pX;
    }

    public int getX() {
        return rectangle.x;
    }

    public void setY(int pY) {
        this.rectangle.y = pY;
    }

    public int getY() {
        return rectangle.y;
    }

    private int randomX() {
        Random random = new Random();
        int randomValue = random.nextInt(798) + 1;
        return randomValue;
    }

    private int randomSpeed() {
        Random random = new Random();
        int randomSpeed = random.nextInt(2) + 2;
        return randomSpeed;
    }
}
