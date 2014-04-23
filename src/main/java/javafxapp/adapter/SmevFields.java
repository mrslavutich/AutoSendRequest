package javafxapp.adapter;

import java.text.SimpleDateFormat;
import java.util.UUID;


public class SmevFields  {

    public String ServiceCode;

    public String SenderCode;

    public String SenderName;

    public String RecipientCode;

    public String RecipientName;

    public String OriginatorCode;

    public String OriginatorName;

    public String TypeCode;

    public String StatusRequest;

    public String Date;

    public String ExchangeType;

    public String TestMsg;

    public String TestMessage;

    public String Status;
    public String CaseNumber;

    public String BinaryData;
    public String RequestCode;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSenderCode() {
        return SenderCode;
    }

    public void setSenderCode(String senderCode) {
        SenderCode = "KSIR01001";
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String SenderName) {
        this.SenderName = "Тестовый СИР";
    }

    public String getRecipientCode() {
        return RecipientCode;
    }

    public void setRecipientCode(String recipientCode) {
        RecipientCode = "RecipientCode";
    }

    public String getRecipientName() {
        return RecipientName;
    }

    public void setRecipientName(String recipientName) {
        RecipientName = "RecipientName";
    }

    public String getOriginatorCode() {
        return OriginatorCode;
    }

    public void setOriginatorCode(String originatorCode) {
        OriginatorCode = "KSIR01001";
    }

    public String getOriginatorName() {
        return OriginatorName;
    }

    public void setOriginatorName(String originatorName) {
        OriginatorName = "Тестовый СИР";
    }

    public String getTypeCode() {
        return TypeCode;
    }

    public void setTypeCode(String typeCode) {
        TypeCode = "GFNC";
    }

    public String getStatusRequest() {
        return StatusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        StatusRequest = "REQUEST";
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        Date = simpleDateFormat.format(new java.util.Date());
    }

    public String getExchangeType() {
        return ExchangeType;
    }

    public void setExchangeType(String exchangeType) {
        ExchangeType = "2";
    }

    public String getTestMsg() {
        return TestMsg;
    }

    public void setTestMsg(String testMsg) {
        TestMsg = testMsg;
    }

    public String getTestMessage() {
        return TestMessage;
    }

    public void setTestMessage(String testMessage) {
        TestMessage = testMessage;
    }

    public void setServiceCode(String serviceCode) {
        this.ServiceCode = serviceCode;
    }

    public String getServiceCode() {
        return ServiceCode;
    }

    public String getCaseNumber() {
        return CaseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        CaseNumber = UUID.randomUUID().toString();
    }

    public String getBinaryData() {
        return BinaryData;
    }

    public void setBinaryData(String binaryData) {
        BinaryData = binaryData;
    }

    public String getRequestCode() {
        return RequestCode;
    }

    public void setRequestCode(String requestCode) {
        RequestCode = requestCode;
    }
}
