package picture;

import java.util.function.BiFunction;

public class Process {

  private Picture pic;

  public Process(Picture pic) {
    this.pic = pic;
  }

  public Picture getPic() {
    return pic;
  }

  public void invert() {
    int maxIntensity = 255;

    applyProcessToAllPixels(
        (x, y) -> {
          Color oldPixel = pic.getPixel(x, y);
          Color newPixel = new Color(maxIntensity - oldPixel.getRed(),
                                     maxIntensity - oldPixel.getGreen(),
                                     maxIntensity - oldPixel.getBlue());
          return newPixel;
        }
    );
  }

  public void grayscale() {
    applyProcessToAllPixels(
        (x, y) -> {
          Color oldPixel = pic.getPixel(x, y);
          int average = (oldPixel.getRed() + oldPixel.getGreen() +
              oldPixel.getBlue()) / 3;
          Color newPixel = new Color (average, average, average);
          return newPixel;
        }
    );
  }

  public void rotate(int angle) {
    switch (angle) {
      case 90:
        rotate90Clockwise();
        break;
      case 180:
        for (int i = 0; i < 2; i++) {
          rotate90Clockwise();
        }
        break;
      case 270:
        for (int i = 0; i < 3; i++) {
          rotate90Clockwise();
        }
        break;
    }
  }

  private void rotate90Clockwise() {
    Picture newPic = Utils.createPicture(pic.getHeight(), pic.getWidth());

    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        newPic.setPixel(pic.getHeight() - y - 1, x, pic.getPixel(x, y));
      }
    }

    pic = newPic;
  }

  public void flip(char dir) {
    switch (dir) {
      case 'H':
        flipHorizontal();
        break;
      case 'V':
        flipVertical();
        break;
    }
  }

  private void flipHorizontal() {
    applyProcessToAllPixels(
        (x, y) -> {
          return pic.getPixel(pic.getWidth() - x - 1, y);
        }
    );
  }

  private void flipVertical() {
    applyProcessToAllPixels(
        (x, y) -> {
          return pic.getPixel(x, pic.getHeight() - y - 1);
        }
    );
  }

  public void blend(Picture[] pics) {
    assert (pics != null && pics.length < 0) :
        "blend takes a not null, non-empty Picture array";

    int width = minWidth(pics);
    int height = minHeight(pics);
    int numPics = pics.length;
    Picture newPic = Utils.createPicture(width, height);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;

        for (int i = 0; i < numPics; i++) {
          Color pixel = pics[i].getPixel(x, y);
          redSum += pixel.getRed();
          greenSum += pixel.getGreen();
          blueSum += pixel.getBlue();
        }

        Color newPixel = new Color(redSum / numPics, greenSum / numPics,
            blueSum / numPics);

        newPic.setPixel(x, y, newPixel);
      }
    }

    pic = newPic;
  }

  public void blur() {
    applyProcessToAllPixels(
        (x, y) -> {
          if (isOnEdgeOfPic(x, y)) {
            return pic.getPixel(x, y);
          } else {
            return averageSurroundingPixels(x, y);
          }
        }
    );
  }

  public void mosaic(int tileSize, Picture[] pics) {
    assert (pics != null && pics.length < 0) :
        "mosaic takes a not null, non-empty Picture array";
    assert (tileSize > 0) :
        "mosaic takes a tileSize that is greater than 0";

    int width = minWidth(pics) - (minWidth(pics) % tileSize);
    int height = minHeight(pics) - (minHeight(pics) % tileSize);
    Picture newPic = Utils.createPicture(width, height);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Color chosenPixel = pics[((x + y) / tileSize) % pics.length]
            .getPixel(x, y);
        newPic.setPixel(x, y, chosenPixel);
      }
    }

    pic = newPic;
  }

  private void applyProcessToAllPixels(
      BiFunction<Integer, Integer, Color> createNewPixel) {
    Picture newPic = Utils.createPicture(pic.getWidth(), pic.getHeight());

    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        newPic.setPixel(x, y, createNewPixel.apply(x, y));
      }
    }

    pic = newPic;
  }

  private int minWidth(Picture[] pics) {
    int min = pics[0].getWidth();

    for (int i = 0; i < pics.length; i++) {
      min = Math.min(min, pics[i].getWidth());
    }

    return min;
  }

  private int minHeight(Picture[] pics) {
    int min = pics[0].getHeight();

    for (int i = 0; i < pics.length; i++) {
      min = Math.min(min, pics[i].getHeight());
    }

    return min;
  }

  private boolean isOnEdgeOfPic(int x, int y) {
    return x == 0 || y == 0 ||
        x == pic.getWidth() - 1 || y == pic.getHeight() - 1;
  }

  private Color averageSurroundingPixels(int x, int y) {
    int redSum = 0;
    int greenSum = 0;
    int blueSum = 0;

    for (int i = x - 1; i < x + 2; i++) {
      for (int j = y - 1; j < y + 2; j++) {
        Color pixel = pic.getPixel(i, j);
        redSum += pixel.getRed();
        greenSum += pixel.getGreen();
        blueSum += pixel.getBlue();
      }
    }

    return new Color(redSum / 9, greenSum / 9, blueSum / 9);
  }
}
