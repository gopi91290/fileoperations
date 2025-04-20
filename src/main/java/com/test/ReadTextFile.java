package com.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReadTextFile {

    public static void main(String[] args) {
        String filePath = "src/main/resources/moby.txt";
        Map<String, Long> stringsByOccurences = readStringByOccurencesFromFile(filePath);

        List<Map.Entry<String, Long>> top5StringsCounts = stringsByOccurences.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(5).collect(Collectors.toList());
        System.out.println(top5StringsCounts);
    }


    private static Map<String, Long> readStringByOccurencesFromFile(String filePath) {
        List<String> allFileStrings = new ArrayList<>();
        List<String> excludeStrings = Arrays.asList("in", "on", "at", "he", "she", "it", "and", "or", "but", "the", "a", "an", "is", "was");
        Map<String, Long> stringOccurences = new HashMap<>();
        try {
            Files.lines(Paths.get(filePath))
                    .forEach(eachString -> {
                        String[] strArray = eachString.split(" ");
                        allFileStrings.addAll(Arrays.asList(strArray));
                    });

            stringOccurences = allFileStrings.stream()
                    .map(str -> str.replaceAll("[\"'\\;,\\.\\?]", ""))
                    .filter(str -> (!str.isBlank() && !excludeStrings.contains(str.trim().toLowerCase())))
                    .map(strword ->
                            {
                                return handlePluralWords(strword);
                            }
                    ).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (IOException e) {
            System.out.println("IOException, msg:" + e.getMessage());
        }
        return stringOccurences;
    }


    private static String handlePluralWords(String word) {
        // Some words in the file
        // List<String> pluralString = Arrays.asList("years", "hypos", "peoples", "throws", "feelings", "surrounds");
        /*if (word.toLowerCase().endsWith("s") && pluralString.contains(word)) {
            return word.substring(0, word.length() - 1).toLowerCase();
        }*/

        // Criteria as some words ends with 's' having size > 4
        if (word.toLowerCase().endsWith("s") && word.length() > 4) {
            return word.substring(0, word.length() - 1).toLowerCase();
        }
        return word.toLowerCase();
    }
}