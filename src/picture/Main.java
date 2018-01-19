package picture;

import java.util.Arrays;
import java.util.List;

public class Main {

  private static final String validArguments =
          "invert <input> <output>\n" +
          "grayscale <input> <output>\n" +
          "rotate [90|180|270] <input> <output>\n" +
          "flip [H|V] <input> <output>\n" +
          "blend <input_1> <input_2> <input_...> <input_n> <output>\n" +
          "blur <input> <output>\n" +
          "mosaic <tileSize> <input_1> <input_2> <input_...> <input_n> "
              + "<output>";

  private static final List<String> processesWithMoreThanThreeArgs =
      Arrays.asList("rotate", "flip", "mosaic");

  public static void main(String[] args) {

    if (args.length < 3) {
      System.err.println("Insufficient number of arguments");
      System.out.println("Here is a list of valid arguments:");
      System.out.println(validArguments);
      System.exit(1);
    } else if (args.length < 4 &&
        (processesWithMoreThanThreeArgs.contains(args[0]))) {
      System.err.println("Insufficient number of arguments");
      System.out.println("Here is a list of valid arguments:");
      System.out.println(validArguments);
      System.exit(1);
    }

    boolean success;
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
        process.blend(pics);
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
        process.mosaic(tileSize, pics);
        break;
      default:
        System.err.println("No process called " + processType);
        System.exit(1);
    }

    success = Utils.savePicture(process.getPic(), output);

    if (!success) {
      System.err.println("Failed to save picture to " + output);
      System.exit(1);
    }
  }
}
