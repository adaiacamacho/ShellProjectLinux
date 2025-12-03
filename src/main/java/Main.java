import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] types= {"echo","exit","type","pwd"};
        Scanner sc=new Scanner(System.in);
        String env= System.getenv("PATH");
        do{
            System.out.print("$ ");
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
                case String a when a.startsWith("pwd"):
                System.out.println(System.getProperty("user.dir"));
                break;
                default:
                tryExec:{
                    try {     
                        String exec=com.split(" ")[0];
                        String[] paths = env != null ? env.split(File.pathSeparator) : new String[0];
                        for (String path : paths) {
                            File dirNatural=new File(path);
                            File buscarDir=new File(dirNatural,exec);
                            if(buscarDir.exists() && buscarDir.canExecute()){
                                ProcessBuilder pb = new ProcessBuilder(Arrays.asList(com.split(" ")));
                                pb.inheritIO();
                                Process p = pb.start();
                                p.waitFor();
                                break tryExec;
                            }
                        }  
                    } catch (Exception e) {
                        System.out.println(com.concat(": command not found"));
                    } 
                    
                }
                break;
            }
        }while(true);
        
    } 
}
