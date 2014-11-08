package me.kevingleason.nlp.server.pos.app;

import me.kevingleason.nlp.server.pos.adt.TaggedWord;
import me.kevingleason.nlp.server.pos.adt.Tagger;
import me.kevingleason.nlp.server.pos.taggers.Viterbi;
import me.kevingleason.nlp.server.pos.tokenizer.Tokenizer;
import me.kevingleason.nlp.server.pos.util.BankLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by GleasonK on 10/31/14.
 */
public class RealTime {

    /**
     * For reading and tagging an untagged selection of data.
     * @param taggedData
     * @return
     */
    static private List<List<String>> readUntaggedData(Scanner taggedData) {
        // create an empty list of sentences
        List<List<String>> rawSentences = new LinkedList<List<String>>();
        Tokenizer tokenizer = new Tokenizer(false);
        // loop through the input, one line at a time
        while (taggedData.hasNextLine()) {
            String line = taggedData.nextLine(); // read a line
            // create an empty sentence
            List<String> rawSentence = tokenizer.tokenize(line);

            // loop through the word/tag items in this sentence
            // add the tagged sentence to the sentence list
            rawSentences.add(rawSentence);
        }
        return rawSentences;
    }

    static public List<List<String>> processSentence(String sentence){
        Tokenizer tokenizer = new Tokenizer(false);
        List<List<String>> rawSentences = new LinkedList<List<String>>();

        // loop through the input, one line at a time

        List<String> rawSentence = tokenizer.tokenize(sentence);

        // loop through the word/tag items in this sentence
        // add the tagged sentence to the sentence list
        rawSentences.add(rawSentence);

        return rawSentences;
    }

    public static List<List<TaggedWord>> tagWords(List<List<String>> sentences) {


        // create a com.gleason.pos.adt.Tagger
        Tagger tagger = new Viterbi();
        List<List<TaggedWord>> taggedWords = tagger.tag(sentences);
        return taggedWords;
    }

    public static void runApp(String[] args) throws FileNotFoundException{

        // read the training data
        List<List<TaggedWord>> trainingSentences =
                BankLoader.readTaggedData(new Scanner(new File(args[0])));

        // do the training
        BankLoader.train(trainingSentences);

        Scanner s = new Scanner(System.in);
        System.out.print("Enter a sentence: ");
        String line = s.nextLine();
        while(!line.equals("")){
            List<List<String>> sentence = processSentence(line);
            List<List<TaggedWord>> taggedWords = tagWords(sentence);
            System.out.println(taggedWords);
            System.out.print("Enter another sentence: ");
            line = s.nextLine();
        }
        System.out.println(s.nextLine());

    }

    public static void main(String[] args) throws FileNotFoundException{
        args = new String[3];
        args[0] = "TreeBank/treebank.tagged.large";
        RealTime.runApp(args);
    }
}
