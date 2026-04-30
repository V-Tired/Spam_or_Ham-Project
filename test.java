
import Emails.Anaylzer;
import Emails.Display;
import Emails.EfficiencyAnalyzer;
import Emails.Email;
import Emails.EmailFactory;
import java.util.ArrayList;
import java.util.Collections;


public class test 
{
    public static void main(String[] args) 
    {
        //Initialization
        EmailFactory factory = new EmailFactory();
        Anaylzer a = new Anaylzer();
        EfficiencyAnalyzer eAnaylzer = new EfficiencyAnalyzer();
        
        //Gather data from csv
        ArrayList<ArrayList<Email>> emails = factory.readCSVFile("spam_or_not_spam.csv");

        //-------------------------------Training Data ---------------------------------------------
        //Shuffle to improve accuracy
        Collections.shuffle(emails.get(0));
        Collections.shuffle(emails.get(1));

        //Make Training Data Sets from first 80% of Spam Emails
        int spam_size1 = (int)((emails.get(0).size()) *.8);
        ArrayList<Email> training_spam = new ArrayList<>(emails.get(0).subList(0, spam_size1));
        //Make Training Data Sets from first 80% of Spam Emails
        int ham_size1 = (int)((emails.get(1).size())*.8);
        ArrayList<Email> training_ham = new ArrayList<>(emails.get(1).subList(0, ham_size1));

        //Check training data for top words, cross reference and cancel out
        a.findHamWords(training_ham);
        a.findSpamWords(training_spam);
        a.normalize();

        //Calculate all numbers across training spam for average email object creation
        a.analyze(training_spam, "spam", false);
        a.analyze(training_ham, "ham", false);

        //Calculate Training Ham and Spam ZScores(plus StDevs), set averages in avg Email Objects
        a.calculateZScores(training_ham, "ham", false);
        a.calculateZScores(training_ham, "spam", false);

        a.calculateZScores(training_spam, "ham", false);
        a.calculateZScores(training_spam, "spam", false);

        a.setWeights();

        //-------------------------------Testing Data ---------------------------------------------
       
        //Create Testing Dataset with last 20% of Emails
        int spam_size2 = emails.get(0).size();
        int ham_size2 = emails.get(1).size();
        ArrayList<Email> testing_spam = new ArrayList<>(emails.get(0).subList(spam_size1, spam_size2));
        ArrayList<Email> testing_ham = new ArrayList<>(emails.get(1).subList(ham_size1,ham_size2));

        //Run calculations on testing dataset
        a.analyze(testing_ham, "ham",true);
        a.analyze(testing_spam, "spam",true);


        //Calculate Zscore Distances For Testing Ham
        a.calculateZScores(testing_ham, "ham", true);
        a.calculateZScores(testing_ham, "spam", true);
        
        //Calculate Zscore Distances For Testing Spam
        a.calculateZScores(testing_spam, "ham", true); 
        a.calculateZScores(testing_spam, "spam", true);


        //Calculate EuclidDistance for Testing Ham Dataset
        a.calculateEuclidDistance(testing_ham, "ham", true);
        a.calculateEuclidDistance(testing_ham, "spam", true);

        //Calculate Euclid Distance for Testing Spam Dataset
        a.calculateEuclidDistance(testing_spam, "ham", true);
        a.calculateEuclidDistance(testing_spam, "spam", true);

        //Make guesses based on values
        a.guess(testing_ham); 
        a.guess(testing_spam);
       
        ArrayList<ArrayList<Email>> completeList = new ArrayList<>();
        completeList.add(testing_spam);
        completeList.add(testing_ham);

        System.out.println("\nAnalysis Complete\n");

        //Calculate Number of Correct Guesses (guess vs actual)
        eAnaylzer.toString(testing_spam, "Spam");
        eAnaylzer.toString(testing_ham, "Ham");
        
        //---------Print testing spam and ham features to featues.csv------------------------------
        Display d = new Display();
        d.printFeatures(testing_spam, "Testing Spam Data");
        d.printFeatures(testing_ham, "\nTesting Ham Data");

        //Print average Email information
        d.printSummary(a.averageHam, "Average Training Ham Values");
        d.printSummary(a.averageSpam, "Average Training Spam Values");

        //Print Top 10 Spam Words
        d.printWords(a.spamWords.subList(0, 10));

        //Print Difference between avg ham and avg spam email values
        d.printHashMap(a.w, "\nFeatures by Average Difference: ");

        //Print the Weights 
        d.printHashMap(a.weights, "\nWeights by Difference Value: ");
    
        //Close printWriters
        d.close();

        d.view(completeList);



    }//main()
}//test
