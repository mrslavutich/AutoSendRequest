<?xml version="1.0" encoding="UTF-8"?>
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
    <S:Header>
    </S:Header>
    <S:Body wsu:Id="body">
        <ns5:RequestGIAC xmlns:ns5="http://smev.gosuslugi.ru/rev111111" xmlns="http://tower.ru/mvd/clients/common/responseID" xmlns:ns10="http://tower.ru/mvd/clients/giac/request" xmlns:ns2="http://www.w3.org/2000/09/xmldsig#" xmlns:ns3="http://tower.ru/mvd/clients/common/requestID" xmlns:ns4="http://tower.ru/mvd/clients/giac/response" xmlns:ns6="http://rsoc.ru/services/pgu" xmlns:ns7="http://tower.ru/mvd/clients/epgu/response" xmlns:ns8="http://www.w3.org/2004/08/xop/include" xmlns:ns9="http://tower.ru/mvd/clients/epgu/request">
            <ns5:Message>
                <ns5:Sender>
                    <ns5:Code>${SenderCode}</ns5:Code>
                    <ns5:Name>${SenderName}</ns5:Name>
                </ns5:Sender>
                <ns5:Recipient>
                    <ns5:Code>MVDR01001</ns5:Code>
                    <ns5:Name>МВД России</ns5:Name>
                </ns5:Recipient>
                <ns5:Originator>
                    <ns5:Code>${SenderCode}</ns5:Code>
                    <ns5:Name>${SenderName}</ns5:Name>
                </ns5:Originator>
                <ns5:TypeCode>${TypeCode}</ns5:TypeCode>
                <ns5:Status>REQUEST</ns5:Status>
                <ns5:Date>${Date}</ns5:Date>
                <ns5:ExchangeType>${ExchangeType}</ns5:ExchangeType>
                <ns5:ServiceCode>${ServiceCode}</ns5:ServiceCode>
                <ns5:CaseNumber>${CaseNumber}</ns5:CaseNumber>
            <#if TestMsg="on">
                <ns5:TestMsg>${TestMеssage}</ns5:TestMsg>
            </#if>
            </ns5:Message>
            <ns5:MessageData>
                <ns5:AppData>
                    <ns10:Message>
                        <ns10:Header from_foiv_id="${SenderCode}" from_foiv_name="${SenderName}" from_system="СИР" from_system_id="3" msg_type="REQUEST" msg_vid="conviction_info" to_foiv_id="MVDR01001" to_foiv_name="МВД России" to_system="ИС ГИАЦ" to_system_id="6" version="1.1">
                            <ns10:InitialRegNumber regtime="${Date}">${CaseNumber}</ns10:InitialRegNumber>
                            <ns10:Service code="1" name="conviction_investigation">${typeRequest}</ns10:Service>
                            <ns10:Reason>${reason}</ns10:Reason>
                            <ns10:Originator code="${SenderCode}" fio="${originatorFio}  ${originatorTel}" name="${SenderName}" region="${originatorRegion}"/>
                        </ns10:Header>

                        <ns10:Document>
                            <ns10:PrivatePerson>
                                <ns10:FirstName>${FirstName}</ns10:FirstName>
                                <ns10:FathersName><#if FathersName?has_content>${FathersName}<#else>-</#if></ns10:FathersName>
                                <ns10:SecName>${SecName}</ns10:SecName>
                                <ns10:DateOfBirth>${DateOfBirth}</ns10:DateOfBirth>
                                <ns10:PlaceOfBirth code="${PlaceOfBirth_code}">${PlaceOfBirth}</ns10:PlaceOfBirth>
                                <ns10:Address>
                                    <ns10:Region>${addressRegion}</ns10:Region>
                                    <ns10:RegistrationPlace>${addressRegistrationPlace}</ns10:RegistrationPlace>
                                    <ns10:TypeRegistration>${addressTypeRegistration}</ns10:TypeRegistration>
                                </ns10:Address>
                            </ns10:PrivatePerson>
                        </ns10:Document>
                    </ns10:Message>
                </ns5:AppData>
            </ns5:MessageData>
        </ns5:RequestGIAC>
    </S:Body>
</S:Envelope>