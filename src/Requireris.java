import java.util.Date;
import java.util.Scanner;

/**
 * Created by remyo on 28/11/16.
 */
public class Requireris {
    public static void main(String[] args){
        try {
            if (args.length > 0) {
                System.out.println("No args required");
            }
            System.out.println("Put your shared secret :");
            Scanner sc = new Scanner(System.in);
            String secret = sc.nextLine();

            String digit;
            System.out.println("Choose digit (t = totp):");
            digit = sc.nextLine();

            HOTP generator;
            if (!digit.equals("t")) {
                long dig = Long.parseLong(digit);
                generator = new HOTP(secret.toUpperCase(), dig);
            } else {
                Date d = new Date();
                generator = new HOTP(secret.toUpperCase(), (d.getTime() / 1000) / 30);
            }
            System.out.println(generator.generateOTP());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
