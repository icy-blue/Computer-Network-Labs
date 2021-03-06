package exp301;

public class CRC {
    private String polynomial;
    private int k;

    public void setPolynomial(String polynomial) {
        if (polynomial == null) {
            this.polynomial = null;
            this.k = -1;
        } else {
            this.polynomial = polynomial.replaceAll("^(0+)", ""); // remove leading zeros
            this.k = polynomial.length() - 1;
        }
    }

    public String getPolynomial() {
        return this.polynomial;
    }

    public String generate(String data) {
        if (polynomial == null) return null;
        StringBuilder tmp = new StringBuilder(data + "0".repeat(k));
        StringBuilder ans = new StringBuilder(data);
        for (int i = 0; i < data.length(); i++) {
            if (tmp.charAt(i) == '1') {
                for (int j = 0; j <= k; j++) {
                    if (polynomial.charAt(j) == '1')
                        tmp.setCharAt(i + j, (char) (tmp.charAt(i + j) ^ 1));
                }
            }
        }
        ans.append(tmp.substring(data.length()));
        return ans.toString();
    }

    public boolean check(String data) {
        if (polynomial == null) return false;
        if (data.length() <= k) return false;
        StringBuilder tmp = new StringBuilder(data);
        for (int i = 0; i < data.length() - k; i++) {
            if (tmp.charAt(i) == '1') {
                for (int j = 0; j <= k; j++) {
                    if (polynomial.charAt(j) == '1')
                        tmp.setCharAt(i + j, (char) (tmp.charAt(i + j) ^ 1));
                }
            }
        }
        String str = tmp.substring(data.length() - k + 1);
        for (char x : str.toCharArray()) {
            if (x != '0') return false;
        }
        return true;
    }

    public CRC(String polynomial) {
        setPolynomial(polynomial);
    }
}
