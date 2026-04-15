package Emails;
public class Email
{
    private int wordCount;
    private int charCount;
    private String content;
    private String[] splitContent;
    private int numURL;
    private int numSpecialChars;
    private final boolean isSpam;
    private final int id;
    private double avgWordLen;
    private int longestWord;

    private double spamLikely;
    private boolean guess;

    public Email(int id, int wordCount, int charCount, String content, String[] splitContent, int numURL, int numSpecialChars, boolean isSpam)
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

            this.spamLikely = 0.0;
    }
    
    //Accessors
    public int getWordCount()
    {
        return this.wordCount;
    }

    public double getCharCount()
    {
        return this.charCount;
    }

    public boolean spamCheck()
    {
        return this.isSpam;
    }
    
    public double getAvgWordLen()
    {
        return this.avgWordLen;
    }

    public int getNumSpecialChars()
    {
        return this.numSpecialChars;
    }

    public int getLongestWord()
    {
        return this.longestWord;
    }

    public boolean getGuess()
    {
        return this.guess;
    }
    
    public String getContent()
    {
        return this.content;
    }

    public String[] getSplitContent()
    {
        return this.splitContent;
    }

    public double getSpamLikely()
    {
        return this.spamLikely;
    }
    public int getID()
    {
        return this.id;
    }

    public int getNumURLs()
    {
        return this.numURL;
    }

    //Mutators
    public void updateAvgWordLen(double count)
    {
        this.avgWordLen = count;
    }

    public void updateSpamLikely(double update)
    {
        this.spamLikely += update;
    }

    public void updateURLCount(int count)
    {
        this.numURL = count;
    }

    public void updateLongestWord(int length)
    {
        this.longestWord = length;
    }

    public void updateGuess(boolean guess)
    {
        this.guess = guess;
    }


    public double compareTo(Email email)
    {
        return Math.abs(this.spamLikely - email.spamLikely);
    }

}