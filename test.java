
import Emails.Anaylzer;
import Emails.EfficiencyAnaylzer;
import Emails.Email;
import Emails.EmailFactory;
import java.util.ArrayList;


public class test 
{
    public static void main(String[] args) 
    {
        // Email email;
        EmailFactory factory = new EmailFactory();
        ArrayList<ArrayList<Email>> emails = factory.readCSVFile("spam_or_not_spam.csv");

        Anaylzer a = new Anaylzer();
        EfficiencyAnaylzer eAnaylzer = new EfficiencyAnaylzer();

        //Training Data
        int spam_size1 = (int)((emails.get(0).size()) *.8);
        int ham_size1 = (int)((emails.get(1).size())*.8);

        ArrayList<Email> training_spam = new ArrayList<>(emails.get(0).subList(0, spam_size1));
        ArrayList<Email> training_ham = new ArrayList<>(emails.get(1).subList(0, ham_size1));

        a.findHamWords(training_ham);
        a.findSpamWords(training_spam);
        a.normalize();

        a.analyze(training_spam, "spam", false);
        a.analyze(training_ham, "ham", false);


        //Testing
        int spam_size2 = emails.get(0).size() -1;
        int ham_size2 = emails.get(1).size() -1;

        ArrayList<Email> testing_spam = new ArrayList<>(emails.get(0).subList(spam_size1, spam_size2));
        ArrayList<Email> testing_ham = new ArrayList<>(emails.get(1).subList(ham_size1,ham_size2));


        a.analyze(testing_spam, "spam",true);
        a.analyze(testing_ham, "ham",true);
       
        System.out.println("\nAnalysis Complete\n");

        // System.out.println("Top Spam Words: " + a.spamWords.subList(0, 10) + "\n");
        // System.out.println("------------------------------------------------------------\n");
        // System.out.println("Top Ham Words: " + a.hamWords.subList(0, 10) + "\n");
        // System.out.println("------------------------------------------------------------");

        System.out.println("Average Spam Email Analysis:");
        Email avgSpam = a.averageSpam;


        System.out.println("Average # of Characters: " + avgSpam.getCharCount());
        System.out.println("Average # of Words: " + avgSpam.getWordCount());
        System.out.println("Average Word Length: " + String.format("%.2f", avgSpam.getAvgWordLen()));
        System.out.println("Aveerage # of Special Characters: " + avgSpam.getNumSpecialChars());
        System.out.println("Average # of URLs: " + avgSpam.getNumURLs());
        System.out.println("Average Long Word Count: " + avgSpam.longCount);
        System.out.println("Average Short Word Count: " + avgSpam.shortCount);
        System.out.println("Average Spam Likelihood: " + avgSpam.getSpamLikely());

        System.out.println("\nAverage Ham Email Analysis:");
    
        Email avgHam = a.averageHam;

        System.out.println("Average # of Characters: " + avgHam.getCharCount());
        System.out.println("Average # of Words: " + avgHam.getWordCount());
        System.out.println("Average Word Length: " + String.format("%.2f", avgHam.getAvgWordLen()));
        System.out.println("Aveerage # of Special Characters: " + avgHam.getNumSpecialChars());
        System.out.println("Average # of URLs: " + avgHam.getNumURLs());
        System.out.println("Average Long Word Count: " + avgHam.longCount);
        System.out.println("Average Short Word Count: " + avgHam.shortCount);
        System.out.println("Average Spam Likelihood: " + avgHam.getSpamLikely());

        eAnaylzer.toString(testing_spam, "Spam");
        eAnaylzer.toString(testing_ham, "Ham");



        //Test code
    }//main()
}//test
