package com.inv.invmaster001.util;

import java.math.BigDecimal;

public final class NumberToWordsUtil {

    private NumberToWordsUtil() {
    }

    private static final String[] ONES = {
            "",
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten",
            "Eleven",
            "Twelve",
            "Thirteen",
            "Fourteen",
            "Fifteen",
            "Sixteen",
            "Seventeen",
            "Eighteen",
            "Nineteen"
    };

    private static final String[] TENS = {
            "",
            "",
            "Twenty",
            "Thirty",
            "Forty",
            "Fifty",
            "Sixty",
            "Seventy",
            "Eighty",
            "Ninety"
    };

    public static String convert(BigDecimal amount) {

        long rupees = amount.longValue();

        int paise = amount
                .remainder(BigDecimal.ONE)
                .movePointRight(2)
                .intValue();

        StringBuilder builder = new StringBuilder();

        if (rupees == 0) {
            builder.append("Zero Rupees");
        } else {

            builder.append(convertNumber(rupees));

            builder.append(
                    rupees == 1
                            ? " Rupee"
                            : " Rupees"
            );
        }

        if (paise > 0) {

            builder.append(" and ");

            builder.append(convertNumber(paise));

            builder.append(
                    paise == 1
                            ? " Paisa"
                            : " Paise"
            );
        }

        builder.append(" Only");

        return builder.toString();
    }

    private static String convertNumber(long number) {

        if (number == 0) {
            return "Zero";
        }

        StringBuilder builder = new StringBuilder();

        if (number >= 10000000) {

            builder.append(
                    convertBelowThousand((int) (number / 10000000))
            );

            builder.append(" Crore ");

            number %= 10000000;
        }

        if (number >= 100000) {

            builder.append(
                    convertBelowThousand((int) (number / 100000))
            );

            builder.append(" Lakh ");

            number %= 100000;
        }
        if (number >= 1000) {

            builder.append(
                    convertBelowThousand((int) (number / 1000))
            );

            builder.append(" Thousand ");

            number %= 1000;
        }

        if (number > 0) {

            builder.append(
                    convertBelowThousand((int) number)
            );

        }

        return builder
                .toString()
                .trim();
    }

    private static String convertBelowThousand(int number) {

        StringBuilder builder =
                new StringBuilder();

        if (number >= 100) {

            builder.append(
                    ONES[number / 100]
            );

            builder.append(" Hundred");

            number %= 100;

            if (number > 0) {
                builder.append(" ");
            }
        }

        if (number >= 20) {

            builder.append(
                    TENS[number / 10]
            );

            number %= 10;

            if (number > 0) {
                builder.append(" ");
            }
        }

        if (number > 0 && number < 20) {

            builder.append(
                    ONES[number]
            );

        }

        return builder.toString();
    }

}