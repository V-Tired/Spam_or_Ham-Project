package Emails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO: make sure all features are positively effecting the statistics
//TODO: add weight distributions to each feature instead of int values 
//TODO: Remove words > 22char or <3 char from both spam and ham top word lists 
//TODO: Split into 80% testing, 20% analyzing
//TODO: implement average spam and average ham Email objects for proper comparison 
//TODO: build display (GUI?)

/**
 * 
 */
public class Anaylzer 
{
    public Email averageHam;
    public Email averageSpam;

    public ArrayList<String> spamWords;
    public ArrayList<String> hamWords;

    /**
     * Analyzes a list of emails to determine their spam likelihood.
     * @param emails An arrayList of Email objects to be analyzed (either ham or spam))
     */
    public void analyze(ArrayList<Email> emails)
    {

        averageWordLength(emails);
        urlCount(emails);
        longestWord(emails);
        compareWords(emails);
        specialCharCount(emails);
        wordCountCheck(emails);
        shortestWordCount(emails);

        makeGuess(emails);
    }

    /**
     * Loops through emails, combines into 1 string, and counts frequency. The top 200 words are added to the spamWords arrayList.
     * @param spamEmails an arrayList of spam Email objects. 
     */
    public void findSpamWords(ArrayList<Email> spamEmails)
    {
        String allSpam = "";
        String[] spamWords;

        //Loop through all spam emails and combine into 1 big string
        for (int i = 0; i< spamEmails.size();i++)
        {
            Email temp  = spamEmails.get(i);
            allSpam += temp.getContent();
        }
        spamWords = allSpam.replaceAll("[^a-zA-Z] ", "").split(" ");

        HashMap<String, Integer> spamCounts = new HashMap<>();
        for (String word : spamWords) 
        {
            if (!word.isEmpty()) 
            {
                spamCounts.put(word, spamCounts.getOrDefault(word, 0) + 1);
            }
        }
        
            this.spamWords = new ArrayList<>(spamCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(200)
            .map(Map.Entry::getKey)
            .toList());
        
    }//topSpamWords

    /**
     * Loops through emails, combines into 1 string, and counts frequency. The top 350 words are added to the hamWords arrayList.
     * @param hamEmails an arrayList of ham Email objects. 
     */
    public void findHamWords(ArrayList<Email> hamEmails)
    {
        String allHam = "";
        String[] hamWords;

        //Loop through all ham emails and combine into 1 big string
        for (int i = 0; i< hamEmails.size();i++)
        {
            Email temp  = hamEmails.get(i);
            allHam += temp.getContent();
        }
        hamWords = allHam.replaceAll("[^a-zA-Z] ", "").split(" ");

        //Put all words into a HashMap along with their count
        HashMap<String, Integer> hamCounts = new HashMap<>();
        for (String word : hamWords) 
        {
            if (!word.isEmpty()) 
            {
                hamCounts.put(word, hamCounts.getOrDefault(word, 0) + 1);
            }
        }
        //Transfer top 350 words into ArrayList for easy comparison
        this.hamWords = new ArrayList<>(hamCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(350)
            .map(Map.Entry::getKey)
            .toList());

    }//topHamWords

    /**
     * Remove overlapping words from both ham and spam ArraylLists, so that they do not affect compareWords() method.
     */
    public void normalize()
    {

        for(int j = 0; j < hamWords.size(); j++)
        {
            if (spamWords.contains(hamWords.get(j)))
            {
                spamWords.remove(hamWords.get(j));
                hamWords.remove(hamWords.get(j));
                j--;
            }
        }
    }//normalize()

    /**
     * loop throughArrayList, check average word length, add to email's values, adjust spamLikely value of Email object if avg is not b/w 4 and 7 
     * @param emails The ArrayList of Email objects to check.
     */
    public void averageWordLength(ArrayList<Email> emails)
    {
        //calculate
        for(int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);
            String[] words = current.getContent().split(" ");
            double average = 0.0;
            for (int j = 0; j< words.length;j++)
            {
                average += words[j].length();
            }
            average = average/words.length;
            current.updateAvgWordLen(average);

            if (average > 7 || average < 4)
                current.updateSpamLikely(1);
        }
    }//averageWordLength()

    /**
     * Checks the number of special characters in a given email and adjust the spamLikely value 
     * @param emails The ArrayList of Email objects to check.
     */
    public void specialCharCount(ArrayList<Email> emails)
    {
        //calculate
        for(int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);

            if (current.getNumSpecialChars() > 10)
                current.updateSpamLikely(1);
        }
    }//specialCharCount()

    /**
     * Check if top spam words are in each email, add to spamLikely value
     * @param emails The ArrayList of Email objects to check.
     */
    public void compareWords(ArrayList<Email> emails)
    {
        for (int i = 0; i < emails.size(); i++)
        {
            Email current = emails.get(i);
            for (int j = 0; j < spamWords.size(); j++)
            {
                if (current.getContent().contains(spamWords.get(j)))
                    current.updateSpamLikely(0.5);
            }
        }
    }//compareWords()

    /**
     * Loop through each Email object and check its longest word. Update Email's longest word, adjust spamLikely value if more than 22 characters and again if more than 4 of those words. 
     * @param emails The ArrayList of Email objects to check.
     */
    public void longestWord(ArrayList<Email> emails)
    {
        //calculate
        for(int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);
            String[] words = current.getContent().split(" ");
            int longest = 0;
            int longCount = 0;
            for (int j = 0; j< words.length;j++)
            {
                if (words[j].length() > longest)
                    longest = words[j].length();
                if (longest > 20)
                    longCount++;
            }

            if (longest > 22)
                current.updateSpamLikely(2);
            current.updateLongestWord(longest);
            if (longCount > 4)
                current.updateSpamLikely(1);
        }
    }//longestWord()

    /**
     * Loop through Email objects, check for number of occurrences of single character entries. Adjust spamLikely value if over 60 or 0. 
     * @param emails The ArrayList of Email objects to check.
     */
    public void shortestWordCount(ArrayList<Email> emails)
    {
        //calculate
        for(int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);
            String[] words = current.getContent().split(" ");
            int shortCount = 0;
            for (int j = 0; j< words.length;j++)
            {
                if (words[j].length() == 1)
                    shortCount++;
            }

            if (shortCount > 60)
                current.updateSpamLikely(3);
            if (shortCount < 1)
                current.updateSpamLikely(2);
        }    
    }//shortestWordCount()

    /**
     *  Count number of URL occurences, adjust email numURL, add to spamLikely 
     * @param email The ArrayList of Email objects to check.
     */
    public void urlCount(ArrayList<Email> emails)
    {
        Email current;
        int urlCount = 0;

        for(int i = 0; i< emails.size();i++)
        {
            current = emails.get(i);
            for (int j = 0; j < current.getSplitContent().length; j++)
            {
                String word = current.getSplitContent()[j];
                if (word.contains("URL") || word.contains("hyperlink") || word.contains("https://") || word.contains("www.") || word.contains("url"))
                    urlCount ++;
            }
            current.updateURLCount(urlCount);
            if (current.getNumURLs() > 1)
                current.updateSpamLikely(1);
        }
    }//urlCount()

    /**
     * Loop through Email object array, check word count for each email, adjust spamLikely value if count is between 30 and 100
     * @param emails The ArrayList of Email objects to check.
     */
    public void wordCountCheck(ArrayList<Email> emails)
    {
        for(int i = 0; i< emails.size();i++)
        {
            int count = emails.get(i).getWordCount();

            if (30 < count &&  count < 100)
                emails.get(i).updateSpamLikely(2);
        }
    }//wordCountCheck()

    /**
     * Loop through Email objects, check spamLikely value, mark as Spam if > 7 or ham otherwise. 
     * @param emails The ArrayList of Email objects to check.
     */
    public void makeGuess(ArrayList<Email> emails)
    {
        for (int i = 0; i< emails.size();i++)
        {
            if (emails.get(i).getSpamLikely() > 7)
                emails.get(i).updateGuess(true);
            else
                emails.get(i).updateGuess(false);
        }
    }//makeGuess()
}//Analyzer
