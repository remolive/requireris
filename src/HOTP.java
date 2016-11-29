import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
 * Created by remyo on 29/11/16.
 */
public class HOTP {
    private String secret;
    private long digit;

    public HOTP(String secret, long digit) {
        this.secret = secret;
        this.digit = digit;
    }

    private byte[] hmac_sha1(byte[] text) throws NoSuchAlgorithmException, InvalidKeyException, Base32String.DecodingException {
        byte[] keyBytes = Base32String.decode(this.secret);
        Mac hmacSha1;
        try {
            hmacSha1 = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException nsae) {
            hmacSha1 = Mac.getInstance("HMAC-SHA-1");
        }
        SecretKeySpec macKey =
                new SecretKeySpec(keyBytes, "RAW");
        hmacSha1.init(macKey);
        return hmacSha1.doFinal(text);
    }

    private String getResult(int binary) {
        int otp = binary % 1000000;
        String result = Integer.toString(otp);
        while (result.length() < 6) {
            result = "0" + result;
        }
        return result;
    }

    private int trunc(byte[] hash) {
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        return binary;
    }

    public String generateOTP() throws NoSuchAlgorithmException, InvalidKeyException, Base32String.DecodingException {
        byte[] text = new byte[8];
        for (int i = text.length - 1; i >= 0; i--) {
            text[i] = (byte) (this.digit & 0xff);
            this.digit >>= 8;
        }

        byte[] hash = hmac_sha1(text);
        int binary = trunc(hash);
        return getResult(binary);
    }
}