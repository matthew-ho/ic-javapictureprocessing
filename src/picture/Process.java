package picture;

import java.util.function.BiFunction;

/**
 * A class that encapsulates and provides methods for transforming a given
 * Picture.
 */
public class Process {

  private Picture pic;

  public Process(Picture pic) {
    this.pic = pic;
  }

  public Picture getPic() {
    return pic;
  }

  /**
   * Inverts the picture (i.e creates the negative version on the picture).
   */
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

  /**
   * Modifies the picture to become a grayscale version of itself.
   */
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

  /**
   * Rotates the picture by the given angle.
   * @param angle the angle to rotate the picture by which should be 90, 180
   *              or 270.
   */
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

  /**
   * Flips the picture based on the given direction.
   * @param dir direction to flip the picture: H (horizontal) or V (vertical).
   */
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

  /**
   * Blends the given pictures together by averaging all their pixel values.
   * @param pics array of pictures to blend together.
   */
  public void blend(Picture[] pics) {
    assert (pics != null && pics.length > 0) :
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

  /**
   * Blurs the picture by setting each pixel to the average of its
   * surrounding pixels (including itself). If a pixel is on the edge of the
   * picture it is unchanged.
   */
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

  /**
   * Creates a mosaic of the given pictures by taking the first element of
   * the first picture, second element of the second picture, etc., wrapping
   * around to the first picture when the number of pictures is less than the
   * number of pixels of the smallest picture. The size of the mosaic is the
   * size of the smallest picture.
   * @param tileSize size of the mosaic tiles.
   * @param pics pictures to be used in the mosaic.
   */
  public void mosaic(int tileSize, Picture[] pics) {
    assert (pics != null && pics.length > 0) :
        "mosaic takes a not null, non-empty Picture array";

    int width = minWidth(pics) - (minWidth(pics) % tileSize);
    int height = minHeight(pics) - (minHeight(pics) % tileSize);
    Picture newPic = Utils.createPicture(width, height);

    assert (tileSize > 0 && tileSize <= width && tileSize <= height) :
        "mosaic takes a tileSize that is between 0 and "
            + "the minimum height/width";

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

  private static int minWidth(Picture[] pics) {
    int min = pics[0].getWidth();

    for (int i = 0; i < pics.length; i++) {
      min = Math.min(min, pics[i].getWidth());
    }

    return min;
  }

  private static int minHeight(Picture[] pics) {
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
