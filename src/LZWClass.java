import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by slavon on 10.04.17.Upd
 */
public class LZWClass {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("___________COMPRESS___________________");
        Scanner in = new Scanner(new File("inputFile.txt"));
        PrintWriter out = new PrintWriter("file.txt");
        compress(in, out);
        System.out.println("___________DECOMPRESS___________________");
        Scanner in1 = new Scanner(new File("file.txt"));
        PrintWriter out1 = new PrintWriter("outputFile.txt");
        decompress(in1,out1);
    }

    public static void compress(Scanner in, PrintWriter out) {
        String inputString;
        String subString ="";
        String subSubString;

        ArrayList<String> strings = new ArrayList<>();
        Character endOfMessage = '#';
        strings.add(endOfMessage.toString());
        for (Character i = 'a'; i <= 'z'; i++) {
            strings.add(i.toString());
        }
        inputString = in.next();
        int i1 = 0;
        int i2 = i1+2;
        while (!subString.endsWith("#")) {
            subString = inputString.substring(i1, i2);
            if (strings.contains(subString)) i2++;
            else {
                strings.add(subString);
                subSubString = inputString.substring(i1, i2-1);
                //out.print(String.format("%5s", Integer.toBinaryString(strings.indexOf(subSubString))).replace(' ','0'));
                out.print(strings.indexOf(subSubString)+" ");
                i1=i1+subString.length()-1;
                i2=i1+2;
            }
        }
        out.close();


        System.out.println("___________СЛОВАРЬ____________________");
        strings.forEach(x-> {
            System.out.println(x +"     "+Integer.toString(strings.indexOf(x),10 ));
        });

    }

    public static void decompress(Scanner in,PrintWriter out) {
        String subString;
        String subSubString;
        ArrayList<String> strings = new ArrayList<>();
        Character endOfMessage = '#';
        strings.add(endOfMessage.toString());
        for (Character i = 'a'; i <= 'z'; i++) {
            strings.add(i.toString());
        }

        String first = in.next();
        out.print(extract(strings,first));
        while (in.hasNext()) {
            String second = in.next();
            subString = extract(strings,first) + extract(strings,second);
            if (strings.contains(subString)) out.print(extract(strings, second));
            else {
                out.print(extract(strings, second));
                subSubString = extract(strings, first) + extract(strings, second).charAt(0);
                strings.add(subSubString);
                first = second;
            }

        }
        out.print(endOfMessage);

        out.close();
        System.out.println("___________СЛОВАРЬ______________________");
        strings.forEach(x-> {
            System.out.println(x +"     "+Integer.toString(strings.indexOf(x),10 ));
        });

    }

    public static String extract(ArrayList<String> strings,String number) {
        return strings.get(Integer.parseInt(number));
    }
}


