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
    private double spamWordCount;
    public double longestWord;
    public double shortCount;
    public double longCount;

    private double distFromAvgSpam;
    private double distFromAvgHam;
    private boolean guess;
    public double[] standardDeviations;
    public double[] zScoreHam;
    public double[] zScoreSpam;
    public double[] variances;

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
            this.distFromAvgSpam = 0.0;
            this.distFromAvgHam = 0.0;
            zScoreHam = new double[9];
            zScoreSpam = new double[9];
            this.standardDeviations = new double[9];
            this.variances = new double[9];
    }

    public Email(boolean isSpam)
    {
        this.isSpam = isSpam;
        this.id = -1;
        this.wordCount = 0;
        this.avgWordLen = 0;
        this.numURL = 0.0;
        this.numSpecialChars = 0;
        this.longestWord = 0.0;
        this.distFromAvgSpam = 0.0;
        this.distFromAvgHam = 0.0;
        this.standardDeviations = new double[9];
        this.zScoreHam = new double[9];
        this.zScoreSpam = new double[9];
        this.variances = new double[9];
    }
    
    //Accessors
    public double getWordCount()
    {return this.wordCount;}

    public double getCharCount()
    {return this.charCount;}

    public boolean isSpam()
    {return this.isSpam;}
    
    public double getAvgWordLen()
    {return this.avgWordLen;}

    public double getNumSpecialChars()
    {return this.numSpecialChars;}

    public boolean getGuess()
    {return this.guess;}
    
    public String getContent()
    {return this.content;}

    public String[] getSplitContent()
    {return this.splitContent;}

    public double getDistFromSpam()
    {return this.distFromAvgSpam;}

    public double getDistFromHam()
    {return this.distFromAvgHam;}
    
    public int getID()
    {return this.id;}

    public double getNumURLs()
    {return this.numURL;}

    public double getNumSpamWords()
    {return this.spamWordCount;}

    //Mutators
    public void updateShortCount(double count)
    {this.shortCount = count;}

    public void updateAvgWordLen(double count)
    {this.avgWordLen = count;}

    public void updateSpamDist(double update)
    {this.distFromAvgSpam = update;}

    public void updateHamDist(double update)
    {this.distFromAvgHam = update;}

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

    public void updateSpamWordCt(double count)
    {this.spamWordCount = count;}

}//Email