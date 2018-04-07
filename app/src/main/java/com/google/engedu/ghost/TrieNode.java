/*Author: Pradnya Valsangkar*/

package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {

        TrieNode currentNode = this;
        for(int i = 0; i < s.length(); i++){
            if(!currentNode.children.containsKey(String.valueOf(s.charAt(i)))){
                currentNode.children.put(String.valueOf(s.charAt(i)), new TrieNode());
            }
            currentNode = currentNode.children.get(String.valueOf(s.charAt(i)));
        }
        currentNode.isWord = true;
    }

    public boolean isWord(String s) {
        TrieNode currentNode = this;
        for(int i = 0; i < s.length(); i++){
            if(!currentNode.children.containsKey(String.valueOf(s.charAt(i)))){
                return false;
            }
            currentNode = currentNode.children.get(String.valueOf(s.charAt(i)));
        }
        return currentNode.isWord;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode currentNode = this;
        String newString = "";
        for(int i = 0; i < s.length(); i++){
            if(!currentNode.children.containsKey(String.valueOf(s.charAt(i)))){
                return null;
            }
            newString += s.charAt(i);
            currentNode = currentNode.children.get(String.valueOf(s.charAt(i)));
        }

        if(currentNode.children.size() == 0){
            return null;
        }

        do {
            for (char i = 'a'; i <= 'z'; i++) {
                if (currentNode.children.containsKey(String.valueOf(i))) {
                    newString += i;
                    currentNode = currentNode.children.get(String.valueOf(i));
                    break;
                }
            }
        } while (!currentNode.isWord);


        return newString;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
