import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {

    // the file path is specified in the arguments provided to the program
    public static void main(String args[]) {
        StringBuilder loadedCode = new StringBuilder();
        String filePath;
        if (args.length == 0) {
            System.out.println("No FilePath provided using default");
            filePath = "program.txt";
        }else{
            filePath = args[0];
        }
        try {
            File file = new File(filePath);
            System.out.println("Trying to reading from file : " + file.getAbsolutePath());
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine())
                loadedCode.append(fileReader.nextLine()).append(fileReader.hasNextLine() ? "\n" : "");
            System.out.println("Loaded Code : \n==========================\n" + loadedCode);
        } catch (FileNotFoundException e) {
            System.out.println("That file was not found");
            return;
        }
        String code = loadedCode.toString();
        if (code.length() == 0) {
            System.out.println("No code was loaded");
            return;
        }
        BareBones.BareBonesRuntime runtime;
        try {
            runtime = BareBones.RunBareBones(code);
        }catch (Exception e){
            System.out.println("\nError during running the code");
            e.printStackTrace();
            return;
        }
        System.out.println("\nCode ran successfully\nResults are :");
        for(int i = 0; i < runtime.getVariablesNames().length; i++)
            System.out.println(runtime.getVariablesNames()[i] + " -> " + runtime.getVariablesValues()[i]);

    }
}
