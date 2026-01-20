import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
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
                CaseEcho(a);
                break;
                case String a when a.startsWith("type"):
                String x=a.substring(5);
                CaseType(x, types, env);
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
                    CaseCdPrevious(ob2);
                    break;
                    case '~':
                    String home=System.getenv("HOME");
                    if(home.isEmpty()){
                        home=System.getProperty("user.home");
                    }
                    System.setProperty("user.dir", home);
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
                    String[] partes= new String[]{};
                    if(com.contains(">")){
                        DefCat(com, partes);
                        break tryExec;
                    }
                    try {     
                        if(DefExec(env, com)){
                        System.out.println(com.concat(": command not found"));}
                    } catch (Exception e) {
                        System.out.println(com.concat(": command not found"));
                    }     
                }
                break;
            }
        }while(true);   
    }
    public static void CaseEcho(String a) throws InterruptedException, IOException{
        if(a.contains(">")){
            String[] partes= new String[]{};
            if(a.contains("1>")){
                partes=a.split("1>");
            }else{
                partes=a.split(">");
            }
            String redir=partes[1].strip();
            ProcessBuilder pb = new ProcessBuilder(partes[0].replaceAll("'","").split(" "));
            pb.redirectOutput(new File(redir));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            pb.start().waitFor();
            return;
        }
        String b=a.substring(5);
        System.out.println(b);
    }
    public static void CaseType(String x,String[] types,String env){
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
    }
    public static void CaseCdPrevious(String ob2){
        if(ob2.startsWith("./")){
            String nuevo=ob2.substring(1);
            String previo=System.getProperty("user.dir");
            System.setProperty("user.dir", new String(previo+nuevo));
        }else if(ob2.startsWith("..")){
            String[] pasos=ob2.split("/");
            for(String p:pasos){
                if(p.equals("..")){
                    File act=new File(System.getProperty("user.dir"));
                    Path menos= act.toPath();
                    Path nuevo=menos.subpath(0, menos.getNameCount()-1);
                    System.setProperty("user.dir", new String("/"+nuevo.toString()));
                }else{
                    String moveDown=System.getProperty("user.dir");
                    System.setProperty("user.dir", new String(moveDown+"/"+p));
                }
            }
        }
    }
    public static void DefCat(String com, String[] partes) throws InterruptedException, IOException{
        if(com.contains("1>")){
            partes=com.split("1>");
        }else{
            partes=com.split(">");
        }
        String redir=partes[1].strip();
        ProcessBuilder pb = new ProcessBuilder(partes[0].split(" "));
        pb.redirectOutput(new File(redir));
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.start().waitFor();
    }
    public static boolean DefExec(String env,String com) throws IOException, InterruptedException{
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
                return false;
            }
        } 
        return true; 
    }
}

