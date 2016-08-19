/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author hp
 */


public class suggest {
    String q;
    class Mycomp implements Comparator<String> {
    @Override
    public int compare(String s0, String s1) {
        int w0 =editDistDP(s0,q,s0.length(),q.length());
        int w1 = editDistDP(s1,q,s1.length(),q.length());
        return (w0 < w1? -1 : (w0 == w1) ? 0 : 1);
    }
}
    int mini(int x,int y)
    {
        if(x<y)
            return x;
        else
            return y;
    }
    int min(int x, int y, int z) 
    {
        return mini(mini(x, y), z);
    }
    int editDistDP(String str1, String str2, int m, int n)
{
    // Create a table to store results of subproblems
     int dp[][] = new int[m+1][n+1];
 
    // Fill d[][] in bottom up manner
    for (int i=0; i<=m; i++)
    {
        for (int j=0; j<=n; j++)
        {
            // If first string is empty, only option is to
            // isnert all characters of second string
            if (i==0)
                dp[i][j] = j;  // Min. operations = j
 
            // If second string is empty, only option is to
            // remove all characters of second string
            else if (j==0)
                dp[i][j] = i; // Min. operations = i
 
            // If last characters are same, ignore last char
            // and recur for remaining string
            else if (str1.charAt(i-1) == str2.charAt(j-1))
                dp[i][j] = dp[i-1][j-1];
 
            // If last character are different, consider all
            // possibilities and find minimum
            else
                dp[i][j] = 1 + min(dp[i][j-1],  // Insert
                                   dp[i-1][j],  // Remove
                                   dp[i-1][j-1]); // Replace
        }
    }
 
    return dp[m][n];
}
 public ArrayList<String> sorter(ArrayList<String> sf)
 {
     Collections.sort(sf,new Mycomp());
     return sf;
 }
         
}
