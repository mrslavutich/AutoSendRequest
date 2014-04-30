<?xml version="1.0" encoding="UTF-8"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
                   xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
    <SOAP-ENV:Header>
    </SOAP-ENV:Header>
    <SOAP-ENV:Body wsu:Id="body">
        <SendShortFLRequest xmlns="http://ws.unisoft/">
            <smev:Message xmlns:smev="http://smev.gosuslugi.ru/rev111111">
                <smev:Sender>
                    <smev:Code>${SenderCode}</smev:Code>
                    <smev:Name>${SenderName}</smev:Name>
                </smev:Sender>
                <smev:Recipient>
                    <smev:Code>FNS001001</smev:Code>
                    <smev:Name>ФНС России</smev:Name>
                </smev:Recipient>
                <smev:Originator>
                    <smev:Code>${SenderCode}</smev:Code>
                    <smev:Name>${SenderName}</smev:Name>
                </smev:Originator>
                <smev:TypeCode>${TypeCode}</smev:TypeCode>
                <smev:Status>${StatusRequest}</smev:Status>
                <smev:Date>${Date}</smev:Date>
                <smev:ExchangeType>${ExchangeType}</smev:ExchangeType>
            <#if TestMsg="on">
                <smev:TestMsg/>
            </#if>
            </smev:Message>
            <smev:MessageData xmlns:smev="http://smev.gosuslugi.ru/rev111111">
                <smev:AppData wsu:Id="fns-AppData">
                    <Документ xmlns="http://ws.unisoft/EGRNXX/ShortFLReq" ВерсФорм="4.02" ИдДок="${idDoc}">
                    <#if isInn?? && isInn!='on'>
                        <#if ogrn?has_content>
                                <ЗапросИП ИдЗапрос="${idDoc}">
                                    <ОГРНИП>${ogrn}</ОГРНИП>
                                </ЗапросИП>
                        </#if>
                    <#else>
                        <#if  inn?? && inn?has_content>
                                <ЗапросИП ИдЗапрос="${idDoc}">
                                    <ИНН>${inn}</ИНН>
                                </ЗапросИП>
                        </#if>
                    </#if>
                    </Документ>
                </smev:AppData>
            </smev:MessageData>
        </SendShortFLRequest>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>