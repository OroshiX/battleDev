package com.isograd.exercise.n1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.PrintWriter;

public class Exercice1 {
    public void solve(int testNumber, Scanner in, PrintWriter out) {
        int N = in.nextInt();
        Map<String,Integer> map= new HashMap<>();
        for (int i = 0; i < N; i++) {
            String color = in.next();
            if(!map.containsKey(color)){
                map.put(color,0);
            }
            map.put(color,map.get(color)+1);
        }
        String r1 = pref1(map);
        out.print(r1+" ");
        map.remove(r1);
       out.print(pref1(map));
    }

    public String pref1(Map<String, Integer>map) {
        int max = 0;
        String color="";

        for (Map.Entry<String,Integer> k:map.entrySet()             ) {
            if(k.getValue()>max) {
                max=k.getValue();
                color=k.getKey();
            }
        }
        return color;
    }
}
