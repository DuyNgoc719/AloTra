package spring.alotra.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class VnpayService {

    public String generateVnPayUrl(double amount, String txnRef) {
        RestTemplate restTemplate = new RestTemplate();
        String nodeJsUrl = "http://localhost:3001/generate-vnpay-url";

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("amount", amount);
        requestData.put("txnRef", txnRef);

        // Gửi yêu cầu POST tới Node.js
        Map<String, String> response = restTemplate.postForObject(nodeJsUrl, requestData, Map.class);

        // Nhận URL trả về
        return response != null ? response.get("paymentUrl") : null;
    }
}
