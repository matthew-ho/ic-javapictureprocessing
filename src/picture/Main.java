package picture;

public class Main {

  public static void main(String[] args) {
    boolean success = false;
    String input = args[args.length - 2];
    String output = args[args.length - 1];
    Picture pic = Utils.loadPicture(input);
    Process process = new Process(pic);
    Picture[] pics;

    String processType = args[0];
    switch (processType) {
      case "invert":
        process.invert();
        break;
      case "grayscale":
        process.grayscale();
        break;
      case "rotate":
        int theta = Integer.parseInt(args[1]);
        process.rotate(theta);
        break;
      case "flip":
        char dir = args[1].charAt(0);
        process.flip(dir);
        break;
      case "blend":
        pics = new Picture[args.length - 2];
        for (int i = 1; i < args.length - 1; i++) {
          pics[i - 1] = Utils.loadPicture(args[i]);
        }
        process = new Process(pics);
        process.blend();
        break;
      case "blur":
        process.blur();
        break;
      case "mosaic":
        int tileSize = Integer.parseInt(args[1]);
        pics = new Picture[args.length - 3];
        for (int i = 2; i < args.length - 1; i++) {
          pics[i - 2] = Utils.loadPicture(args[i]);
        }
        process = new Process(pics);
        process.mosaic(tileSize);
        break;
      default:
        System.err.println("No process called " + processType);
    }

    success = Utils.savePicture(process.getPic(), output);
  }
}
