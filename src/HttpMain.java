/**
 * File name: HttpRequest.java
 *
 * @version 1.0
 * Creation Date: 25/10/2019
 * Last Modification date: 17/11/2019
 * @author Ben Wickens
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * 
 * <br>
 * Version History
 * 1.0 - connects to a url passed into it. Very basic error catching.
 *
 */
public class HttpMain {

    public static void main(String[] args) {
        HttpRequest get = new HttpRequest();
        String result = get.newConnection("http://cswebcat.swan.ac.uk/puzzle");
        System.out.println(result);
        CipherSolver solve = new CipherSolver();
        String solvedCipher = solve.solved(result);
        System.out.println("output:" + solvedCipher);
        String cipherURL = "http://cswebcat.swan.ac.uk/message?solution=" + solvedCipher;
        System.out.println(cipherURL);
        System.out.println("Connection result with solved solution:");
        result = get.newConnection(cipherURL);
        System.out.println(result);
    }
}
