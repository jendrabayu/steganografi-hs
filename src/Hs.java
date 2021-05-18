import java.awt.image.*;
import java.io.File;
import java.awt.*;

public class Hs {
  public void embbeded(String imagePath, String message) {

    File imageFile = new File(imagePath);

    int[][] cover = Helper.getArgbFromImage(imageFile);
    int[] histogram = Helper.getHistogramFromArgb(cover);
    String secret = Helper.textToBinary(message);

    System.out.print("\nHistogram : ");
    for (int i = 0; i < histogram.length; i++) {
      System.out.print(histogram[i] + " ");
    }
    System.out.println();

    // Mendapatkan peak dan zero point
    int peak = 0;
    int zero = 0;
    int peakValue = histogram[0];
    int zeroValue = histogram[0];

    for (int i = 0; i < histogram.length; i++) {
      if (histogram[i] > peakValue) {
        peak = i;
        peakValue = histogram[i];
      }
      if (histogram[i] < zeroValue) {
        zero = i;
        zeroValue = histogram[i];
      }
    }

    System.out.println("Peak : " + peak);
    System.out.println("Zero : " + zero);

    // Jika posisi peak disebalah kiri zero
    if (peak < zero) {
      for (int y = 0; y < cover.length; y++) {
        for (int x = 0; x < cover[y].length; x++) {
          int temp = (cover[y][x] >> 16) & 0xff;

          if (temp == zero) {
            secret += "0";
          } else if (temp == (zero - 1)) {
            secret += "1";
          }
          // Pergeseran histogram
          if (temp > peak && temp < zero) {
            Color pixel = new Color(temp + 1, (cover[y][x] >> 8) & 0xff, cover[y][x] & 0xff,
                (cover[y][x] >> 24) & 0xff);
            cover[y][x] = pixel.getRGB();
          }
        }
      }

      // Sisipkan pesan
      int index = 0;
      for (int y = 0; y < cover.length; y++) {
        for (int x = 0; x < cover[y].length; x++) {
          int temp = (cover[y][x] >> 16) & 0xff;
          if ((temp == peak) && (index < secret.length())) {
            temp += Integer.parseInt(secret.substring(index, index + 1), 2);
            index++;
            Color pixel = new Color(temp, (cover[y][x] >> 8) & 0xff, cover[y][x] & 0xff, (cover[y][x] >> 24) & 0xff);
            cover[y][x] = pixel.getRGB();
          }
        }
      }
    }

    // Jika posisi Peak disebelah kanan Zero
    else if (peak > zero) {
      for (int y = 0; y < cover.length; y++) {
        for (int x = 0; x < cover[y].length; x++) {
          int temp = (cover[y][x] >> 16) & 0xff;
          if (temp == zero) {
            secret += "0";
          } else if (temp == (zero + 1)) {
            secret += "1";
          }
          // Shift the Histogram
          if (temp < peak && temp > zero) {
            Color pixel = new Color(temp - 1, (cover[y][x] >> 8) & 0xff, cover[y][x] & 0xff,
                (cover[y][x] >> 24) & 0xff);
            cover[y][x] = pixel.getRGB();
          }
        }
      }

      // Embeded message
      int index = 0;
      for (int y = 0; y < cover.length; y++) {
        for (int x = 0; x < cover[y].length; x++) {
          int temp = (cover[y][x] >> 16) & 0xff;
          if ((temp == peak) && (index < secret.length())) {
            temp -= Integer.parseInt(secret.substring(index, index + 1), 2);
            index++;
            Color pixel = new Color(temp, (cover[y][x] >> 8) & 0xff, cover[y][x] & 0xff, (cover[y][x] >> 24) & 0xff);
            cover[y][x] = pixel.getRGB();
          }
        }
      }
    }

    // Jika posisi Peak sama dengan Zero
    else {
      System.out.println("Gambar tidak bisa digunakan!");
      System.exit(0);
    }

    histogram = Helper.getHistogramFromArgb(cover);
    System.out.print("Histogram : ");
    for (int i = 0; i < histogram.length; i++) {
      System.out.print(histogram[i] + " ");
    }
    System.out.println();

    // Buat file gambar yang sudah disisipkan pesan
    BufferedImage image = new BufferedImage(cover[0].length, cover.length, BufferedImage.TYPE_4BYTE_ABGR);
    for (int y = 0; y < cover.length; y++) {
      for (int x = 0; x < cover[y].length; x++) {
        image.setRGB(x, y, cover[y][x]);
      }
    }
    String fileExt = Helper.getFileExtension(imageFile.getName());
    String fileOutput = "E:/Coding/Java/Project/Steganography/src/embedded_images/" + imageFile.getName();
    Helper.createImageFile(image, fileExt, fileOutput);
  }

  public void extraction(String imagePath, int peak, int zero) {

    File imageFile = new File(imagePath);
    int[][] cover = Helper.getArgbFromImage(imageFile);
    int[] histogram = Helper.getHistogramFromArgb(cover);

    System.out.print("\nHistogram : ");
    for (int i = 0; i < histogram.length; i++) {
      System.out.print(histogram[i] + " ");
    }
    System.out.println();

    String secret = "";
    if (peak < zero) {
      for (int y = 0; y < cover.length; y++) {
        for (int x = 0; x < cover[y].length; x++) {
          int temp = (cover[y][x] >> 16) & 0xff;
          if (temp == peak) {
            secret += "0";
          } else if (temp == (peak + 1)) {
            secret += "1";
          }
        }
      }
    } else if (peak > zero) {
      for (int y = 0; y < cover.length; y++) {
        for (int x = 0; x < cover[y].length; x++) {
          int temp = (cover[y][x] >> 16) & 0xff;
          if (temp == peak) {
            secret += "0";
          } else if (temp == (peak - 1)) {
            secret += "1";
          }
        }
      }
    } else {
      System.err.println("Gambar tidak dapat diproses!");
      System.exit(0);
    }

    String payload = "";
    for (int i = 0; i + 6 < histogram.length; i += 7) {
      int temp = Integer.parseInt(secret.substring(i, i + 7), 2);
      payload += (char) temp;
    }

    System.out.println("Pesan : " + payload);

    histogram = Helper.getHistogramFromArgb(cover);

    System.out.print("Histogram : ");
    for (int i = 0; i < histogram.length; i++) {
      System.out.print(histogram[i] + " ");
    }
    System.out.println();
  }
}
