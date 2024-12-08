package spring.alotra.controllers;

import lombok.Data;

import java.util.Objects;

@Data
public class VnpayNotification {
    private String vnp_TxnRef;
    private String vnp_ResponseCode;
    private String vnp_SecureHash;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VnpayNotification that = (VnpayNotification) o;
        return Objects.equals(vnp_TxnRef, that.vnp_TxnRef) && Objects.equals(vnp_ResponseCode, that.vnp_ResponseCode) && Objects.equals(vnp_SecureHash, that.vnp_SecureHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vnp_TxnRef, vnp_ResponseCode, vnp_SecureHash);
    }

    @Override
    public String toString() {
        return "VnpayNotification{" +
                "vnp_TxnRef='" + vnp_TxnRef + '\'' +
                ", vnp_ResponseCode='" + vnp_ResponseCode + '\'' +
                ", vnp_SecureHash='" + vnp_SecureHash + '\'' +
                '}';
    }
}
