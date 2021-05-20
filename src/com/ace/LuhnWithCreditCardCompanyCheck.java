package com.ace;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class LuhnWithCreditCardCompanyCheck {

    public static final String LUHN_CHECK_FAILED = "Luhn check failed";

    public static void main(String[] args) {
        System.out.println(isValidLuhn("4444444444444448"));
        System.out.println(isValidLuhn("5500005555555559"));
        System.out.println(isValidLuhn("371449635398431"));
        System.out.println(isValidLuhn("36438936438936"));
        System.out.println(isValidLuhn("3566003566003566"));
        System.out.println(isValidLuhn("6011016011016011"));
        System.out.println(generateCheckDigit("601101601101601"));
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

        /* a credit card’s number must have 13 to 16 digits
         * add an extra check to capture which credit card company the customer has
         * */
        return (newCardNumber.length() >= 13)
                && (newCardNumber.length() <= 16)
                && (checkDigit == digit.charAt(0))
                && (CreditCardCompany.checkCreditCardCompany(newCardNumber) != null);
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

enum CreditCardCompany {

    /*
    Visa : 13 or 16 digits, starting with 4.
    MasterCard : 16 digits, starting with 51 through 55.
    Discover : 16 digits, starting with 6011 or 65.
    American Express : 15 digits, starting with 34 or 37.
    Diners Club : 14 digits, starting with 300 through 305, 36, or 38.
    JCB : 15 digits, starting with 2131 or 1800, or 16 digits starting with 35.
    */

    VISA("^4[0-9]{12}(?:[0-9]{3})?$"),
    MASTERCARD("^5[1-5][0-9]{14}$"),
    AMEX("^3[47][0-9]{13}$"),
    DINERS("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"),
    DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$"),
    JCB("^(?:2131|1800|35\\d{3})\\d{11}$");

    private final String regex;

    CreditCardCompany(String regex) {
        this.regex = regex;
    }

    public static CreditCardCompany checkCreditCardCompany(String cardNumber) {
        for (CreditCardCompany creditCardCompany : CreditCardCompany.values()) {
            if (cardNumber.matches(creditCardCompany.getRegex())) {
                return creditCardCompany;
            }
        }
        return null;
    }

    public String getRegex() {
        return regex;
    }
}
