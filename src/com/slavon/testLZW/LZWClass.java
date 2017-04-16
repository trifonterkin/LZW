package com.slavon.testLZW;

import jdk.internal.util.xml.impl.Pair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;

/**
 * Created by slavon on 10.04.17.Upd
 */
public class LZWClass {
    public static void main(String[] args) {

        /*Для того, чтобы не морочиться с вводом путей к файлам используются
        стандартные из папки с проектом
        inputFile.txt - исходное сообщение
        file.txt - закодированное сообщение
        outputFile.txt - разкодированное сообщение*/


        System.out.println("___________COMPRESS_V1__________________");
        long startTime = System.currentTimeMillis();
        try (Scanner in = new Scanner(new File("inputFile.txt")); PrintWriter out = new PrintWriter("file.txt");) {
            compress(in, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        long time = System.currentTimeMillis() - startTime;
        System.out.println("первый вариант "+time);


        System.out.println("___________DECOMPRESS_V1__________________");

        try (Scanner in1 = new Scanner(new File("file.txt")); PrintWriter out1 = new PrintWriter("outputFile.txt");) {
            decompress(in1, out1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("___________COMPRESS_V4___________________");
        long startTimeV2 = System.currentTimeMillis();
        Path CompressPath = Paths.get("file.txt");
        try (BufferedReader in = new BufferedReader(
                     new InputStreamReader(
                             new FileInputStream("big.txt"), StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(Files.newBufferedWriter(CompressPath, StandardCharsets.UTF_8))
        )

        {
            compressV4(in,out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        long timeV2 = System.currentTimeMillis() - startTimeV2;
        System.out.println("Четвёртый вариант внешний таймер"+ timeV2);


        System.out.println("___________DECOMPRESS_V2__________________");
        Path DecompressPath = Paths.get("file.txt");
        Path OutPutPath = Paths.get("outputFile.txt");
        try (Scanner in = new Scanner(DecompressPath,"UTF-8");
             PrintWriter out = new PrintWriter(Files.newBufferedWriter(OutPutPath, StandardCharsets.UTF_8))) {

            decompressV2(in, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("___________COMPRESS_V5___________________");
        long startTimeV5 = System.currentTimeMillis();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("big.txt"), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(Files.newBufferedWriter(CompressPath, StandardCharsets.UTF_8))
        )

        {
            compressV5(in,out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long timeV5 = System.currentTimeMillis() - startTimeV5;
        System.out.println("Пятый вариант внешний таймер"+ timeV5);


        System.out.println("___________DECOMPRESS_V2__________________");
        try (Scanner in = new Scanner(DecompressPath,"UTF-8");
             PrintWriter out = new PrintWriter(Files.newBufferedWriter(OutPutPath, StandardCharsets.UTF_8))) {

            decompressV2(in, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void compressV4(BufferedReader in, PrintWriter out) throws IOException {

        long startTimeV2 = System.currentTimeMillis();
        Map<String , String> dictionary = new HashMap<>();

        Map<String  , Integer> dictionaryReserved = new HashMap<>();

        dictionaryFillingStringString(dictionary);

        Integer k = dictionaryFillingStringInteger(dictionaryReserved);


        int i = -1;
        Character firstCharacter =(char) in.read();
        Character secondCharacter=' ';

        while ((i= in.read())!=-1) {
            secondCharacter =(char) i;
            String dictionaryWord = firstCharacter.toString() + secondCharacter.toString();
            if (dictionary.containsKey(dictionaryWord)) {
                while (dictionary.containsKey(dictionaryWord)) {
                    secondCharacter = (char) in.read();
                    dictionaryWord = dictionaryWord + secondCharacter.toString();
                }

                dictionary.put(dictionaryWord, dictionaryWord);
                dictionaryReserved.put(dictionaryWord,k=k+1);
                out.print((int)dictionaryReserved.get(dictionaryWord.substring(0,dictionaryWord.length()-1))+" ");
                firstCharacter = secondCharacter;
            } else {
                dictionary.put(dictionaryWord, dictionaryWord);
                dictionaryReserved.put(dictionaryWord, k=k+1);
                out.print((int)dictionaryReserved.get(dictionaryWord.substring(0,dictionaryWord.length()-1))+" ");
                firstCharacter = secondCharacter;
            }
        }
        if ((dictionaryReserved.get(secondCharacter.toString()) != null)) {
            out.print(dictionaryReserved.get(secondCharacter.toString()));
        }
        long timeV2 = System.currentTimeMillis() - startTimeV2;
        System.out.println("Четвёртый вариант"+ timeV2);

    }

    public static void compressV5(BufferedReader in, PrintWriter out) throws IOException {
        long startTimeV5 = System.currentTimeMillis();
        Map<String, Pair> dictionary = new HashMap<String, Pair>();

        Integer k = dictionaryFillingStringPair(dictionary);

        int i = -1;
        Character firstCharacter =(char) in.read();
        Character secondCharacter = ' ';
        String dictionaryString;

        while ((i = in.read()) != -1) {
            secondCharacter = (char) i;
            dictionaryString = firstCharacter.toString() + secondCharacter.toString();
            if (dictionary.containsKey(dictionaryString)) {
                while (dictionary.containsKey(dictionaryString)) {
                    secondCharacter =(char) in.read();
                    dictionaryString = dictionaryString + secondCharacter.toString();
                }
                k = k + 1;
                dictionary.put(dictionaryString, new Pair(dictionaryString, k));
                out.print(dictionary.get(dictionaryString.substring(0, dictionaryString.length() - 1)).getNumber()+" ");
                firstCharacter = secondCharacter;
            } else {
                k = k + 1;
                dictionary.put(dictionaryString, new Pair(dictionaryString, k));
                out.print(dictionary.get(dictionaryString.substring(0, dictionaryString.length() - 1)).getNumber()+" ");
                firstCharacter = secondCharacter;
            }
        }
        if ((dictionary.get(secondCharacter.toString())!= null)) {


            out.print(dictionary.get(secondCharacter.toString()).getNumber());
        }
        long timeV5 = System.currentTimeMillis() - startTimeV5;
        System.out.println("Пятый вариант"+ timeV5);

    }

    public static void compress(Scanner in, PrintWriter out) {
        String inputString;
        String subString ="";
        String partToOutput;

        /*Заполняется начальный словарь*/

        ArrayList<String> dictionary = new ArrayList<>();
        Character endOfMessage = '#';
        dictionary.add(endOfMessage.toString());
        for (Character i = 'a'; i <= 'z'; i++) {
            dictionary.add(i.toString());
        }
        /*Читается кодируемое сообщение*/

        inputString = in.next();

        /*инициализируются переменные, задающие границы кодируемых подстрок*/
        int i1 = 0;
        int i2 = i1+2;

        while (!subString.endsWith("#")) {
            subString = inputString.substring(i1, i2);
            if (dictionary.contains(subString)) i2++;
            else {
                dictionary.add(subString);
                /*выделяется правая часть кодируемой подстроки*/
                partToOutput = inputString.substring(i1, i2-1);
                //out.print(String.format("%5s", Integer.toBinaryString(dictionary.indexOf(partToOutput))).replace(' ','0'));\

                /*на выход записывается номер в словаре правой части кодируемой подстроки*/
                out.print(dictionary.indexOf(partToOutput)+" ");
                i1=i1+subString.length()-1;
                i2=i1+2;
            }
        }
        out.close();




    }

    public static void decompressV2(Scanner in, PrintWriter out) {
        Map<Integer  , String> dictionaryReserved = new HashMap<>();
        Integer k = dictionaryFillingIntegerString(dictionaryReserved);
        int first = in.nextInt();
        out.print(dictionaryReserved.get(first));
        int second;
        String subString;
        while (in.hasNext()) {
            second = in.nextInt();
            if (!dictionaryReserved.containsKey(second)) {
                k = k + 1;
                dictionaryReserved.put(second, dictionaryReserved.get(first) + dictionaryReserved.get(first).substring(0, 1));
                out.print(dictionaryReserved.get(first) + dictionaryReserved.get(first).substring(0, 1));
                first = second;

            }
            else {
                out.print(dictionaryReserved.get(second));
                subString = dictionaryReserved.get(first) + dictionaryReserved.get(second).substring(0, 1);
                k = k + 1;
                dictionaryReserved.put(k, subString);

                        first = second;
            }
        }

        System.out.println("decompressed");

    }


    public static void decompress(Scanner in,PrintWriter out) {
        String subString;
        String subSubString;

        /*Заполняется начальный словарь*/
        ArrayList<String> dictionary = new ArrayList<>();
        for (Character i = '\u0020'; i <= '\u007F'; i++) {
            dictionary.add(i.toString());
        }

        /*читается первый закодированный элемент, это будет один символ из начального словаря*/
        String first = in.next();

        /*подаётся на вывод раскодированный первый символ, используется вспомогательный метод extract*/
        out.print(extract(dictionary,first));

        while (in.hasNext()) {

            /*читается следующий элемент для составления подстрок для заполнения словаря*/
            String second = in.next();

            /*подстрока состоит из левой части и первого символа правой части. Она добавляется в словарь*/
            subString = extract(dictionary, first) + extract(dictionary, second).charAt(0);
            dictionary.add(subString);
            /*на выход подаётся правая часть польностью*/
                out.print(extract(dictionary, second));
                /*второй прочитанный закодированный элемент на каждой итерации становится
                * первым для следующей итерации*/
                first = second;

        }
        out.close();
        System.out.println("decompressed");

    }
    /*вспомогательный метод для извлечения из словаря элемента по номеру в словаре*/
    public static String extract(ArrayList<String> dictionary,String number) {
        return dictionary.get(Integer.parseInt(number));
    }

    public static void dictionaryFillingStringString(Map<String, String> dictionary) {
        Character enterCharacter = '\n';
        dictionary.put(enterCharacter.toString(), enterCharacter.toString());

        for (Character i = '\u0020'; i <= '\u007E'; i++) {
            dictionary.put(i.toString(), i.toString());
        }
        for (Character i = '\u2012'; i <= '\u201F'; i++) {
            dictionary.put(i.toString(), i.toString());
        }
        for (Character i = '\u0080'; i <= '\u00FF'; i++) {
            dictionary.put(i.toString(), i.toString());
        }
    }

    public static Integer dictionaryFillingStringInteger(Map<String, Integer> dictionary) {
        Integer k = 0;
        Character enterCharacter = '\n';
        dictionary.put(enterCharacter.toString(), 0);
        for (Character i = '\u0020'; i <= '\u007E'; i++) {
            k = k + 1;
            dictionary.put(i.toString(), k);
        }
        for (Character i = '\u2012'; i <= '\u201F'; i++) {
            k = k + 1;
            dictionary.put(i.toString(), k);
        }
        for (Character i = '\u0080'; i <= '\u00FF'; i++) {
            k = k + 1;
            dictionary.put(i.toString(), k);
        }
        return k;
    }

    public static Integer dictionaryFillingIntegerString(Map<Integer, String> dictionary) {
        Character enterCharacter = '\n';
        Integer k = 0;
        dictionary.put(0, enterCharacter.toString());
        for (Character i = '\u0020'; i <= '\u007E'; i++) {
            k=k+1;
            dictionary.put(k,i.toString());
        }
        for (Character i = '\u2012'; i <= '\u201F'; i++) {
            k = k + 1;
            dictionary.put(k,i.toString());
        }
        for (Character i = '\u0080'; i <= '\u00FF'; i++) {
            k=k+1;
            dictionary.put(k,i.toString());
        }

        return k;

    }

    public static Integer dictionaryFillingStringPair(Map<String, Pair> dictionary) {
        Character enterCharacter = '\n';
        dictionary.put(enterCharacter.toString(), new Pair(enterCharacter.toString(), 0));
        Integer k = 0;
        for (Character i = '\u0020'; i <= '\u007E'; i++) {
            k = k + 1;
            dictionary.put(i.toString(), new Pair(i.toString(), k));
        }
        for (Character i = '\u2012'; i <= '\u201F'; i++) {
            k = k + 1;
            dictionary.put(i.toString(),new Pair(i.toString(), k));
        }
        for (Character i = '\u0080'; i <= '\u00FF'; i++) {
            k=k+1;
            dictionary.put(i.toString(),new Pair(i.toString(), k));
        }

        return k;

    }

   static class Pair {
        private String val;
        private int number;

        public Pair(String val, int number) {
            this.val = val;
            this.number = number;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "val='" + val + '\'' +
                    ", number=" + number +
                    '}';
        }
    }

}


