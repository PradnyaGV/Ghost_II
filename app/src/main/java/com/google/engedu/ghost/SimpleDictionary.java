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

package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    Random random=new Random();
    private ArrayList<String> words;
    private ArrayList<String> oddStarter;
    private ArrayList<String> evenStarter;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix==""|| prefix==null){
            int r=random.nextInt(9000);
            return (words.get(r));
        }
        else {
            //binary search
            int low = 0, hi = words.size() - 1, mid;
            while (low < hi) {
                mid = (low + hi) / 2;
                String midword=words.get(mid).toString();
                boolean isprefix =midword.startsWith(prefix);
                System.out.println("isprefix::" + isprefix);
                int comparison =prefix.compareTo(midword);
                System.out.println("comparisom::" + comparison);
                if (isprefix)
                    return (midword);
                else if (comparison > 0)
                    low = mid + 1;
                else
                    hi = mid - 1;
            }

            return null;
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {return "";}

//        String goodWord=getAnyWordStartingWith(prefix);
//        if(goodWord.length()%2==0)
//            evenStarter.add(goodWord);
//        else
//            oddStarter.add(goodWord);
//
//        int randomList=random.nextInt(2);
//        if(randomList==1)
//            return (oddStarter.get(new Random().nextInt(oddStarter.size())));
//        else
//            return (evenStarter.get(new Random().nextInt(evenStarter.size())));


 }

