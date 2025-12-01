import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        //TODO: Uncomment the code below to pass the first stage
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
                default:
                    System.out.println(com.concat(": command not found"));
                    break;
            }
        }while(true);
        
    }
}
