package com.project.springcsvdatabase.helper;

import com.project.springcsvdatabase.model.PriceItem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceItemHelper {

    private static String Regex = "[^\\x00-\\x7F]|\\p{Punct}";

    public static String TYPE = "text/csv";

    static String[] HEADERs = {
            "Nomenclature",
            "Vendor",
            "Article",
            "Description",
            "Weight",
            "Frequency of shipment",
            "Price, rub",
            "Base price, rub",
            "Count",
            "Shipments day",
            "Number",
            "Number OEM",
            "Uses",
            "Vendor-code"
    };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (TYPE.equals(file.getContentType())
                || file.getContentType().equals("application/vnd.ms-excel")) {
            return true;
        }

        return false;
    }

    public static List<PriceItem> csvToPriceItems(InputStream is) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(bufferedReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader()
                             .withIgnoreHeaderCase()
                             .withTrim()
                             .withDelimiter(';')
                             .withHeader(HEADERs)
             );) {

            List<PriceItem> priceItemList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                PriceItem priceItem = new PriceItem (
                        csvRecord.get("Nomenclature"),
                        csvRecord.get("Vendor"),
                        csvRecord.get("Number"),
                        dataConverter(csvRecord.get("Vendor")),
                        dataConverter(csvRecord.get("Number")),
                        cutString(csvRecord.get("Description")),
                        Float.parseFloat(commaToDot(csvRecord.get("Price, rub"))),
                        Integer.parseInt(AmountCutter(csvRecord.get("Count")))
                );

                priceItemList.add(priceItem);
            }

            return priceItemList;
        } catch (IOException e) {
            throw new RuntimeException("CSV parse error: " + e.getMessage());
        }
    }

    private static String dataConverter(String column) {
        return column.replaceAll(Regex, "");
    }

    private static String commaToDot(String string){
        return string.replaceAll(",", ".");
    }

    private static String cutString(String string){
        String result = string;
        if(string.length() > 512){
            result = string.substring(0, Math.min(string.length(), 512));
        }
        return result;
    }

    private static String AmountCutter(String string){
        if(Pattern.matches("[^\\p{Punct}]", string)){
            return string;
        }
        int index = indexOf(Pattern.compile("\\p{Punct}"), string);
        var news = string.substring(index + 1);
        return news;
    }

    private static int indexOf(Pattern pattern, String s){
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }
}
