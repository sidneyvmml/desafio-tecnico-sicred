package com.desafio.sicred.votacao.adapters.output.associado.validation;

public final class CpfValidator {

    private CpfValidator() {}

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        String digits = cpf.replaceAll("\\D", "");

        if (digits.length() != 11) return false;

        // Reject sequences like 00000000000, 11111111111, etc.
        if (digits.chars().distinct().count() == 1) return false;

        return checkDigit(digits, 9) && checkDigit(digits, 10);
    }

    private static boolean checkDigit(String digits, int position) {
        int sum = 0;
        int weight = position + 1;
        for (int i = 0; i < position; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * weight--;
        }
        int remainder = (sum * 10) % 11;
        if (remainder == 10 || remainder == 11) remainder = 0;
        return remainder == Character.getNumericValue(digits.charAt(position));
    }
}
