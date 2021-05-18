import javax.imageio.ImageIO;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Helper {

  public static String textToBinary(String text) {
    char[] chars = text.toCharArray();
    String binary = "";
    for (int i = 0; i < chars.length; i++) {
      String temp = Integer.toBinaryString((int) chars[i]);
      binary += String.format("%1$7s", temp).replace(' ', '0');
    }

    return binary;
  }

  public static void createImageFile(BufferedImage bufferedImage, String formatName, String output) {
    try {
      ImageIO.write(bufferedImage, formatName, new File(output));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static int[][] getArgbFromImage(File file) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    int[][] cover = new int[image.getHeight()][image.getWidth()];
    for (int y = 0; y < cover.length; y++) {
      for (int x = 0; x < cover.length; x++) {
        cover[y][x] = image.getRGB(x, y);
      }
    }

    return cover;
  }

  public static int[] getHistogramFromArgb(int[][] cover) {
    int[] histogram = new int[256];
    for (int i = 0; i < cover.length; i++) {
      for (int j = 0; j < cover.length; j++) {
        int temp = (cover[j][i] >> 16) & 0xff;
        histogram[temp]++;
      }
    }
    return histogram;
  }

  public static String getFileExtension(String fileName) {
    int index = fileName.lastIndexOf('.');
    if (index > 0) {
      return fileName.substring(index + 1);
    }

    return "";
  }

  public static String getFileNameWithoutExtension(String fileName) {
    return fileName.replaceFirst("[.][^.]+$", "");
  }

}