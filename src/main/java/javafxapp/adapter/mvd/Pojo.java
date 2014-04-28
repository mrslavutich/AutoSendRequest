package javafxapp.adapter.mvd;

import javafxapp.adapter.SmevFields;

public class Pojo extends SmevFields {

    public String typeRequest; //Сведения о наличии (отсутствии) судимости и (или) факта уголовного преследования либо о прекращении уголовного преследования, сведения о нахождении в розыске
    public String reason; //12345678901

    //Сведения об инициаторе проверки
    public String originatorFio; //Ковалевская И.А.
    public String originatorTel; //тел. (351) 232-3456
    public String originatorRegion; //074

    //Сведения о проверяемом лице
    public String FirstName;
    public String FathersName;
    public String SecName;
    public String DateOfBirth;
    public String SNILS;

    //Сведения о месте рождения проверяемого лица
    public String PlaceOfBirth_code;
    public String PlaceOfBirth;

    //Адресные сведения проверяемого лица
    public String addressRegion;
    public String addressTypeRegistration;
    public String addressRegistrationPlace;

    public int rowNum;



    public String getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOriginatorFio() {
        return originatorFio;
    }

    public void setOriginatorFio(String originatorFio) {
        this.originatorFio = originatorFio;
    }

    public String getOriginatorTel() {
        return originatorTel;
    }

    public void setOriginatorTel(String originatorTel) {
        this.originatorTel = originatorTel;
    }

    public String getOriginatorRegion() {
        return originatorRegion;
    }

    public void setOriginatorRegion(String originatorRegion) {
        this.originatorRegion = originatorRegion;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getFathersName() {
        return FathersName;
    }

    public void setFathersName(String fathersName) {
        FathersName = fathersName;
    }

    public String getSecName() {
        return SecName;
    }

    public void setSecName(String secName) {
        SecName = secName;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getSNILS() {
        return SNILS;
    }

    public void setSNILS(String SNILS) {
        this.SNILS = SNILS;
    }

    public String getPlaceOfBirth_code() {
        return PlaceOfBirth_code;
    }

    public void setPlaceOfBirth_code(String placeOfBirth_code) {
        PlaceOfBirth_code = placeOfBirth_code;
    }

    public String getPlaceOfBirth() {
        return PlaceOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        PlaceOfBirth = placeOfBirth;
    }

    public String getAddressRegion() {
        return addressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public String getAddressTypeRegistration() {
        return addressTypeRegistration;
    }

    public void setAddressTypeRegistration(String addressTypeRegistration) {
        this.addressTypeRegistration = addressTypeRegistration;
    }

    public String getAddressRegistrationPlace() {
        return addressRegistrationPlace;
    }

    public void setAddressRegistrationPlace(String addressRegistrationPlace) {
        this.addressRegistrationPlace = addressRegistrationPlace;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
}