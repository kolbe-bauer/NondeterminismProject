// 
// Decompiled by Procyon v0.5.36
// 

package com.aliasi.sentences;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

public class MedlineSentenceModel extends HeuristicSentenceModel implements Serializable
{
    static final long serialVersionUID = -8958290440993791272L;
    private static final Set<String> POSSIBLE_STOPS;
    private static final Set<String> IMPOSSIBLE_PENULTIMATES;
    private static final Set<String> IMPOSSIBLE_SENTENCE_STARTS;
    private static final Set<String> LOWERCASE_STARTS;
    public static final MedlineSentenceModel INSTANCE;
    
    public MedlineSentenceModel() {
        super((Set)MedlineSentenceModel.POSSIBLE_STOPS, (Set)MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES, (Set)MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS, true, true);
    }
    
    protected boolean possibleStart(final String[] tokens, final String[] whitespaces, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (MedlineSentenceModel.LOWERCASE_STARTS.contains(tokens[i])) {
                return true;
            }
            if (this.containsDigitOrUpper(tokens[i])) {
                return true;
            }
            if (whitespaces[i + 1].length() > 0) {
                return false;
            }
        }
        return false;
    }
    
    private boolean containsDigitOrUpper(final CharSequence token) {
        for (int len = token.length(), i = 0; i < len; ++i) {
            if (Character.isUpperCase(token.charAt(i))) {
                return true;
            }
            if (Character.isDigit(token.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    Object writeReplace() {
        return new MedlineSentenceModel.Serializer();
    }
    
    static {
        (POSSIBLE_STOPS = new HashSet<String>()).add(".");
        MedlineSentenceModel.POSSIBLE_STOPS.add("..");
        MedlineSentenceModel.POSSIBLE_STOPS.add("!");
        MedlineSentenceModel.POSSIBLE_STOPS.add("?");
        (IMPOSSIBLE_PENULTIMATES = new HashSet<String>()).add("Bros");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("No");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("al");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("vs");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("etc");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Fig");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Dr");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Prof");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("PhD");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("MD");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Co");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Corp");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Inc");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Jan");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Feb");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Mar");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Apr");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Jul");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Aug");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Sep");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Sept");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Oct");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Nov");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("Dec");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("St");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("AM");
        MedlineSentenceModel.IMPOSSIBLE_PENULTIMATES.add("PM");
        (IMPOSSIBLE_SENTENCE_STARTS = new HashSet<String>()).add(")");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("]");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("}");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add(">");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("<");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add(".");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("!");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("?");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add(":");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add(";");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("-");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("--");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("---");
        MedlineSentenceModel.IMPOSSIBLE_SENTENCE_STARTS.add("%");
        (LOWERCASE_STARTS = new HashSet<String>()).add("alpha");
        MedlineSentenceModel.LOWERCASE_STARTS.add("beta");
        MedlineSentenceModel.LOWERCASE_STARTS.add("gamma");
        MedlineSentenceModel.LOWERCASE_STARTS.add("delta");
        MedlineSentenceModel.LOWERCASE_STARTS.add("c");
        MedlineSentenceModel.LOWERCASE_STARTS.add("i");
        MedlineSentenceModel.LOWERCASE_STARTS.add("ii");
        MedlineSentenceModel.LOWERCASE_STARTS.add("iii");
        MedlineSentenceModel.LOWERCASE_STARTS.add("iv");
        MedlineSentenceModel.LOWERCASE_STARTS.add("v");
        MedlineSentenceModel.LOWERCASE_STARTS.add("vi");
        MedlineSentenceModel.LOWERCASE_STARTS.add("vii");
        MedlineSentenceModel.LOWERCASE_STARTS.add("viii");
        MedlineSentenceModel.LOWERCASE_STARTS.add("ix");
        MedlineSentenceModel.LOWERCASE_STARTS.add("x");
        INSTANCE = new MedlineSentenceModel();
    }
}
