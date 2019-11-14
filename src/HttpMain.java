
public class HttpMain {

    public static void main(String[] args) {
        HttpRequest get = new HttpRequest();
        String result = get.newConnection("http://cswebcat.swan.ac.uk/puzzle");
        System.out.println(result);
        PuzzleSolver solve = new PuzzleSolver();
        String solvedCipher = solve.solved(result);
        System.out.println("output:" + solvedCipher);
        String cipherURL = "http://cswebcat.swan.ac.uk/message?solution=" + solvedCipher;
        System.out.println(cipherURL);
        System.out.println("Connection result with solved solution:");
        result = get.newConnection(cipherURL);
        System.out.println(result);
    }
}
