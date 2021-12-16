package Exp301;

public class CRC {
    private String polynomial;
    private int k;
    void setPolynomial(String polynomial) {
        this.polynomial = polynomial.replaceAll("^(0+)", ""); // remove leading zeros
        this.k = polynomial.length();
    }
    String getPolynomial() {
        return this.polynomial;
    }
    String generate(String data) {
        String tmp = data + "0".repeat(k);
        StringBuilder ans = new StringBuilder();
        for(int i = 0; i < data.length(); i++) {

        }

    }
}
