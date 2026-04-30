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
            boolean isSpam;
            String[] splitContent;

            while (scanner.hasNext())
           {
                String contents = scanner.nextLine();
                String[] split = contents.split(",",2);
                
                int wordCount = split[0].split(" ").length;
                String text = split[0];
                int charCount = text.length();
                int numSpecialChars = split[0].replaceAll("[a-zA-Z0-9\\s]", "").length();

                isSpam = !(split.length > 1 && split[1].contains("0"));

                int numURL = 0;

               
                splitContent = split[0].split(" ");


                Email email = new Email(id, wordCount, charCount, contents, splitContent, numURL, numSpecialChars, isSpam);

                if (email.isSpam() == true)
                    spam.add(email);
                else
                    ham.add(email);

                id += 1;
           }
            scanner.close();
            
            //Add both ArrayLists of Emails to an ArrayList
            emailsArrayList.add(spam);
            emailsArrayList.add(ham);
            return emailsArrayList;   
        }
        
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
            return new ArrayList<>();
        }
    }//readCSVFile()
}//EmailFactory
