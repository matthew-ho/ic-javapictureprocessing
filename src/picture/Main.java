package picture;

public class Main {

  public static void main(String[] args) {
    boolean success = false;
    String input;
    String output;
    Picture pic;
    Process process;

    //System.out.println(args.length);

    // TODO: Implement this.
    String processType = args[0];
    switch (processType) {
      case "invert":
        input = args[1];
        output = args[2];
        pic = Utils.loadPicture(input);
        process = new Process(pic);
        process.invert();
        success = Utils.savePicture(process.getPic(), output);
        break;
      case "grayscale":
        input = args[1];
        output = args[2];
        pic = Utils.loadPicture(input);
        process = new Process(pic);
        process.grayscale();
        success = Utils.savePicture(process.getPic(), output);
        break;
      case "rotate":
        int theta = Integer.parseInt(args[1]);
        input = args[2];
        output = args[3];
        pic = Utils.loadPicture(input);
        process = new Process(pic);
        process.rotate(theta);
        success = Utils.savePicture(process.getPic(), output);
        break;
      case "flip":
        char dir = args[1].charAt(0);
        input = args[2];
        output = args[3];
        pic = Utils.loadPicture(input);
        process = new Process(pic);
        process.flip(dir);
        success = Utils.savePicture(process.getPic(), output);
        break;
      case "blend":
        output = args[args.length - 1];
        Picture[] pics = new Picture[args.length - 2];
        for (int i = 1; i < args.length - 1; i++) {
          pics[i - 1] = Utils.loadPicture(args[i]);
        }
        process = new Process(pics);
        process.blend();
        success = Utils.savePicture(process.getPic(), output);
        break;
      case "blur":
        input = args[1];
        output = args[2];
        pic = Utils.loadPicture(input);
        process = new Process(pic);
        process.blur();
        success = Utils.savePicture(process.getPic(), output);
        break;
      default:
        System.err.println("No process called " + processType);
    }
  }
}
