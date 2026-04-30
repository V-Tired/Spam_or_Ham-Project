package Emails;

import java.util.ArrayList;

public class EfficiencyAnalyzer 
{
    int correct;

    public int compare(ArrayList<Email> emails)
    {
        correct = 0;
        for (int i = 0; i < emails.size(); i++)
        
            if (emails.get(i).getGuess() == emails.get(i).isSpam())
            {
                correct++;
            }
        return correct;
    }//compare()

    public void toString(ArrayList<Email> emails, String type)
    {
        double u = this.compare(emails);
        double v = emails.size();
        double w = (u / v) * 100;
        System.out.print(type + " Emails Guessed Correctly: " + (int)u + " out of " + (int)v);
        System.out.println(", " + String.format("%.2f", w) + "%");
    }


}//EfficiencyAnalyzer
