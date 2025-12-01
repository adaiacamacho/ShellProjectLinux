import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;

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
                    tryExec:{
                        String exec="";
                        String argmts="";
                        for(int i=0;i<10;i++){
                            String ch=com.valueOf(i);
                            if (ch.equals(" ")) {
                                exec=com.substring(0,i); 
                                argmts=com.substring(i);
                            }
                        }
                        String env= System.getenv("PATH");
                        String[] paths = env != null ? env.split(File.pathSeparator) : new String[0];
                        for (String path : paths) {
                            File dirNatural=new File(path);
                            File buscarDir=new File(dirNatural,exec);
                            if(buscarDir.exists() && buscarDir.canExecute()){
                               ProcessBuilder pb = new ProcessBuilder(Arrays.asList(argmts.split(" ")));
                               pb.inheritIO();
                               Process p = pb.start();
                               p.waitFor();
                               break tryExec;
                            }
                        }   
                        System.out.println(com.concat(": command not found"));
                    }
                    break;
            }
        }while(true);
        
    } 
}
