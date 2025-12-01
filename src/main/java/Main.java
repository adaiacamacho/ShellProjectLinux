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
                    System.exit(0);
                    break;
                case String a when a.contains("echo"):
                    String b=a.substring(5);
                    System.out.println(b);
                    break; 
                case String a when a.contains("type"):
                    String x=a.substring(5);
                    for(int i=0;i<types.length;i++){
                        if(x.equals(types[i])){
                            System.out.println(x.concat(" is a shel builtin"));
                            break;
                        }
                    }
                    notfound(x);
                    break;
                default:
                    notfound(com);
                    break;
            }
        }while(true);
        
       
    } 
    public static void notfound(String command){
            System.out.println(command.concat(": command not found"));
    }
}
