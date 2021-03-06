package Solutions.FourthDezember;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FourthDezemberSolution {
    public void Solution(){
        int partOne;
        int partTwo;
        List<Bingo[][]> input = new ArrayList<Bingo[][]>();
        Bingo[][] bingoPuzzle = new Bingo[5][5];
        String s="";
        String[] line;
        File inputFile = new File("rsc/input4Dezember.txt");
        Scanner scan;
        try {
            scan = new Scanner(inputFile);
            String str = String.valueOf(scan.nextLine());
            String[] bing = str.split(",");
            while(scan.hasNext()) {
                scan.nextLine();
                for(int i=0;i<bingoPuzzle.length;i++) {
                    s = String.valueOf(scan.nextLine()).strip();
                    line = s.split("\\s+");
                    for(int j=0;j<line.length;j++) {
                        bingoPuzzle[i][j] = new Bingo(line[j]);
                    }

                }
                input.add(bingoPuzzle);
                bingoPuzzle = new Bingo[5][5];
            }
           partOne = part1(bing, input);
           partTwo = part2(bing, input);
           System.out.println("The Solution is/ Part 1: " + partOne + " Part 2: " + partTwo);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static int part1(String[] bing, List<Bingo[][]> input) {
        int num;
        for(int i = 0; i< bing.length;i++) {//going through each bing
            num = Integer.valueOf(bing[i]);
            for(int j=0;j < input.size();j++) {//going through each bingo set
                for(int g=0;g<input.get(j).length;g++) {//going through each piece in bingo set
                    for(int h=0;h<input.get(j)[0].length;h++) {
                        if(input.get(j)[g][h].getNumber().equals(String.valueOf(num)))
                            input.get(j)[g][h].setMarked();
                    }
                }
                if (check(input.get(j)))
                    return getSum(input.get(j))*num;
            }
        }
        return 0;
    }
    public static int part2(String[] bing, List<Bingo[][]> input) {
        int num,ans = 0; int counter= input.size();boolean[] skip = new boolean[input.size()];

        for(int i = 0; i< bing.length;i++) {//going through each bing
            num = Integer.valueOf(bing[i]);
            for(int j=0;j < input.size();j++) {//going through each bingo set
                for(int g=0;g<input.get(j).length;g++) {//going through each piece in bingo set
                    for(int h=0;h<input.get(j)[0].length;h++) {
                        if(input.get(j)[g][h].getNumber().equals(String.valueOf(num)) && !skip[j])
                            input.get(j)[g][h].setMarked();
                    }
                }
                if (check(input.get(j)) && !skip[j]) {
                    counter--;
                    skip[j] = true;
                    if(counter == 0)
                        ans = j;
                }
            }
            if(counter == 0) {
                return getSum(input.get(ans))*num;
            }
        }
        return 0;
    }
    public static boolean check(Bingo[][] input) {
        for(int i=0;i<input.length;i++) {
            final int index = i;
            if(Arrays.stream(input[i]).allMatch(val -> val.getmarked()))
                return true;
            if(Arrays.stream(input).allMatch(val -> val[index].getmarked()))
                return true;
        }
        return false;
    }
    public static int getSum(Bingo[][] input) {
        int sum=0;
        for(int g=0;g<input.length;g++) {//going through each piece in bingo set
            for(int h=0;h<input[0].length;h++) {
                if(!input[g][h].getmarked())
                    sum+= Integer.valueOf(input[g][h].getNumber());
            }
        }
        return sum;
    }
}


class Bingo {
    private String number;
    private boolean marked;
    Bingo(String num){
        this.number=num;
        this.marked = false;
    }
    public String getNumber() {
        return this.number;
    }
    public boolean getmarked() {
        return this.marked;
    }
    public boolean setMarked() {
        return this.marked = true;
    }
}

