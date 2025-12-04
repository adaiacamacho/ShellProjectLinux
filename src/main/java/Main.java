import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] types= {"echo","exit","type","pwd","cd"};
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
                case String a when a.startsWith("cd"):
                    switch (a.charAt(3)) {
                        case '/':
                            String objetivo=a.substring(3);
                            File f=new File(objetivo);
                            if(f.exists()){
                                System.setProperty("user.dir", objetivo);
                            }else{
                                System.out.println(new String("cd: " + objetivo+": No such file or directory"));
                            }
                            break;
                        case '.':
                            String ob2=a.substring(3);
                            if(ob2.startsWith("./")){
                                String nuevo=ob2.substring(1);
                                String previo=System.getProperty("user.dir");
                                System.setProperty("user.dir", new String(previo+nuevo));
                            }else if(ob2.startsWith("..")){
                                String[] pasos=ob2.split("/");
                                for(String p:pasos){
                                    if(p.equals("..")){
                                        File act=new File(System.getProperty("user.dir"));
                                        Path menos=act.toPath();
                                        Path nuevo=menos.subpath(0, menos.getNameCount()-1);
                                        System.setProperty("user.dir", nuevo.toString());
                                    }else{
                                        String moveDown=System.getProperty("user.dir");
                                        System.setProperty("user.dir", new String(moveDown+"/"+p));
                                    }
                                }
                            }
                            break;
                        default:
                            String ob3=a.substring(3);
                            File f1=new File(ob3);
                            String prev=System.getProperty("user.dir");
                            if(f1.exists()&&f1.isDirectory()&&f1.getAbsolutePath().contains(prev)){
                                System.setProperty("user.dir", new String(prev+"/"+ob3));
                            }else{
                                System.out.println(new String("cd: " + ob3+": No such file or directory"));
                            }
                            break;
                    }
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
                        System.out.println(com.concat(": command not found"));
                    } catch (Exception e) {
                        System.out.println(com.concat(": command not found"));
                    } 
                    
                }
                break;
            }
        }while(true);
        
    } 
}
