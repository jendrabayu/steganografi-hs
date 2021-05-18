public class App {
    public static void main(String[] args) throws Exception {
        // 5.3.01.tiff => peak : 0, zero : 1
        // 5.3.02.tiff => peak : 148, zero : 1
        // 7.2.01.tiff => peak 83, zero : 0

        String originalImageFolderPath = "E:/Coding/Java/Project/Steganography/src/images/";
        String embeddedImageFolderPath = "E:/Coding/Java/Project/Steganography/src/embedded_images/";
        String[] images = { "5.3.01.tiff", "5.3.02.tiff", "7.2.01.tiff" };
        int[][] peakAndZero = { { 0, 1 }, { 148, 1 }, { 83, 0 } };

        Hs hs = new Hs();
        for (String string : images) {
            hs.embbeded(originalImageFolderPath + "" + string, "Assalamualaikum Sayangku...");
        }

        int index = 0;
        for (String string : images) {
            int peak = peakAndZero[index][0];
            int zero = peakAndZero[index][1];
            hs.extraction(embeddedImageFolderPath + string, peak, zero);
            index++;
        }

    }
}
