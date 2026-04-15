package Emails;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class EmailFactory 
{
    public ArrayList<ArrayList<Email>> readCSVFile(String FileName)
    {
        ArrayList<ArrayList<Email>> emailsArrayList = new ArrayList<>();
        ArrayList<Email> spam = new ArrayList<>();
        ArrayList<Email> ham = new ArrayList<>();
        File file = new File(FileName);

        try (Scanner scanner = new Scanner(file))
        {
            int id = 1;
            scanner.next();
            boolean isSpam;
            String[] splitContent;

            while (scanner.hasNext())
           {
                String contents = scanner.nextLine();
                String[] split = contents.split(",");
                
                int wordCount = split[0].split(" ").length;
                int charCount = contents.length()-2;
                int numSpecialChars = split[0].replaceAll("[a-zA-Z0-9\\s]", "").length();

                if (split.length > 1 && split[1].contains("0"))
                    isSpam = false;
                else
                    isSpam = true;

                int numURL = 0;

                splitContent = contents.split(",");
                splitContent = splitContent[0].split(" ");


                Email email = new Email(id, wordCount, charCount, contents, splitContent, numURL, numSpecialChars, isSpam);

                if (email.spamCheck() == true)
                    spam.add(email);
                else
                    ham.add(email);

                id = id + 1;
           }
            scanner.close();   
            emailsArrayList.add(spam);
            emailsArrayList.add(ham);
            return emailsArrayList;   
        }
        
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
            return null;
        }
        
    }//readCSVFile()

}//EmailFactory
