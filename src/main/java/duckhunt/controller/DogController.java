package duckhunt.controller;

import duckhunt.GUI.GamePanel;
import duckhunt.model.Dog;
import duckhunt.utility.Resources.Resources;
import duckhunt.utility.sound.Sound;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Vittorio Polverino
 */
public class DogController {

    private static final int DELAY = 250;
    private static DogController dogControllerIstance = null;

    private DogIntroAnimation dogIntroAnimation;
    private DogAnimation dogAnimation;
    
    private Spritesheet spriteSheet;
    
    private BufferedImage currentImage;
    
    private GamePanel panel;
    
    private Dog dog;
    
    private Sound dogCall;
    private Sound capturedDuck;
    private Sound dogLaugh;
    
    private boolean isJumpFinihed;
    private boolean isAnimationFinished;
    
    private int x;
    private int y;

    private DogController() {
        currentImage = Resources.getImage("/images/dogRight0.png");
        dogCall = Resources.getSound("/sounds/dog.wav");
        capturedDuck = Resources.getSound("/sounds/capturedDuck.wav");
        dogLaugh = Resources.getSound("/sounds/dogLaugh.wav");
        dogIntroAnimation = new DogIntroAnimation();
        dogAnimation = new DogAnimation();
        spriteSheet = new Spritesheet();
        isAnimationFinished = false;
        isJumpFinihed = false;
        x = 0;
        y = 0;
    }

    public static DogController getIstance() {
        if (dogControllerIstance == null) {
            return dogControllerIstance = new DogController();
        }
        return dogControllerIstance;
    }

    public void setPanel(GamePanel pPanel) {
        this.panel = pPanel;
    }

    public DogIntroAnimation getIntroAnimation() {
        return dogIntroAnimation;
    }

    public DogAnimation getAnimation() {
        return dogAnimation;
    }

    public void setDog(Dog pDog) {
        this.dog = pDog;
    }

    public Dog getDog() {
        return dog;
    }

    private void moveRigth() {
        spriteSheet.setFrames(3, "dogRight");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
        x = dog.getX();
        x += dog.getSpeed();
        dog.setX(x);
    }

    private void sniff() {
        spriteSheet.setFrames(2, "dogSniff");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
    }

    private void moveUp() {
        currentImage = Resources.getImage("/images/dogHappy.png");
        y = dog.getY();
        y -= dog.getSpeed();
        dog.setY(y);
    }

    private void laughAndGoUp() {
        spriteSheet.setFrames(2, "dogLaugh");
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
        y = dog.getY();
        y -= dog.getSpeed();
        dog.setY(y);
    }

    private void laugh() {
        spriteSheet.setFrames(2, "dogLaugh");
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
    }

    private void laughAndGoDown() {
        spriteSheet.setFrames(2, "dogLaugh");
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
        y = dog.getY();
        y += dog.getSpeed();
        dog.setY(y);
    }

    private void moveDown() {
        currentImage = Resources.getImage("/images/dogHappy.png");
        y = dog.getY();
        y += dog.getSpeed();
        dog.setY(y);
    }

    private void dogInAlert() {
        currentImage = Resources.getImage("/images/dogAttention.png");
    }

    private void jump() {
        currentImage = Resources.getImage("/images/dogJump.png");
        x = dog.getX();
        y = dog.getY();
        x += dog.getSpeed();
        y -= dog.getSpeed();
        dog.setX(x);
        dog.setY(y);
    }

    private void land() {
        currentImage = Resources.getImage("/images/dogLanding.png");
        x = dog.getX();
        y = dog.getY();
        x += dog.getSpeed();
        y += dog.getSpeed();
        dog.setX(x);
        dog.setY(y);
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public boolean isIntroAnimationFinished() {
        return isAnimationFinished;
    }

    public class DogIntroAnimation implements Runnable {

        private Thread thread;
        private int i;

        public void start() throws InterruptedException {
            reset();
            thread = new Thread(this);
            thread.start();
            thread.join();
        }

        public void stop() {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        private void reset() {
            isAnimationFinished = false;
            i = 0;
            dog.setX(0);
            dog.setY(450);
        }

        @Override
        public void run() {
            while (!isAnimationFinished) {
                while (dog.getX() < 250) {
                    moveRigth();
                    panel.setDogCurrentImage(currentImage);
                    panel.repaint();
                }
                if (dog.getX() == 250) {
                    while (i < 180) {
                        sniff();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                        i++;
                    }
                    dogInAlert();
                    panel.setDogCurrentImage(currentImage);
                    panel.repaint();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("an error occured during dog intro animation thread");
                        stop();
                    }
                    dogCall.play();
                    while (dog.getY() > 320) {
                        jump();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                    }
                    while (dog.getY() < 390) {
                        land();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                    }
                }
                isAnimationFinished = true;
                stop();
            }
        }
    }

    public class DogAnimation implements Runnable {

        private Thread thread;
        private boolean theDuckIsDead;
        private int i;

        public void start(boolean pValue) throws InterruptedException {
            reset();
            theDuckIsDead = pValue;
            thread = new Thread(this);
            thread.start();
            thread.join();
        }

        public void stop() {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        private void reset() {
            dog.setX(350);
            dog.setY(380);
            i = 0;
            isAnimationFinished = false;
        }

        @Override
        public void run() {
            while (!isAnimationFinished) {
                if (theDuckIsDead) {
                    capturedDuck.play();
                    while (dog.getY() > 350) {
                        moveUp();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("an error occured during dog animation thread");
                        stop();
                    }
                    while (dog.getY() < 360) {
                        moveDown();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                    }
                } else {
                    dogLaugh.play();
                    while (dog.getY() > 370) {
                        laughAndGoUp();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                    }
                    while (i < 200) {
                        laugh();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                        i++;
                    }
                    while (dog.getY() < 390) {
                        laughAndGoDown();
                        panel.setDogCurrentImage(currentImage);
                        panel.repaint();
                    }
                }
                isAnimationFinished = true;
                stop();
            }
        }
    }
}
