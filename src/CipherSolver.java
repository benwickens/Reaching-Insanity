
public class CipherSolver {

    public String solved(String unsolved) {

        int x = 0;
        String solvedStr = "";
        int tempLength = unsolved.length();
        int temp = tempLength;
        while (x < tempLength) {
            char tempChar = unsolved.charAt(x);
            if (((x % 2) == 0) || (x == 0)) {
                char solvedChar = shiftChar(tempChar, 1);
                int ii =9;
                solvedStr = solvedStr.concat(java.lang.Character.toString(solvedChar));
                x += 1;
            } else if ((x % 2) > 0) {
                char solvedChar = shiftChar(tempChar, -1);
                solvedStr = solvedStr.concat(java.lang.Character.toString(solvedChar));
                x += 1;
            }
        }
        return solvedStr;
    }

    public char shiftChar(char in, int direction) {
        char shiftedChar = in;
        if ((in == 'A') && (direction == -1)) { //Handles two special cases of wrapping around the alphabet.
            return 'Z';
        } else if ((in == 'Z') && (direction == 1)) {
            return 'A';
        } else {
            int numOfShift = in + direction;
            char solvedChar = (char) numOfShift;
            return solvedChar;
        }
    }
}
