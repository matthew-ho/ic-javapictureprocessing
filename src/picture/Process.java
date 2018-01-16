package picture;

public class Process {

  private Picture pic;
  private Picture[] pics;

  public Process(Picture pic) {
    this.pic = pic;
  }

  public Process(Picture[] pics) {
    this.pics = pics;
  }

  public Picture getPic() {
    return pic;
  }

  public void invert() {
    int maxIntensity = 255;
    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        Color oldPixel = pic.getPixel(x, y);
        Color newPixel = new Color(maxIntensity - oldPixel.getRed(),
            maxIntensity - oldPixel.getGreen(),
            maxIntensity - oldPixel.getBlue());
        pic.setPixel(x, y, newPixel);
      }
    }
  }

  public void grayscale() {
    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        Color oldPixel = pic.getPixel(x, y);
        int average = (oldPixel.getRed() + oldPixel.getGreen() + oldPixel.getBlue()) / 3;
        Color newPixel = new Color (average, average, average);
        pic.setPixel(x, y, newPixel);
      }
    }
  }

  public void rotate(int theta) {
    switch (theta) {
      case 90:
        rotate90();
        break;
      case 180:
        rotate90();
        rotate90();
        break;
      case 270:
        rotate90();
        rotate90();
        rotate90();
        break;
    }
  }

  private void rotate90() {
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
    Picture newPic = Utils.createPicture(pic.getWidth(), pic.getHeight());

    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        newPic.setPixel(pic.getWidth() - x - 1, y, pic.getPixel(x, y));
      }
    }

    pic = newPic;
  }

  private void flipVertical() {
    Picture newPic = Utils.createPicture(pic.getWidth(), pic.getHeight());

    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        newPic.setPixel(x, pic.getHeight() - y - 1, pic.getPixel(x, y));
      }
    }

    pic = newPic;
  }

  public void blend() {
    int minWidth = minWidth();
    int minHeight = minHeight();
    int numPics = pics.length;

    Picture newPic = Utils.createPicture(minWidth, minHeight);

    for (int x = 0; x < minWidth; x++) {
      for (int y = 0; y < minHeight; y++) {
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

  private int minWidth() {
    int min = pics[0].getWidth();

    for (int i = 0; i < pics.length; i++) {
      min = Math.min(min, pics[i].getWidth());
    }

    return min;
  }

  private int minHeight() {
    int min = pics[0].getHeight();

    for (int i = 0; i < pics.length; i++) {
      min = Math.min(min, pics[i].getHeight());
    }

    return min;
  }

  public void blur() {
    Picture newPic = Utils.createPicture(pic.getWidth(), pic.getHeight());
    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        if (x == 0 || y == 0 || x == pic.getWidth() - 1 || y == pic.getHeight() - 1) {
          newPic.setPixel(x, y, pic.getPixel(x, y));
        } else {
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

          Color newPixel = new Color(redSum / 9, greenSum / 9, blueSum / 9);

          newPic.setPixel(x, y, newPixel);
        }
      }
    }

    pic = newPic;
  }
}
