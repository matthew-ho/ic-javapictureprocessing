package picture;

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
}
