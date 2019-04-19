/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private Set<String> wordSet;
    private Map<String, ArrayList<String>> lettersToWord;
    private Map<Integer, ArrayList<String>> sizeToWords;

    public AnagramDictionary(Reader reader) throws IOException {
        wordList = new ArrayList<>();
        wordSet = new HashSet<String>();
        lettersToWord = new HashMap<String, ArrayList<String>>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();
        wordLength = DEFAULT_WORD_LENGTH;
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String wordSorted = sortLetters(word);
            wordSet.add(word);
            wordList.add(word);
            int tempWordLength = word.length();
            if (sizeToWords.get(tempWordLength) != null) {
                sizeToWords.get(tempWordLength).add(word);
            } else {
                sizeToWords.put(tempWordLength, new ArrayList<String>( Arrays.asList(word)));
            }
            if (lettersToWord.get(wordSorted) != null) {
                lettersToWord.get(wordSorted).add(word);
            } else {
                lettersToWord.put(wordSorted, new ArrayList<String>( Arrays.asList(word) ));
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word)) {
            if (!word.contains(base)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        String targetWordSorted = sortLetters(targetWord); // sort target string
        if (lettersToWord.get(targetWordSorted) != null) {
            return lettersToWord.get(targetWordSorted);
        }
        return null;
    }

    public String sortLetters (String word) {
        char tempArray[] = word.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char alphabet = 'a';alphabet <='z';alphabet++) {
            String tempWord = new String(sortLetters(word + alphabet));
            if (lettersToWord.get(tempWord) != null) {
                result.addAll(lettersToWord.get(tempWord));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int searchWordsListSize = sizeToWords.get(wordLength).size();
        for (int i = random.nextInt(searchWordsListSize); i < searchWordsListSize; i = (i + 1) % searchWordsListSize) {
            String tempWord = new String(sizeToWords.get(wordLength).get(i));
            if (lettersToWord.get(sortLetters(tempWord)).size() == MIN_NUM_ANAGRAMS) {
                if (wordLength < MAX_WORD_LENGTH) wordLength++;
                return tempWord;
            }
        }
        return null;
    }
}
