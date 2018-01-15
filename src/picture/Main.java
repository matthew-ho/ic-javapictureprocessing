package picture;

public class Main {

  public static void main(String[] args) {
    boolean success = false;

    // TODO: Implement this.
    String processType = args[0];
    switch (processType) {
      case "invert":
        String input = args[1];
        String output = args[2];
        Picture pic = Utils.loadPicture(input);
        Process process = new Process(pic);
        process.invert();
        success = Utils.savePicture(process.getPic(), output);
        break;
      default:
        System.err.println("No process called " + processType);
    }
  }
}
