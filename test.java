
import Emails.Anaylzer;
import Emails.EfficiencyAnaylzer;
import Emails.Email;
import Emails.EmailFactory;
import java.util.ArrayList;


public class test 
{
    public static void main(String[] args) 
    {
        Email email;

        EmailFactory factory = new EmailFactory();
        ArrayList<ArrayList<Email>> emails = factory.readCSVFile("spam_or_not_spam.csv");

        Anaylzer a = new Anaylzer();
        EfficiencyAnaylzer eAnaylzer = new EfficiencyAnaylzer();

        a.findHamWords(emails.get(1));
        a.findSpamWords(emails.get(0));
        a.normalize();


        a.analyze(emails.get(0));
        a.analyze(emails.get(1));
       
        System.out.println("\nAnalysis Complete\n");

        System.out.println("Top Spam Words: " + a.spamWords + "\n");
        System.out.println("------------------------------------------------------------\n");
        System.out.println("Top Ham Words: " + a.hamWords + "\n");

        System.out.println("------------------------------------------------------------");
        System.out.println("Sample Spam Email Analysis:");
        Email toCheck = emails.get(0).get(1);
        System.out.println("ID Number: " + toCheck.getID());

        //System.out.println("Content: " + toCheck.getContent());
        System.out.println("Number of Characters: " + toCheck.getCharCount());
        System.out.println("Number of Words: " + toCheck.getWordCount());
        System.out.println("Average Word Length: " + String.format("%.2f", toCheck.getAvgWordLen()));
        System.out.println("Number of Special Characters: " + toCheck.getNumSpecialChars());
        System.out.println("Number of URLs: " + toCheck.getNumURLs());
        System.out.println("Longest Word: " + toCheck.getLongestWord());
        System.out.println("Spam Likelihood: " + toCheck.getSpamLikely());
        System.out.println("Actual: " + toCheck.spamCheck());
        System.out.println("Best Guess: " + toCheck.getGuess());
        System.out.println("------------------------------------------------------------ \n");

        eAnaylzer.toString(emails.get(0), "Spam Emails");
        eAnaylzer.toString(emails.get(1), "Ham Emails");

        //Test code
    }//main()
}//test
