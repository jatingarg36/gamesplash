/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

/**
 *
 * @author jatin
 */
public class Encryption {
    private final String password;

    public Encryption() {
        this.password = null;
    }

    
    public Encryption(String password) {
        this.password = password;
    }
    
    public String performEncryption(String password,int key){
        String cipherPsd = "";
        int len = password.length();
        for(int i=0;i<password.length();i++){
            cipherPsd+=password.charAt((i+key)%len);
        }
        return cipherPsd;
    }
    
    public String performDecryption(String cipherText,int key){
        String psd="";
        int len = cipherText.length();
        for(int i=0;i<cipherText.length();i++){
            psd+=cipherText.charAt((len+i-key)%len);
        }
        return psd;
    }
}
