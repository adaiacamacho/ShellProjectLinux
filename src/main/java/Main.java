import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        //TODO: Uncomment the code below to pass the first stage
        do{
            System.out.print("$ ");
            Scanner sc=new Scanner(System.in);
            String com= sc.nextLine();
            if(!com.equals("exit")){
                System.out.println(com.concat(": command not found"));
            }else{
                System.exit(0);
            }
        }while(true);
        
    }
}
