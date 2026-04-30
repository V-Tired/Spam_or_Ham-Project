package Emails;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//TODO: build display (GUI?)
//DUE APRIL 30

/**
 * Performs all Calculations related to Email object features
 */
public class Anaylzer 
{
    public Email averageHam;
    public Email averageSpam;

    public ArrayList<String> spamWords;
    public ArrayList<String> hamWords;
    public HashMap<String, Double> weights;
    public HashMap<String, Double> w;


    /**
     * Analyzes a list of emails to determine their spam likelihood.
     * @param emails An arrayList of Email objects to be analyzed (either ham or spam))
     */
    public void analyze(ArrayList<Email> emails, String classification, boolean testing)
    {
        //Call all calculations
        averageWordLength(emails, classification, testing);
        urlCount(emails, classification, testing);
        longestWord(emails, classification, testing);
        specialCharCount(emails, classification, testing);
        shortWordCount(emails, classification, testing);
        averageCharCount(emails, classification, testing);
        averageWordCount(emails, classification, testing);
        countSpamWords(emails, classification, testing);

        //Calculate Standard Deviation for Training Data Only
        if (testing == false)
        {
            callStDev(emails, averageSpam);
            callStDev(emails, averageHam);
        }
    }//analyze()

    /**
     * Loops through emails, combines into 1 string, and counts frequency. The top 200 words are added to the spamWords arrayList.
     * @param spamEmails an arrayList of spam Email objects. 
     */
    public void findSpamWords(ArrayList<Email> spamEmails)
    {
        String allSpam = "";
        String[] spamWord;

        //Loop through all spam emails and combine into 1 big string
        for (int i = 0; i< spamEmails.size();i++)
        {
            Email temp  = spamEmails.get(i);
            allSpam += temp.getContent();
        }
        spamWord = allSpam.replaceAll("[^a-zA-Z] ", "").split(" ");

        HashMap<String, Integer> spamCounts = new HashMap<>();
        for (String word : spamWord) 
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
        hamWords = allHam.replaceAll("[^a-zA-Z] ", " ").split(" ");

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
        this.averageHam.setContent(hamWords.toArray(String[]::new));
        this.averageSpam.setContent(spamWords.toArray(String[]::new));
    }//normalize()

    public void countSpamWords(ArrayList<Email> emails, String name, boolean testing)
    {   
        double avgCount = 0.0;
        for (int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);

            //Check for Spam Words
            ArrayList<String> spamCheck = new ArrayList<>(spamWords.subList(0, 10));
            
            double count = 0.0;

            for (int j = 0; j < spamCheck.size(); j++)
            {
                if (current.getContent().contains(spamCheck.get(j)))
                    count ++;    
            }
            current.updateSpamWordCt(count);
            avgCount += count;
        }
        avgCount = avgCount/ (emails.size());

        if (testing == false)
        {
            if (name.contentEquals("spam"))
                this.averageSpam.updateSpamWordCt(avgCount);
            else
                this.averageHam.updateSpamWordCt(avgCount);
        }
        
    }//countSpamWords

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

        if (testing == false)
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
        double avgLongest = 0.0;
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
                if (longest > 22)
                    longCount++;
                current.longestWord = longest;
                
            }
            current.longCount = longCount;
            avgLongCt += longCount;
            avgLongest += longest;
        }
        avgLongCt = avgLongCt/emails.size();
        avgLongest = avgLongest/emails.size();

        if (testing == false)
        {
            if (classification.contentEquals("spam"))
            {   this.averageSpam.longCount = avgLongCt;
                this.averageSpam.longestWord = avgLongest;
            }
            else if (classification.contentEquals("ham"))
               { this.averageHam.longCount = avgLongCt;
                this.averageHam.longestWord = avgLongest;}
        }    
    }//longestWord()

    /**
     * Loop through Email objects, check for number of occurrences of single character entries.
     * @param emails The ArrayList of Email objects to check.
     */
    public void shortWordCount(ArrayList<Email> emails, String classification, boolean testing)
    {
        //calculate
        double avgShortCt = 0.0;
        for(int i = 0; i< emails.size();i++)
        {
            Email current = emails.get(i);
            String[] words = current.getContent().split(" ");
            int shortCount = 0;
            for (String word : words) {
                if (word.length() == 1) {
                    shortCount++;
                }
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

    public void setWeights()
    //indexes = 0: SpamWordCt, 1: WordCt, 2: URLCt, 3: ShortCt, 4: LongestWord, 5: LongCt, 6: AvgWordLen, 7: CharCt, 8:SpecialCharCt
    {
        double a = Math.abs(this.averageHam.getNumSpamWords() - this.averageSpam.getNumSpamWords());
        double b = Math.abs(this.averageHam.getWordCount() - this.averageSpam.getWordCount());
        double c = Math.abs(this.averageHam.getNumURLs() - this.averageSpam.getNumURLs());
        double d = Math.abs(this.averageHam.shortCount - this.averageSpam.shortCount);
        double e = Math.abs(this.averageHam.longestWord - this.averageSpam.longestWord);
        double f = Math.abs(this.averageHam.longCount - this.averageSpam.longCount);
        double g = Math.abs(this.averageHam.getAvgWordLen() - this.averageSpam.getAvgWordLen());
        double h = Math.abs(this.averageHam.getCharCount() - this.averageSpam.getCharCount());
        double i = Math.abs(this.averageHam.getNumSpecialChars() - this.averageSpam.getNumSpecialChars());

        w = new HashMap<>();

        w.put("numSpamWords", a);
        w.put("wordCount", b);
        w.put("numURLs", c);
        w.put("shortCount", d);
        w.put("longestWord", e);
        w.put("longCount", f);
        w.put("avgWordLen", g);
        w.put("characterCt", h);
        w.put("numSpecialChars", i);

        ArrayList<String> w2 = new ArrayList<>(w.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .toList());

        double [] nums = {.2, .2, .15, .13, .1, .06, .06, .05, .05};

        //Finalize With Appropriate Names
        this.weights = new HashMap<>();
        for (int j = 0; j< nums.length; j++)
        {
            this.weights.put(w2.get(j), nums[j]);
        }

    }//setWeights()

    /**
     * Loop through Email objects, calculate spamLikely value, mark as Spam if > 7 or ham otherwise. 
     * @param emails The ArrayList of Email objects to check.
     */
    public void calculateZScores(ArrayList<Email> emails, String distanceFrom, boolean testing)
    {
        //Update ZScores for each of 9 characteristics
        Email current;
        Email compareTo;
        double[] zscore;
        double[] avgZscore = new double[9];
        Arrays.fill(avgZscore, 0);

        for (int i = 0; i < emails.size(); i++)
        {
            current = emails.get(i);
            if (distanceFrom.contentEquals("ham"))
            {
                zscore = current.zScoreHam;
                avgZscore = this.averageHam.zScoreHam;
                compareTo = this.averageHam;

            }
            else
            {
                zscore = current.zScoreSpam;
                avgZscore = this.averageSpam.zScoreSpam;
                compareTo = this.averageSpam;
            }

            //Z score of Spam Word Count
            double f0 =(current.getNumSpamWords() - compareTo.getNumSpamWords());
            f0 = f0/compareTo.standardDeviations[0];
            zscore[0] = f0;
            avgZscore[0] += f0;
        
            //Z score of Word Count
            double f1 = (current.getWordCount() - compareTo.getWordCount());
            f1 = f1/compareTo.standardDeviations[1];
            zscore[1]= f1;
            avgZscore[1] += f1;

            //Z score of URL count
            double f2 = (current.getNumURLs() - compareTo.getNumURLs());
            f2 = f2/compareTo.standardDeviations[2];
            zscore[2]= f2;
            avgZscore[2] += f2;

            //Z score of Short Words (1 character)
            double f3 = (current.shortCount - compareTo.shortCount);
            f3 = f3/compareTo.standardDeviations[3];
            zscore[3]= f3;
            avgZscore[3] += f3;

            //Z score of Longest Word Length
            double f4 = (current.longestWord - compareTo.longestWord);
            f4 = f4/compareTo.standardDeviations[4];
            zscore[4]= f4;
            avgZscore[4] += f4;

            //Z score of long word count
            double f5 = (current.longCount - compareTo.longCount);
            f5 = f5/compareTo.standardDeviations[5];
            zscore[5]= f5;
            avgZscore[5] += f5;
            
            //Z score of Average Word Length
            double f6 = (current.getAvgWordLen() - compareTo.getAvgWordLen());
            f6 = f6/compareTo.standardDeviations[6];
            zscore[6]= f6;
            avgZscore[6] += f6;
           
            //Z score of Character Count
            double f7 = (current.getCharCount() - compareTo.getCharCount());
            f7 = f7/compareTo.standardDeviations[7];
            zscore[7]= f7;
            avgZscore[7] += f7;

            //Z score of Special Character Count
            double f8 = (current.getNumSpecialChars() - compareTo.getNumSpecialChars());
            f8 = f8/compareTo.standardDeviations[8];
            zscore[8]= f8;
            avgZscore[8] += f8;

            //Average the Avg Email ZScores on last loop
            if (i == emails.size()-1)
            {
               for (int j = 0; j < avgZscore.length; j++)
                    avgZscore[j] = (avgZscore[j])/emails.size(); 
            }
        }
    }//calculateZScores()
    
    public void calculateEuclidDistance(ArrayList<Email> emails, String distanceFrom, boolean testing){
        
       Email current;
    double distance;
    double totalDistance = 0;

    for (int i = 0; i < emails.size(); i++)
    {
        current = emails.get(i);

        double[] z;

        if (distanceFrom.equals("ham"))
            z = current.zScoreHam;
        else
            z = current.zScoreSpam;

        distance = 0;
        
        for (int j = 0; j < z.length; j++)
        {
            double weight = this.weights.get(getFeatureName(j));
            distance += Math.pow(z[j], 2) * weight;
        }

        distance = Math.sqrt(distance);
        totalDistance += distance;

        if (distanceFrom.equals("ham"))
            current.updateHamDist(distance);
        else
            current.updateSpamDist(distance);
    }

    double avg = totalDistance / emails.size();

    if (!testing)
    {
        if (distanceFrom.equals("ham"))
            this.averageHam.updateHamDist(avg);
        else
            this.averageSpam.updateSpamDist(avg);
    }

}//calculateEuclidDistance()

    public String getFeatureName(int identifier)
    {
        return switch (identifier) 
        {
            case 0 -> "numSpamWords";
            case 1 -> "wordCount";
            case 2 -> "numURLs";
            case 3 -> "shortCount";
            case 4 -> "longestWord";
            case 5 -> "longCount";
            case 6 -> "avgWordLen";
            case 7 -> "characterCt";
            case 8 -> "numSpecialChars";
            default -> "";
        };
    }

    public void callStDev(ArrayList<Email> emails, Email hamOrSpam)
    { 
        //0: SpamWordCt, 1: WordCt, 2: URLCt, 3: ShortCt, 4: LongestWord, 5: LongCt, 6: AvgWordLen, 7: CharCt, 8:SpecialCharCt
        hamOrSpam.standardDeviations[0] = calcStDev(emails, hamOrSpam, 1);
        hamOrSpam.standardDeviations[1] = calcStDev(emails, hamOrSpam, 2);
        hamOrSpam.standardDeviations[2] = calcStDev(emails, hamOrSpam, 3);
        hamOrSpam.standardDeviations[3] = calcStDev(emails, hamOrSpam, 4);
        hamOrSpam.standardDeviations[4] = calcStDev(emails, hamOrSpam, 5);
        hamOrSpam.standardDeviations[5] = calcStDev(emails, hamOrSpam, 6);
        hamOrSpam.standardDeviations[6] = calcStDev(emails, hamOrSpam, 7);
        hamOrSpam.standardDeviations[7] = calcStDev(emails, hamOrSpam, 8);
        hamOrSpam.standardDeviations[8] = calcStDev(emails, hamOrSpam, 9);

    }//callStDev();

    public double calcStDev(ArrayList<Email> emails, Email hamOrSpam, int switchCase)
    {   
        double result = 0.0;
        double deviation;
        switch(switchCase)
        {//SpamWordCt, WordCt, URLCt, ShortCt, LongestWord, LongCt, AvgWordLen, CharCt, SpecialCharCt
            case 1 -> {
                //SpamWordCt
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).getNumSpamWords() - hamOrSpam.getNumSpamWords()),2);
                    result += deviation;
                }
                hamOrSpam.variances[0] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }
                
            case 2 -> {
                //WordCt
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).getWordCount() - hamOrSpam.getWordCount()),2);
                    result += deviation;
                }
                hamOrSpam.variances[1] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }
                
            case 3 -> {
                //URLCt
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).getNumURLs() - hamOrSpam.getNumURLs()),2);
                    result += deviation;
                }
                hamOrSpam.variances[2] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }

            case 4 -> {
                //ShortCt
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).shortCount - hamOrSpam.shortCount),2);
                    result += deviation;
                }
                hamOrSpam.variances[3] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }

            case 5 -> {
                //LongestWord
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).longestWord - hamOrSpam.longestWord),2);
                    result += deviation;
                }
                hamOrSpam.variances[4] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }
    
            case 6 -> {
                //LongCt
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).longCount - hamOrSpam.longCount),2);
                    result += deviation;
                }
                hamOrSpam.variances[5] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }

            case 7 -> {
                //AvgWordLen
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).getAvgWordLen() - hamOrSpam.getAvgWordLen()),2);
                    result += deviation;
                }
                hamOrSpam.variances[6] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }

            case 8 -> {
                //CharCt
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).getCharCount() - hamOrSpam.getCharCount()),2);
                    result += deviation;
                }
                hamOrSpam.variances[7] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }

            case 9 -> {
                //SpecialCharCt
                for (int i = 0; i < emails.size(); i++)
                {
                    deviation = Math.pow((emails.get(i).getNumSpecialChars()- hamOrSpam.getNumSpecialChars()),2);
                    result += deviation;
                }
                hamOrSpam.variances[8] = result/emails.size();
                result = Math.sqrt(result/(emails.size()-1));
                return result;
            }
            
            default -> {
                return result;
            }
        }
        //SpamWordCt, WordCt, URLCt, ShortCt, LongestWord, LongCt, AvgWordLen, CharCt, SpecialCharCt
            }//calcStDev

    public void guess(ArrayList<Email> emails)
    {
        Email current;
        for (int i = 0; i< emails.size();i++)
        {
            current = emails.get(i);
            //Make final guess based on SpamLikely and HamLikely values
            if (current.getDistFromSpam() < current.getDistFromHam())
                current.updateGuess(true);
            else
                current.updateGuess(false);
        }
    }//guess()

}//Analyzer
