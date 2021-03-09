package securityLayer;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class JamesB_RSA {
    public static void main(String[] args) {

        // 1. Pick two prime numbers , I will pick 2 and 7 , lets call them p and q
        int p = 2;
        int q = 7;
        System.out.println("P = "+p +", Q = "+q);
        // 2. Multiply P and Q , and that becomes the modulus. n = 14
        int n = p * q;
        System.out.println("N = "+n);
        // 3. Make a list between 1 and 14 and remove the common factors:
        ArrayList<Integer> nonCommonFactors = new ArrayList<>();
        for(int i = 1; i <= n; i++){
            if(gcd(i, n) == 1){
                nonCommonFactors.add(i);
            }
        }
        int l = nonCommonFactors.size();
        System.out.println("L = "+l);
        System.out.println("NCF = "+nonCommonFactors);
        // 4. Now we get to pick the encryption key , in the example was (5,14) , we know 14 is the modulus.

        int e = 0; // Encryption Key
        for(int i = 2; i < l; i++){
            if(gcd(i,l) == 1 && gcd(i, n) == 1){
                e = i;
                break;
            }
        }

        // 5. The Decryption part
        int d = 0;
        for(int i = e; i < 10000; i++){
            if((i * e) % l == 1){
                d = i;
                break;
            }
        }


        // ENCRYPTION
        System.out.println("E = "+e);
        System.out.println("D = "+d);
        int plainText = 2000;
        System.out.println("PLAIN TEXT = "+plainText);
        double cipherText = Math.pow(plainText, e) % n;
        System.out.println("CIPHER TEXT = "+cipherText);
        double decryptedText = Math.pow(cipherText, d) % n;
        System.out.println("DECRYPTED TEXT = "+decryptedText);
    }

    // Recursive function to return gcd of a and b
    public static int gcd(int a, int b)
    {
        // Everything divides 0
        if (a == 0)
            return b;
        if (b == 0)
            return a;

        // base case
        if (a == b)
            return a;

        // a is greater
        if (a > b)
            return gcd(a-b, b);
        return gcd(a, b-a);
    }
}
