//Scott Weagley COMP 424 Feistel Cipher Programming Assignment #2
//This program takes block sizes of 32 bits and generates keys of 16 bits
//Generally the key size is 64-128 bits, but this is just a demonstration

package feistelcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FeistelCipher {

    public static void main(String[] args) throws IOException { 
        
        final long halfBlockLen = 16;
        
        //This is unique key generations for 16 bits
        final long[] keyData = {
            0b1000110001110010,
            0b0001100011100101,
            0b0011000111001010,
            0b0110001110010100,
            0b1100011100101000,
            0b1000111001010001,
            0b0001110010100011,
            0b0011100101000110,
            0b0111001010001100,
            0b1110010100011000          
        };
        
        int iterations = keyData.length;
        String lineData = null, outputText = null;
        long left = 0, keyLeft = 0, keyRight = 0, right = 0;
        String inputFileName = null;
        String outputFileName = null;   
        int lineCount = 1;
        
        //Ask the user to input the menu option since encrypt and decrypt
        //use the key process in reverse order
        System.out.println("Choose a menu item:\n1. Encrypt\n2. Decrypt");
        Scanner coding = new Scanner(System.in);
        long menuOption = coding.nextInt();
        
        //Choose input file accordingly
        if(menuOption == 1){
            inputFileName = "plaintext.txt";
            outputFileName = "ciphertext.txt";  
        } else {
            inputFileName = "ciphertext.txt";
            outputFileName = "plaintext.txt";  
        }
        
        //Read one line at a time from the plaintext file
        FileReader inputFile;
        inputFile = new FileReader(inputFileName);
        BufferedReader fileData = new BufferedReader(inputFile);
        System.out.println("Reading file " + inputFileName);
        
        //Setup output text file connection
        BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
        System.out.println("Writing to file " + outputFileName);
        
        //Split up the left and right side of the line and store it as an integer
        while((lineData = fileData.readLine()) != null){            
            left = Long.parseLong(lineData.substring(0, lineData.length()/2), 2);
            right = Long.parseLong(lineData.substring(lineData.length()/2, lineData.length()), 2);
            
            if(menuOption == 1){
                    //Make the ciphertext based upon the number of iterations
                    for(int i = 0; i < iterations; i++){
                        //XOR the right side with the first array key item
                        keyRight = keyData[i]^right;

                        //Shift keyRight by 1
                        keyRight = shiftRight(keyRight, halfBlockLen);
                        
                        //XOR that data with the left side of the key/right item
                        keyLeft = left^keyRight;
                        
                        //Swap left and right side
                        if(i != iterations - 1){
                            //Swap the left and right string data
                            left = right;
                            right = keyLeft;    
                        } else {
                            //Swap the left and right string data
                            left = keyLeft;     
                        }

                    }
            } else {
                    //Make the ciphertext based upon the number of iterations
                    for(int i = iterations-1; i >= 0; i--){
                        //XOR the right side with the first array key item
                        keyRight = keyData[i]^right;
                        
                        //Shift keyRight by 1
                        keyRight = shiftRight(keyRight, halfBlockLen);
                        
                        //XOR that data with the left side of the key/right item
                        keyLeft = left^keyRight;

                        //Swap left and right
                        if(i != 0){
                            //Swap the left and right string data
                            left = right;
                            right = keyLeft;    
                        } else {
                            //Swap the left and right string data
                            left = keyLeft;     
                        }
                    } 
            }
            
            //Pad strings to be the same amount of bits
            StringBuilder strLeft = new StringBuilder();
            
            for (long toPrepend = halfBlockLen - Long.toBinaryString(left).length(); toPrepend > 0; toPrepend--) {
                strLeft.append('0');
            }

            strLeft.append(Long.toBinaryString(left));

            
            StringBuilder strRight = new StringBuilder();
            
            for (long toPrepend = halfBlockLen - Long.toBinaryString(right).length(); toPrepend > 0; toPrepend--) {
                strRight.append('0');
            }

            strRight.append(Long.toBinaryString(right));
            //outputText = Long.toBinaryString(left).length() + Long.toBinaryString(right);
            
            outputText = strLeft.toString() + strRight.toString();
            
            System.out.println(lineCount + ": " + outputText);
            lineCount++;
            
            //Print the output into the cipherfile text. 
            outputFile.write(outputText+"\r\n");
            outputFile.flush();            
        }   
        
        //Close the files and clear memory
        inputFile.close();
        outputFile.close();  
        
        System.out.println("Task complete.");
    }  
    
    static long shiftRight(long binary, long halfBlockLen){
        String data = Long.toBinaryString(binary);
        long[] intArray = new long[data.length()];
        
        //Put the data into an integer array
        for(int i = 0; i < data.length(); i++){
            intArray[(i+1)%data.length()] = (long)data.charAt(i)-48;         
        }
                
        data = "";
        
        for(int i = intArray.length; i < halfBlockLen; i++){
            data += Long.toString(0);
        }
            
        for(int i = 0; i < intArray.length; i++){
            data += intArray[i];
        }
        
        return Long.parseLong(data, 2);
        
    }
}
