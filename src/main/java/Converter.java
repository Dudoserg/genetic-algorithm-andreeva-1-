class Converter {
    // Helper function to xor
    // two characters
    private char xor_c(char a, char b) {
        return (a == b) ? '0' : '1';
    }

    // Helper function to flip the bit
    private char flip(char c) {
        return (c == '0') ? '1' : '0';
    }

    // function to convert binary
    // string to gray string
    public String binarytoGray(String binary) {
        String gray = "";

        // MSB of gray code is same
        // as binary code
        gray += binary.charAt(0);

        // Compute remaining bits, next bit is
        // computed by doing XOR of previous
        // and current in Binary
        for (int i = 1; i < binary.length(); i++) {

            // Concatenate XOR of previous bit
            // with current bit
            gray += xor_c(binary.charAt(i - 1),
                    binary.charAt(i));
        }

        return gray;
    }

    // function to convert gray code
    // string to binary string
    public String graytoBinary(String gray) {
        String binary = "";

        // MSB of binary code is same
        // as gray code
        binary += gray.charAt(0);

        // Compute remaining bits
        for (int i = 1; i < gray.length(); i++) {
            // If current bit is 0,
            // concatenate previous bit
            if (gray.charAt(i) == '0')
                binary += binary.charAt(i - 1);

                // Else, concatenate invert of
                // previous bit
            else
                binary += flip(binary.charAt(i - 1));
        }

        return binary;
    }

    public int binaryToInt(String binary) {
        return Integer.parseInt(binary, 2);
    }

    public static void main(String[] args) {
        Converter converter = new Converter();
        String str = "";
        int x = 3455;
        System.out.println("x to str = " + Integer.toBinaryString(x));
        str = "110111111111";
        System.out.println("if gray\t" + converter.binaryToInt(converter.graytoBinary(str)));
        System.out.println("if binary\t" + converter.binaryToInt(str));
        System.out.println("\n");
        str = "111101111111";
        System.out.println("if gray\t" + converter.binaryToInt(converter.graytoBinary(str)));
        System.out.println("if binary\t" + converter.binaryToInt(str));
    }
}