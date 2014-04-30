<?xml version="1.0" encoding="UTF-8"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
                   xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
    <SOAP-ENV:Header>
    </SOAP-ENV:Header>
    <SOAP-ENV:Body wsu:Id="body">
        <SendFullFLRequest xmlns="http://ws.unisoft/">
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
                <#if NomerDela?has_content>
                <Документ xmlns="http://ws.unisoft/EGRNXX/FullFLReq" ВерсФорм="4.02" ИдДок="${idDoc}"
                          НомерДела="${NomerDela}">
                <#else>
                <Документ xmlns="http://ws.unisoft/EGRNXX/FullFLReq" ВерсФорм="4.02" ИдДок="${idDoc}">
                </#if>

                <#if isInn?? && isInn!='on' && ogrn??>


                    <ЗапросИП>
                        <ОГРНИП>${ogrn}</ОГРНИП>
                    </ЗапросИП>


                <#else>

                    <ЗапросИП>
                        <ИНН><#if inn??>${inn}</#if></ИНН>
                    </ЗапросИП>
                </#if>

                </Документ>
                </smev:AppData>
            </smev:MessageData>
        </SendFullFLRequest>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>