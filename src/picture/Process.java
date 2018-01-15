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
}
