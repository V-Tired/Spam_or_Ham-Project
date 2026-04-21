package Emails;
public class Email
{
    private double wordCount;
    private double charCount;
    private String content;
    private String[] splitContent;
    private double numURL;
    private double numSpecialChars;
    private final boolean isSpam;
    private final int id;
    private double avgWordLen;
    
    
    public int longestWord;
    public double shortCount;
    public double longCount;
    public double spamValue;
    public double hamValue;

    private double spamLikely;
    private boolean guess;

    public Email(int id, double wordCount, double charCount, String content, String[] splitContent, double numURL, double numSpecialChars, boolean isSpam)
    {
            this.id = id;
            this.wordCount = wordCount;
        	this.charCount = charCount;
            this.avgWordLen = 0;
        	this.content = content;
            this.splitContent = splitContent;
        	this.numURL = numURL;
        	this.numSpecialChars = numSpecialChars;
        	this.isSpam = isSpam;
            this.longestWord = 0;
            this.shortCount = 0.0;
            this.spamLikely = 0.0;
            this.longCount = 0.0;
    }

    public Email(boolean isSpam)
    {
        this.isSpam = isSpam;
        this.id = -1;
        this.wordCount = 0;
        this.avgWordLen = 0;
        this.numURL = 0.0;
        this.numSpecialChars = 0;
        this.longestWord = 0;
        this.spamLikely = 0.0;
    }
    
    //Accessors
    public double getWordCount()
    {return this.wordCount;}

    public double getCharCount()
    {return this.charCount;}

    public boolean spamCheck()
    {return this.isSpam;}
    
    public double getAvgWordLen()
    {return this.avgWordLen;}

    public double getNumSpecialChars()
    {return this.numSpecialChars;}

    public int getLongestWord()
    {return this.longestWord;}

    public boolean getGuess()
    {return this.guess;}
    
    public String getContent()
    {return this.content;}

    public String[] getSplitContent()
    {return this.splitContent;}

    public double getSpamLikely()
    {return this.spamLikely;}
    
    public int getID()
    {return this.id;}

    public double getNumURLs()
    {return this.numURL;}

    //Mutators

    public void updateShortCount(double count)
    {this.shortCount = count;}

    public void updateAvgWordLen(double count)
    {this.avgWordLen = count;}

    public void updateSpamLikely(double update)
    {this.spamLikely += update;}

    public void updateURLCount(double count)
    {this.numURL = count;}

    public void updateLongestWord(int length)
    {this.longestWord = length;}

    public void updateGuess(boolean guess)
    {this.guess = guess;}

    public void setContent(String[] content)
    {this.splitContent = content;}

    public void updateNumSpecialChars(double count)
    {this.numSpecialChars = count;}

    public void updateNumChars(double count)
    {this.charCount = count;}

    public void updateWordCount(double count)
    {this.wordCount = count;}

    //Comparator
    public double compareToSpam(Email email)
    {
        this.spamValue = Math.abs(this.spamLikely - email.spamLikely);
        return this.spamValue;
    }//compareToSpam()

        public double compareToHam(Email email)
    {
        this.hamValue = Math.abs(this.spamLikely - email.spamLikely);
        return this.hamValue;
    }//compareToHam()

}