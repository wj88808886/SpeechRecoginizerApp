package com.mhgroup.function;

import android.content.Context;

import com.mhgroup.bean.IndexMatchpair;
import com.mhgroup.bean.StringMatchpair;

import java.util.ArrayList;

/**
 * Created by Jian on 2015/4/20.
 */
public class Matcher {

    private Context context = null;
    public Matcher(Context context)
    {
        this.context = context;;
    }

    public ArrayList<IndexMatchpair> Eqindex(String[] UnrevisedString, String[] RevisedString){
        int n = UnrevisedString.length;
        int m = RevisedString.length;
        ArrayList<IndexMatchpair> match = new ArrayList();
        match.add(new IndexMatchpair(0,0));
        int[][] a = new int[n+1][m+1];
        for (int i=0;i<n;i++){
            a[i+1][0]=i+1;
        }
        a[0][0] = 0;
        for (int j=0;j<m;j++){
            a[0][j+1]=j+1;
        }
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                if (UnrevisedString[i].equals(RevisedString[j])){
                    a[i+1][j+1] = a[i][j];
                    IndexMatchpair mp = new IndexMatchpair(i,j);
                    match.add(mp);
                }

            }
        }
        return match;
    }

    public ArrayList<StringMatchpair> EqString(ArrayList<IndexMatchpair> match,  String[] UnrevisedString, String[] RevisedString, boolean isChinese){
        int n = match.size();
        ArrayList<StringMatchpair> stringMatch = new ArrayList<StringMatchpair>();
        String s1;
        String s2;
        for (int i=0; i<n;i++){

            int i1 = match.get(i).index1;

            int i2 = match.get(i).index2;

            if (i<n-1){
                int j1 = match.get(i+1).index1;
                int j2 = match.get(i+1).index2;
                if (j1-i1 > 1 || j2-i2>1){
                    if (isChinese){
                        s1 = join(UnrevisedString, i1, j1,"");
                        s2 = join(RevisedString, i2, j2,"");
                    }
                    else{
                        s1 = join(UnrevisedString, i1, j1," ");
                        s2 = join(RevisedString, i2, j2," ");
                    }
                    stringMatch.add(new StringMatchpair(s1,s2));
                }
            }
            else{
                if (i1 != UnrevisedString.length-1 || i2 != RevisedString.length-1){
                    if (isChinese){
                        s1 = join(UnrevisedString, i1, UnrevisedString.length,"");
                        s2 = join(RevisedString, i2, RevisedString.length,"");
                    }
                    else {
                        s1 = join(UnrevisedString, i1, UnrevisedString.length, " ");
                        s2 = join(RevisedString, i2, RevisedString.length, " ");
                    }
                    stringMatch.add(new StringMatchpair(s1,s2));
                }
            }
        }
        return stringMatch;
    }

    public String join(String[] l, int begin, int end, String sign){
        String res = "";
        for (int i = begin; i<end; i++){
            if (i!=end-1){
                res += l[i] + sign;
            }
            else{
                res += l[i];
            }
        }
        return res;
    }

}
