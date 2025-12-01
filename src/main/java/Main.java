import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] types= {"echo","exit","type"};
        do{
            System.out.print("$ ");
            Scanner sc=new Scanner(System.in);
            String com= sc.nextLine();
            switch (com) {
                case "exit":
                    sc.close();
                    System.exit(0);
                    break;
                case String a when a.startsWith("echo"):
                    String b=a.substring(5);
                    System.out.println(b);
                    break; 
                case String a when a.startsWith("type"):
                    String x=a.substring(5);
                    typeCase: {
                        for(int i=0;i<types.length;i++){
                            if(x.equals(types[i])){
                                System.out.println(x.concat(" is a shell builtin"));
                                break typeCase;
                            }
                        }
                        String env= System.getenv("PATH");
                        String[] paths = env != null ? env.split(File.pathSeparator) : new String[0];
                        for (String path : paths) {
                            File dirNatural=new File(path);
                            File buscarDir=new File(dirNatural,x);
                            if(buscarDir.exists() && buscarDir.canExecute()){
                                System.out.println(x.concat(" is " + buscarDir));
                                break typeCase;
                            }
                        }   
                        System.out.println(x.concat(": not found"));
                    }
                    break;
                default:
                    System.out.println(com.concat(": command not found"));
                    break;
            }
        }while(true);
        
    } 
}
