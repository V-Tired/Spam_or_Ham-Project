package Emails;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Display 
{
    
    public PrintWriter p;
    public PrintWriter p2;

    public Display()
    {
        try
        {
            File file = new File("features.csv");
            p = new PrintWriter(file);

            File file2 = new File("summary.csv");
            p2 = new PrintWriter(file2);
        }
        catch(FileNotFoundException e)
        {}
    }//Display()
    
    public void printFeatures(ArrayList<Email> data, String name)
    {
        p.println(name);
        p.println("ID, WordCount, AverageWordLength, CharacterCount, SpecialCharacterCount, URLCount, LongWordCount, ShortWordCount, LongestWord, NumSpamWords, SpamDistance, HamDistance, Guess");
        for(int i = 0; i<data.size(); i++)
        {
            p.print(String.valueOf(data.get(i).getID()));
            p.print(", ");
            p.print(String.valueOf(data.get(i).getWordCount()));
            p.print(", ");
            p.print(String.format("%.2f",data.get(i).getAvgWordLen()));
            p.print(", ");
            p.print(data.get(i).getCharCount());
            p.print(", ");
            p.print(data.get(i).getNumSpecialChars());
            p.print(", ");
            p.print(data.get(i).getNumURLs());
            p.print(", ");
            p.print(data.get(i).longCount);
            p.print(", ");
            p.print(data.get(i).shortCount);
            p.print(", ");
            p.print(data.get(i).longestWord);
            p.print(", ");
            p.print(data.get(i).getNumSpamWords());
            p.print(", ");
            p.print(String.format("%.2f",data.get(i).getDistFromSpam()));
            p.print(", ");
            p.print(String.format("%.2f",data.get(i).getDistFromHam()));
            p.print(", ");
            p.println(data.get(i).getGuess() ? "SPAM" : "HAM");
        }
    }//print_features()

    public void printSummary(Email email, String name)
    {
        p2.println(name);
        p2.println("-----------------------------");
        p2.println("Average Word Count: " + String.format("%.2f", email.getWordCount()));
        p2.println("Average Word Length: " + String.format("%.2f", email.getAvgWordLen()));
        p2.println("Average Character Count: " + String.format("%.2f", email.getCharCount()));
        p2.println("Average Longest Word: " + String.format("%.2f", email.longestWord));
        p2.println("Average Special Character Count: " + String.format("%.2f", email.getNumSpecialChars()));
        p2.println("Average Number of URLs: " + String.format("%.2f", email.getNumURLs()));
        p2.println("Average Spam Word Count: " + String.format("%.2f", email.getNumSpamWords()));
        printArrays(email,name);
        p2.println("--------------------------------------------------");
        p2.println();

    }//printSummary()

    public void printWords(List<String> list)
    {
        p2.println("Top 10 Spam Words: ");
        for (int i = 0; i< list.size(); i++)
            {
                p2.print(list.get(i) + " ");
            }
    }//printWords()

    public void print(String toPrint)
    {p2.println(toPrint);}//print()

    public void print(HashMap<String,Double> map)
    {p2.println(map);}//print()

    public void printArrays(Email email, String name)
    {
        String formatted;
        double[] zScore;

        if (email.standardDeviations != null)
        {
            p2.println("\nStandard Deviations for " + name + ":");
            p2.println("NumSpamWords, WordCt, URLCt, ShortCt, LongestWord, LongCt, AvgWordLen, CharCt, SpecialCharCt: ");
            formatted = Arrays.stream(email.standardDeviations)
                    .mapToObj(d -> String.format("%.2f", d))
                    .collect(Collectors.joining(", ", "[", "]"));
            p2.println(formatted);
        }

        if (email.variances != null)
        {
            p2.println("\nVariance for " + name + ":");
            p2.println("NumSpamWords, WordCt, URLCt, ShortCt, LongestWord, LongCt, AvgWordLen, CharCt, SpecialCharCt: ");
            formatted = Arrays.stream(email.variances)
                    .mapToObj(d -> String.format("%.2f", d))
                    .collect(Collectors.joining(", ", "[", "]"));
            p2.println(formatted);  
        }
    }//printArrays()

    public void printHashMap(HashMap<String,Double> map, String name)
    {
        
        p2.println("\n" + name);
        for (Map.Entry<String, Double> entry : map.entrySet()) 
        {
            String formattedValue = String.format("%.2f", entry.getValue());
            p2.print(entry.getKey() + ": " + formattedValue + ", ");
        }
    }
    
    public void view (ArrayList<ArrayList<Email>> emails)
    {
        ArrayList<Email> data;
        Scanner in = new Scanner(System.in);
        System.out.println("Would you like to view a spam or ham email?");
        String choice = in.nextLine();
        if (choice.contains("spam"))
            data = emails.get(0);
        else
            data = emails.get(1);

        System.out.println("This dataset has " + data.size() + " emails in it.");
        System.out.println("Please enter the number of the email you would like to view: " );
        int id = in.nextInt()-1;

        System.out.println(data.get(id).getID());
        System.out.println(data.get(id).getWordCount());
        System.out.println(data.get(id).getAvgWordLen());
        System.out.println(data.get(id).getDistFromHam());
        System.out.println(data.get(id).getDistFromSpam());
        System.out.println(data.get(id).getCharCount());
        System.out.println(data.get(id).getNumSpamWords());
        System.out.println(data.get(id).getNumSpecialChars());
        System.out.println(Arrays.toString(data.get(id).zScoreHam));
        System.out.println(Arrays.toString(data.get(id).zScoreSpam));

    }

    public void close()
    {
        p.close();
        p2.close();
    }

}//Display
