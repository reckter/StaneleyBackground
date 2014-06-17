import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by reckter on 30.05.2014.
 *
 * @auther reckter
 */
public class Main {

    static ReentrantLock lock = new ReentrantLock(true);

    /**
     * the main method of the programm
     * @param args ignored
     */
    static public void main(String[] args){

        BufferedImage staneley = null;
        Robot robot = null;

        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            staneley = ImageIO.read(new File("staneley.jpg"));
            robot = new Robot();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }

       // new RefreshDesktop().start();
        long time, time2;
        while(true) {

            try {
                time = System.currentTimeMillis();
                BufferedImage capture = robot.createScreenCapture(screenRect);



                putImageinImage(staneley, capture, 1265, 500, 388, 258);
                capture = staneley;

                ImageIO.write(staneley, "bmp", new File("C:/out.bmp"));


                Runtime.getRuntime().exec("changeBackground.bat").waitFor();
                Runtime.getRuntime().exec("WallpaperChanger.exe C:\\background.bmp 4").waitFor();
                time2 = System.currentTimeMillis();
                //System.out.println("took: " + (time2 - time));

                Thread.sleep(1000 - (time2 - time));
            } catch (Exception e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }

    }

    /**
     * puts an Image into another image
     * @param in the image, wich will have the puther image in it
     * @param out the image to put into the immage
     * @param xStart the stating point x
     * @param yStart the starting point x
     * @param witdh the width
     * @param height the height
     */
    static public void putImageinImage(BufferedImage in, BufferedImage out, int xStart, int yStart, int witdh, int height) {

        float stepX = 1;
        float stepY = 1;
        Image img = out.getScaledInstance(witdh,height, Image.SCALE_AREA_AVERAGING);
        out = toBufferedImage(img);

        for(int x = 0; x < witdh; x++) {
            for(int y = 0; y < height; y++) {
                in.setRGB(xStart + x, yStart + y, out.getRGB(Math.round(x * stepX), Math.round(y * stepY)));
            }
        }
    }

    /**
     * converts a Image into a Buffered Image (for convienience not from me)
     * @param img the Image
     * @return the buffered Image
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    private static class RefreshDesktop extends Thread {
        @Override
        public void run() {
            System.out.println("started changing");
            long time, time2;
            while(true) {
                try {
                    lock.lock();
                    Thread.sleep(500);
                    time = System.currentTimeMillis();


                    time2 = System.currentTimeMillis();

                    System.out.println("changing took: " + (time2 - time));
                    lock.unlock();
                    Thread.yield();
                } catch (Exception e) {
                    System.out.println("While changing: " + e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
    }

}
