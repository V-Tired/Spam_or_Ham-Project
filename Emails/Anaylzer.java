package Emails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO: make sure all features are positively effecting the statistics
//TODO: add weight distributions to each feature instead of int values 
//TODO: build display (GUI?)
//TODO: calculate both ham and spam values, check which closer to, etc 
//DUE APRIL 30

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
    public void analyze(ArrayList<Email> emails, String classification, boolean testing)
    {
        averageWordLength(emails, classification, testing);
        urlCount(emails, classification, testing);
        longestWord(emails, classification, testing);
        specialCharCount(emails, classification, testing);
        shortestWordCount(emails, classification, testing);
        averageCharCount(emails, classification, testing);
        averageWordCount(emails, classification, testing);

        compareEach(emails);
        makeGuess(emails, classification, testing); 

        // if (testing == true)
        //     compareEach(emails);
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
        for(int j = 0; j < hamWords.size(); j++)
        {
            if (hamWords.get(j).length() < 3 || hamWords.get(j).length() > 22)
                {
                hamWords.remove(hamWords.get(j));
                j--;
                }
            }

    // Remove unviable words (less than 3 characters or greater than 22) from both lists
      for(int j = 0; j < spamWords.size(); j++)
        {   
            if (spamWords.get(j).length() < 3 || spamWords.get(j).length() > 22)
                {
                spamWords.remove(spamWords.get(j));
                j--;
                }
        }
        this.averageHam = new Email(false);
        this.averageSpam = new Email(true);
        this.averageHam.setContent(hamWords.toArray(new String[0]));
        this.averageSpam.setContent(spamWords.toArray(new String[0]));
    }//normalize()

    /**
     * loop throughArrayList, check average word length, add to email's values, adjust spamLikely value of Email object if avg is not b/w 4 and 7 
     * @param emails The ArrayList of Email objects to check.
     */
    public void averageWordLength(ArrayList<Email> emails, String classification, boolean testing)
    {
        //calculate
        double totalAvg= 0.0;
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

            totalAvg += average;
        }
        totalAvg = totalAvg/emails.size();

        if (testing ==false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.updateAvgWordLen(totalAvg);
            else if (classification.contentEquals("ham"))
                this.averageHam.updateAvgWordLen(totalAvg);
        }
    }//averageWordLength()

    /**
     * Checks the number of special characters in a given email and adjust the spamLikely value 
     * @param emails The ArrayList of Email objects to check.
     */
    public void specialCharCount(ArrayList<Email> emails, String classification, boolean testing)
    {
        double average = 0.0;

        //calculate
        for(int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);
            average += current.getNumSpecialChars();
        }
        average = average/emails.size();

        if (testing == false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.updateNumSpecialChars(average);
            else if (classification.contentEquals("ham"))
                this.averageHam.updateNumSpecialChars(average);
        }
    }//specialCharCount()

    public void averageCharCount(ArrayList<Email> emails, String classification, boolean testing)
    {
        double average = 0.0;
        Email current;

        //calculate
        for(int i = 0; i< emails.size();i++)
        {
            current = emails.get(i);
            average += current.getCharCount();
        }
        average = average/emails.size();
if (testing == false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.updateNumChars(average);
            else if (classification.contentEquals("ham"))
                this.averageHam.updateNumChars(average);
        }
    }
    public void averageWordCount(ArrayList<Email> emails, String classification, boolean testing)
    {
        double average = 0.0;
        Email current;

        //calculate
        for(int i = 0; i< emails.size();i++)
        {
            current = emails.get(i);
            average += current.getWordCount();
        }
        average = average/emails.size();
if (testing == false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.updateWordCount(average);
            else if (classification.contentEquals("ham"))
                this.averageHam.updateWordCount(average);
        }
    }
    /**
     * Loop through each Email object and check its longest word. Update Email's longest word, adjust spamLikely value if more than 22 characters and again if more than 4 of those words. 
     * @param emails The ArrayList of Email objects to check.
     */
    public void longestWord(ArrayList<Email> emails, String classification, boolean testing)
    {
        //calculate
        double avgLongCt = 0.0;
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
                current.longestWord = longest;
            }
            current.longCount = longCount;
            avgLongCt += longCount;
        }
        avgLongCt = avgLongCt/emails.size();

        if (testing == false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.longCount = avgLongCt;
            else if (classification.contentEquals("ham"))
                this.averageHam.longCount = avgLongCt;
        }    
        

    }//longestWord()

    /**
     * Loop through Email objects, check for number of occurrences of single character entries. Adjust spamLikely value if over 60 or 0. 
     * @param emails The ArrayList of Email objects to check.
     */
    public void shortestWordCount(ArrayList<Email> emails, String classification, boolean testing)
    {
        //calculate
        double avgShortCt = 0.0;
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
            avgShortCt += shortCount;
            current.updateShortCount(shortCount);
        }
            avgShortCt = avgShortCt / emails.size();

            if (testing == false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.updateShortCount(avgShortCt);
            else if (classification.contentEquals("ham"))
                this.averageHam.updateShortCount(avgShortCt);
        }    
    }//shortestWordCount()

    /**
     *  Count number of URL occurences, adjust email numURL, add to spamLikely 
     * @param email The ArrayList of Email objects to check.
     */
    public void urlCount(ArrayList<Email> emails, String classification, boolean testing)
    {
        Email current;
        double urlCount;
        double avgURL = 0.0;

        for(int i = 0; i< emails.size();i++)
        {
            urlCount = 0.0;
            current = emails.get(i);
            for (int j = 0; j < current.getSplitContent().length; j++)
            {
                String word = current.getSplitContent()[j];
                if (word.contains("URL") || word.contains("hyperlink") || word.contains("https://") || word.contains("www.") || word.contains("url"))
                    urlCount ++;
            }
            current.updateURLCount(urlCount);
            avgURL += urlCount;
        }
        avgURL = avgURL/emails.size();
        if (testing == false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.updateURLCount(avgURL);
            else if (classification.contentEquals("ham"))
                this.averageHam.updateURLCount(avgURL);
        }
    }//urlCount()

    /**
     * Loop through Email objects, calculate spamLikely value, mark as Spam if > 7 or ham otherwise. 
     * @param emails The ArrayList of Email objects to check.
     */
    public void makeGuess(ArrayList<Email> emails, String classification, boolean testing)
    {
        Email current;
        double averageLikely = 0;

        for (int i = 0; i< emails.size();i++)
        {
            current = emails.get(i);

            //Check for Spam Words
            ArrayList<String> spamCheck = new ArrayList<>(spamWords.subList(0, 10));
            for (int j = 0; j < spamCheck.size(); j++)
            {
                if (current.getContent().contains(spamCheck.get(j)))
                    current.updateSpamLikely(2);
            }
        
            //Check Word Count
            if (current.getWordCount() > this.averageHam.getWordCount())
                emails.get(i).updateSpamLikely(2);

            //Check URL count
            if (current.getNumURLs() > this.averageHam.getNumURLs())
                current.updateSpamLikely(.5);

            //Check number of Short Words (1 character)
            if (current.shortCount < this.averageHam.shortCount)
                current.updateSpamLikely(1);
            if (current.shortCount < 1)
                current.updateSpamLikely(2);

            //Check Longest Word Length and Number of Long Words
            if (current.longestWord > this.averageHam.getLongestWord())
                current.updateSpamLikely(2);
            if (current.longCount > averageHam.longCount)
                current.updateSpamLikely(1);
            
            //Check Average Word Length
            if (current.getAvgWordLen() > this.averageHam.getAvgWordLen())
                current.updateSpamLikely(1);
           
            //Check Average Character Count
            if (current.getCharCount()>= averageSpam.getCharCount())
                    current.updateSpamLikely(1);

            averageLikely += current.getSpamLikely();

            //Make final guess based on SpamLikely value
            double diff = averageSpam.getSpamLikely() - averageHam.getSpamLikely();

            if (current.getSpamLikely() >= diff)
                current.updateGuess(true);
            else
                current.updateGuess(false);
        }
        averageLikely = averageLikely/emails.size();
        if (testing == false)
        {
            if (classification.contentEquals("spam"))
                this.averageSpam.updateSpamLikely(averageLikely);
            else if (classification.contentEquals("ham"))
                this.averageHam.updateSpamLikely(averageLikely);
        }

    }//makeGuess()


    public void compareEach(ArrayList<Email> emails)
    {   for(int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);
            current.compareToHam(this.averageHam);
            current.compareToSpam(this.averageSpam);
        }
    }//compareEach()

}//Analyzer
