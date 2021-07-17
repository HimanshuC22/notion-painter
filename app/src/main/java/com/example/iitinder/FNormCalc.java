package com.example.iitinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class implements the match making algorithm
 * FNorm stands for Forbenius norm
 */
public class FNormCalc {
    HashMap<String, ArrayList<Double>> data;
    int PREFS_SIZE;

    public FNormCalc(HashMap<String, ArrayList<Double>> data) {
        this.data = data;
        this.PREFS_SIZE = this.data.size();
    }

    public LinkedHashMap<String, Double> compute(String key) {
        HashMap<String, Double> res = new HashMap<>();
        ArrayList<Double> prefs, refprefs;
        double sqsum;
        int i;

        refprefs = data.get(key);
        data.remove(key);
        for (Map.Entry<String, ArrayList<Double>> e : data.entrySet()) {
            prefs = e.getValue();

            sqsum = 0;
            for (i = 0; i < PREFS_SIZE; i++)
                sqsum += (prefs.get(i).doubleValue() - refprefs.get(i).doubleValue()) * (prefs.get(i).doubleValue() - refprefs.get(i).doubleValue());

            res.put(e.getKey(), sqsum);
        }

        return sortByValue(res);
    }

    public LinkedHashMap<String, Double> sortByValue(HashMap<String, Double> hm) {
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<>(hm.entrySet());
        LinkedHashMap<String, Double> res = new LinkedHashMap<>();
        Collections.sort(list, (v1, v2) -> v1.getValue().compareTo(v2.getValue()));

        for (Map.Entry<String, Double> e : list)
            res.put(e.getKey(), e.getValue());

        return res;
    }
}
