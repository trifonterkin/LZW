import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by slavon on 10.04.17.Upd
 */
public class LZWClass {
    public static void main(String[] args) throws FileNotFoundException {

        /*Для того, чтобы не морочиться с вводом путей к файлам используются
        стандартные из папки с проектом
        inputFile.txt - исходное сообщение
        file.txt - закодированное сообщение
        outputFile.txt - разкодированное сообщение*/


        System.out.println("___________COMPRESS___________________");

        Scanner in = new Scanner(new File("inputFile.txt"));
        PrintWriter out = new PrintWriter("file.txt");
        compress(in, out);

        System.out.println("___________DECOMPRESS___________________");

        Scanner in1 = new Scanner(new File("file.txt"));
        PrintWriter out1 = new PrintWriter("outputFile.txt");
        decompress(in1,out1);

        /*для проверки запомним исходное сообщение*/
        Scanner in3 = new Scanner(new File("inputFile.txt"));

        String inputString = in3.next();


        /*для проверки запомним получившееся сообщение*/
        Scanner in2 = new Scanner(new File("outputFile.txt"));
        String outString = in2.next();
        if (inputString.equals(outString)) System.out.println("Проверка проёдена. Строки совпадают");
            else System.out.println("Проверка не проёдена. Строки не совпадают");
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

        /*На консоль выводится весь словарь*/
        System.out.println("___________СЛОВАРЬ____________________");
        dictionary.forEach(x-> {
            System.out.println(x +"     "+Integer.toString(dictionary.indexOf(x),10 ));
        });

    }

    public static void decompress(Scanner in,PrintWriter out) {
        String subString;
        String subSubString;

        /*Заполняется начальный словарь*/
        ArrayList<String> dictionary = new ArrayList<>();
        Character endOfMessage = '#';
        dictionary.add(endOfMessage.toString());
        for (Character i = 'a'; i <= 'z'; i++) {
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
        /*по завершении цикла на вывод подаётся символ конца сообщения*/
        out.print(endOfMessage);
        out.close();

        /*На консоль выводится весь словарь*/
        System.out.println("___________СЛОВАРЬ______________________");
        dictionary.forEach(x-> {
            System.out.println(x +"     "+Integer.toString(dictionary.indexOf(x),10 ));
        });

    }
    /*вспомогательный метод для извлечения из словаря элемента по номеру в словаре*/
    public static String extract(ArrayList<String> dictionary,String number) {
        return dictionary.get(Integer.parseInt(number));
    }
}


