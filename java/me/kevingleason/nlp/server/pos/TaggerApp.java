package me.kevingleason.nlp.server.pos;/*
  com.gleason.pos.TaggerApp.java

  This program is an application designed to train and test a
  part-of-speech tagger.

  author: John Donaldson
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import me.kevingleason.nlp.server.pos.adt.TaggedWord;
import me.kevingleason.nlp.server.pos.adt.Tagger;
import me.kevingleason.nlp.server.pos.taggers.Viterbi;
import me.kevingleason.nlp.server.pos.util.BankLoader;

public class TaggerApp {

    /*
      probability tables for use by Taggers
    */
    static private Map<String, Double> transitionProbs =
            new HashMap<String, Double>();
    static private Map<String, Double> observationProbs =
            new HashMap<String, Double>();
    static private Map<String, Double> inverseProbs =
            new HashMap<String, Double>();
    static private Set<String> allTags = new HashSet<String>();

    /*
      static getter methods for use by Taggers
    */
    static public Map<String, Double> getTransitionProbs() {
        return transitionProbs;
    }

    static public Map<String, Double> getObservationProbs() {
        return observationProbs;
    }

    static public Map<String, Double> getInverseProbs() {
        return inverseProbs;
    }

    static public Set<String> getAllTags() {
        return allTags;
    }

    /*
      compare(List<List<com.gleason.pos.adt.TaggedWord>>,List<List<com.gleason.pos.adt.TaggedWord>>);

      This method compares two lists of tagged sentences and computes the
      fraction of discrepancies between the two.  It assumes that s1 
      is the standard and that s2 is the list to be tested.
    */
    static private double compare(List<List<TaggedWord>> s1, List<List<TaggedWord>> s2) {
        int wordCount = 0;
        int hits = 0;
        int misses = 0;

        ListIterator<List<TaggedWord>> s2Iterator;
        if (s2 != null)
            s2Iterator = s2.listIterator();
        else
            s2Iterator = null;
        for (List<TaggedWord> s1Sentence : s1) {
            List<TaggedWord> s2Sentence;
            ListIterator<TaggedWord> s2WordIterator;
            if (s2Iterator != null && s2Iterator.hasNext()) {
                s2Sentence = s2Iterator.next();
                if (s2Sentence != null)
                    s2WordIterator = s2Sentence.listIterator();
                else
                    s2WordIterator = null;
            } else {
                s2Sentence = null;
                s2WordIterator = null;
            }

            for (TaggedWord tword1 : s1Sentence) {
                wordCount++;
                if (s2WordIterator == null || !s2WordIterator.hasNext())
                    misses++;
                else {
                    TaggedWord tword2 = s2WordIterator.next();
                    if (tword1.getTag().equals(tword2.getTag()))
                        hits++;
                    else
                        misses++;
                }
            }
        }

        return (double) hits / wordCount;
    }

    public static void runApp(String[] args) throws FileNotFoundException{
        // read the training data
        List<List<TaggedWord>> trainingSentences = BankLoader.readTaggedData(new Scanner(new File(args[1])));

        // do the training
        BankLoader.train(trainingSentences);

        // create a com.gleason.pos.adt.Tagger
        Tagger tagger;
        switch (Integer.parseInt(args[0])) {
//            case 1:
//                tagger = new BestTag();
//                break;
//            case 2:
//                tagger = new Greedy();
//                break;
//            case 3:
//                tagger = new InformedGreedy();
//                break;
            case 4:
                tagger = new Viterbi();
                break;
            default:
                tagger = null;
                System.err.println("Unknown tagger id : " + args[0]);
                System.exit(2);
        }

        // read the test data
        Scanner testData = new Scanner(new File(args[2]));
        List<List<String>> rawSentences;
        double score = 0d;
        if (args[2].length() > 4 && args[2].substring(args[2].length()-4).equals("test")) {
            List<List<TaggedWord>> testSentences = BankLoader.readTaggedData(testData);

            // strip off the tags
            rawSentences = BankLoader.strip(testSentences);

            List<List<TaggedWord>> taggedSentences = tagger.tag(rawSentences);
            System.out.println(taggedSentences);
            score = compare(testSentences, taggedSentences);
            System.out.println(score);
        } else {
            rawSentences = BankLoader.readUntaggedData(testData);
            // tag the data
            List<List<TaggedWord>> taggedSentences = tagger.tag(rawSentences);
            System.out.println(taggedSentences);
        }
    }


    /*
      main method

      The main method expects exactly three command-line arguments:
      
      1:  an integer representing which com.gleason.pos.adt.Tagger should be used in
      this run of the program. (1=com.gleason.pos.taggers.BestTag,2=com.gleason.pos.taggers.Greedy,3=com.gleason.pos.taggers.InformedGreedy,4=com.gleason.pos.taggers.Viterbi)

      2:  the name of a file containing tagged training data for the com.gleason.pos.adt.Tagger

      3:  the name of a file containing a tagged test data set
    */
    static public void main(String[] args) throws IOException {
        //TODO: remove
        args = new String[3];
        args[0] = "4";
        args[1] = "TreeBank/treebank.tagged";
        args[2] = "TreeBank/KevinWords.txt";
        //Todo: End remove

        if (args.length != 3) {
            System.err.println("Usage:  java com.gleason.pos.TaggerApp <version> <training-file> <test-file>");
            System.exit(1);
        }



        runApp(args);
    }

}