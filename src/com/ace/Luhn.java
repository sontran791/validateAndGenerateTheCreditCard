package com.ace;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Luhn {

    public static final String LUHN_CHECK_FAILED = "Luhn check failed";

    public static void main(String[] args) {
        System.out.println(isValidLuhn("012850003580200"));
        System.out.println(generateCheckDigit("1234567890355"));
        System.out.println(countRange("5500005555555551", "5500005555555559"));
    }

    /**
     * Accepts a card number and determines if the card number is a valid number with
     * respect to the
     * Luhn algorithm.
     *
     * @param cardNumber the card number
     * @return true if the card number is valid according to the Luhn algorithm, false
     * if not
     */
    public static boolean isValidLuhn(String cardNumber) {
        /* remove all non-numeric */
        String newCardNumber = cardNumber.replaceAll("[^0-9]+", "");
        if (newCardNumber.isEmpty()) return false;

        char checkDigit = newCardNumber.charAt(newCardNumber.length() - 1);
        String digit = generateCheckDigit(newCardNumber.substring(0, newCardNumber.length() - 1));

        /* a credit card’s number must have 13 to 16 digits */
        return newCardNumber.length() >= 13
                && newCardNumber.length() <= 16
                && checkDigit == digit.charAt(0);
    }

    /**
     * Accepts a partial card number (excluding the last digit) and generates the
     * appropriate Luhn
     * check digit for the number.
     *
     * @param cardNumber the card number (not including a check digit)
     * @return the check digit
     */
    public static String generateCheckDigit(String cardNumber) {
        int sum = getSumOfAllTheDigits(cardNumber);
        return ((sum % 10) == 0)
                ? LUHN_CHECK_FAILED
                : String.valueOf(10 - (sum % 10));
    }

    /**
     * Accepts two card numbers representing the starting and ending numbers of a
     * range of card numbers
     * and counts the number of valid Luhn card numbers that exist in the range,
     * inclusive.
     *
     * @param startRange the starting card number of the range
     * @param endRange   the ending card number of the range
     * @return the number of valid Luhn card numbers in the range, inclusive
     */
    public static int countRange(String startRange, String endRange) {
        return (int) LongStream.rangeClosed(Long.parseLong(startRange), Long.parseLong(endRange))
                .filter(i -> isValidLuhn(String.valueOf(i)))
                .count();
    }

    private static int getSumOfAllTheDigits(String cardNumber) {
        /* convert to array of int for simplicity */
        int[] digits = IntStream.range(0, cardNumber.length())
                .map(i -> Integer.parseInt(cardNumber.substring(i, i + 1)))
                .toArray();
        /* double every other starting from right */
        for (int i = digits.length - 1; i >= 0; i -= 2) {
            int j = digits[i] * 2;
            /* take the sum of digits if digit is greater than 9 */
            if (j > 9) j = j % 10 + 1;
            digits[i] = j;
        }
        return Arrays.stream(digits).sum();
    }
}
