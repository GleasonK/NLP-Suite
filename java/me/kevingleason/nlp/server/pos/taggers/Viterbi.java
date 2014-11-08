package me.kevingleason.nlp.server.pos.taggers;

import me.kevingleason.nlp.server.pos.adt.TagTree;
import me.kevingleason.nlp.server.pos.adt.TaggedWord;
import me.kevingleason.nlp.server.pos.adt.Tagger;

import java.util.LinkedList;
import java.util.List;

/*  com.gleason.pos.taggers.Viterbi

    This version of the tagger just tags every word as a noun (NN).
    
    TODO:  Replace this tagger by your best version of the tagger.  (It may
    be identical to one of your other taggers.)
*/
public class Viterbi implements Tagger {

    public List<List<TaggedWord>> tag(List<List<String>> rawList) {
        List<List<TaggedWord>> taggedList = new LinkedList<List<TaggedWord>>();
        for (List<String> rawSentence : rawList) {
            TagTree tagTree = new TagTree();
            List<TaggedWord> taggedSentence = new LinkedList<TaggedWord>();
            for (String word : rawSentence) {
                tagTree.viterbiStep(word);
            }
            taggedSentence.addAll(tagTree.traceViterbi());
            taggedList.add(taggedSentence);
        }
        return taggedList;
    }

}
