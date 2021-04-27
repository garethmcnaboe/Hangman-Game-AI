package com.company;

import java.util.*;
import java.math.*;
import java.math.BigInteger;
import java.lang.Object.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main{

    public static void main (String[] args){
        Scanner myscanner = new Scanner(System.in);
        int num = Integer.parseInt(myscanner.nextLine());
        String[] dictionary = new String[num];
        for(int i=0;i<num;i++){
            dictionary[i]=myscanner.nextLine();
        }
        String hash = dictionary[num-1]+(int)(Math.random()*100);
        int games = 100;
        int score = 0;
        for(int x=0;x<games;x++){

            Random r = new Random();
            String target = dictionary[r.nextInt(num)];

            String blackout="";
            for(int i=0;i<target.length();i++){
                blackout=blackout+"_";
            }

            Brain mybrain = new Brain(Arrays.copyOf(dictionary, dictionary.length), blackout);
            int lives=8;

            boolean running = true;

            while(running){
                char guess = mybrain.guessLetter();
                String original = mybrain.hiddenWord;
                char[] arrayform = original.toCharArray();
                for(int i=0;i<target.length();i++){
                    if(target.charAt(i)==guess){
                        arrayform[i]=guess;
                    }
                }
                String newform = "";
                for(int i=0;i<target.length();i++){
                    newform=newform+arrayform[i];
                }
                mybrain.hiddenWord=newform;
                if(newform.equals(original)){
                    lives=lives-1;
                }
                if(lives==0){
                    running=false;
                }
                if(mybrain.hiddenWord.equals(target)){
                    running=false;
                    score=score+1;
                }
            }
        }
        System.out.println("You got "+score+" correct out of 100");
        try{
            System.out.println("Your Receipt: "+sha256(hash+score));
        }catch(NoSuchAlgorithmException e){};
    }

    public static String sha256(String input) throws NoSuchAlgorithmException {
        byte[] in = hexStringToByteArray(input);
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(in);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if(len%2==1){
            s=s+"@";
            len++;
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
class Brain{

    //public String[] dictionary;
    //public String hiddenWord="_____";

    //public Brain(String[] wordlist, String target){
    //    dictionary = wordlist;
    //    hiddenWord = target;
    // }

    public String[] dictionary;
    public String hiddenWord = "_______";

    public char[] priorGuesses;
    public int numPriorGuesses = 0;

    public String[] revisedDictionary;
    public int rDictionaryWordCount = 0;

    public boolean isFirstRound = true;

    public Brain(String[]wordlist, String target){
        dictionary = wordlist;
        hiddenWord = target;
        priorGuesses = new char[26];
    }

    public char guessLetter(){
        //System.out.println(hiddenWord);

        //fill this in so as to guess the hiddenWord with the least number of guesses
        //keep track of your used letters so you're using a new letter
        //check the hiddenWord so you can see what letters are known and which ones are unknown
        //unknown characters are marked with an underscore ("_")
        //this method should return a character that is a good guess
        //Random r = new Random();
        //return (char)(r.nextInt(26)+'a');

        //checks to see if it is the first round and if so calls method to cut down dictionary based on length only
        //first round is defined as all rounds until a correct guess occurs.
        if(isFirstRound = true) {
            checkFirstRound(hiddenWord);
        }

        //if it is the first round call the method to perform 1st cut down.
        if(isFirstRound){
            firstCutDown(dictionary);
        }
        //Otherwise call method which compares available letters and cuts down dictionary that way.
        else{
            furtherCutDowns(revisedDictionary);
        }

        //calls method which checks what is the most common character in the remaining dictionary.
        char guess = mostCommonChar(revisedDictionary);

        //add the guess to the prior Guesses arrays and increment counter.
        priorGuesses[numPriorGuesses] = guess;
        numPriorGuesses++;

        //return the guess to the main method.
        //System.out.println(guess);
        return guess;
    }


    //Method to count the most popular letter in an array of Strings
    public char mostCommonChar(String[] revisedDictionary){
        int highest = 16;
        boolean alreadyUsed;

        //create a hash table for 26 letters and populate it with zeros
        int[] HashTable = new int[26];
        for(int i = 0; i<26; i++){
            HashTable[i] = 0;
        }

        char mostCommon = '*';

        for(int i = 0; i< rDictionaryWordCount; i++){
            for(int j = 0; j<revisedDictionary[i].length(); j++){
                char temp = revisedDictionary[i].charAt(j);
                switch(temp){
                    case 'a': HashTable[0] = HashTable[0] + 1;    break;
                    case 'b': HashTable[1] = HashTable[1] + 1;    break;
                    case 'c': HashTable[2] = HashTable[2] + 1;    break;
                    case 'd': HashTable[3] = HashTable[3] + 1;    break;
                    case 'e': HashTable[4] = HashTable[4] + 1;    break;
                    case 'f': HashTable[5] = HashTable[5] + 1;    break;
                    case 'g': HashTable[6] = HashTable[6] + 1;    break;
                    case 'h': HashTable[7] = HashTable[7] + 1;    break;
                    case 'i': HashTable[8] = HashTable[8] + 1;    break;
                    case 'j': HashTable[9] = HashTable[9] + 1;    break;
                    case 'k': HashTable[10] = HashTable[10] + 1;    break;
                    case 'l': HashTable[11] = HashTable[11] + 1;    break;
                    case 'm': HashTable[12] = HashTable[12] + 1;    break;
                    case 'n': HashTable[13] = HashTable[13] + 1;    break;
                    case 'o': HashTable[14] = HashTable[14] + 1;    break;
                    case 'p': HashTable[15] = HashTable[15] + 1;    break;
                    case 'q': HashTable[16] = HashTable[16] + 1;    break;
                    case 'r': HashTable[17] = HashTable[17] + 1;    break;
                    case 's': HashTable[18] = HashTable[18] + 1;    break;
                    case 't': HashTable[19] = HashTable[19] + 1;    break;
                    case 'u': HashTable[20] = HashTable[20] + 1;    break;
                    case 'v': HashTable[21] = HashTable[21] + 1;    break;
                    case 'w': HashTable[22] = HashTable[22] + 1;    break;
                    case 'x': HashTable[23] = HashTable[23] + 1;    break;
                    case 'y': HashTable[24] = HashTable[24] + 1;    break;
                    case 'z': HashTable[25] = HashTable[25] + 1;    break;
                    default: System.out.println("Error in Letter Counting Method");
                }
            }
        }
        //Sub-Method to check what is the highest that has not been used already.
        for(int j = 0; j<26; j++){
            alreadyUsed = false;

            //check to see if it is the most frequent letter
            if(HashTable[j]>HashTable[highest]) {

                //check whether it has been guessed before
                char possibleHighest = (char)(j+'a');
                //System.out.println(possibleHighest);
                //check the char against the 26 slots in the prior guesses array
                for(int k = 0; k<26; k++){
                    if (priorGuesses[k] == possibleHighest) {
                        //System.out.println("already guessed");
                        alreadyUsed = true;
                        break;
                    }
                }
                //if is higher and has not already been used then make it the new highest
                if(!alreadyUsed) {
                    highest = j;
                }
            }
        }

        mostCommon = (char)(highest + 'a');

        return mostCommon;
    }

    //Method to perform a cut-down based on some visible letters in the string
    public String[] furtherCutDowns(String[] revisedDictionary){
        //System.out.println("Run Subsequent Cut Down");
        int counter = 0;
        for(int i = 0; i< rDictionaryWordCount; i++){

            boolean same = true;

            for(int j = 0; j< revisedDictionary[i].length(); j++){

                if(hiddenWord.charAt(j) !='_'){
                    if(hiddenWord.charAt(j) != revisedDictionary[i].charAt(j)){
                        same = false;
                    }
                }
            }
            if (same){
                revisedDictionary[counter] = revisedDictionary[i];
                counter++;
            }
        }
        rDictionaryWordCount = counter;
        return revisedDictionary;
    }

    //Method to perform the first cut-down of the dictionary / based solely on character length
    public String[] firstCutDown(String[] dictionary){
        //System.out.println("run first cut down");
        rDictionaryWordCount = 0;
        revisedDictionary = new String[dictionary.length];
        int hiddenWordLength = hiddenWord.length();
        for(int i = 0; i<dictionary.length; i++){
            if(hiddenWordLength == dictionary[i].length()){
                revisedDictionary[rDictionaryWordCount] = dictionary[i];
                rDictionaryWordCount++;
            }
        }
        return revisedDictionary;
    }

    //Method to check to see if it is the first round of the game / no guesses
    public boolean checkFirstRound(String hiddenWord){
        for(int i = 0; i<hiddenWord.length(); i++){
            if(hiddenWord.charAt(i)!='_'){
                isFirstRound = false;
                //System.out.println("is first round is now" +isFirstRound);
            }
        }
        return isFirstRound;
    }

}