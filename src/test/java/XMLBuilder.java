
import static com.ian.util.TimeStamp.getTimeStamp;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class XMLBuilder {

    public static void main(String[] args) throws Exception {
        createDataSyncRequest();
    }

    public static SOAPMessage createDataSyncRequest() throws Exception {

        String stamp = getTimeStamp();

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.removeNamespaceDeclaration("SOAP-ENV");
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.setPrefix("soapenv");
        //Soap Header
        SOAPHeader soapHeader = envelope.getHeader();
        soapHeader.setPrefix("soapenv");

        envelope.removeChild(soapHeader);
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.setPrefix("soapenv");
        SOAPElement ns1 = soapBody.addChildElement("syncOrderRelation", "ns1", "http://www.csapi.org/schema/parlayx/data/sync/v1_0/local");
        SOAPElement userID = ns1.addChildElement("userID", "ns1");

        SOAPElement ID = userID.addChildElement("ID");
        ID.addTextNode("27633678839");
        SOAPElement type = userID.addChildElement("type");
        type.addTextNode("0");

        SOAPElement spID = ns1.addChildElement("spID", "ns1");
        spID.addTextNode("270110000445");

        SOAPElement productID = ns1.addChildElement("productID", "ns1");
        productID.addTextNode("2701220000002522");

        SOAPElement serviceID = ns1.addChildElement("serviceID", "ns1");
        serviceID.addTextNode("27012000003919");

        SOAPElement serviceList = ns1.addChildElement("serviceList", "ns1");
        serviceList.addTextNode("0011002000001100");

        SOAPElement updateType = ns1.addChildElement("updateType", "ns1");
        updateType.addTextNode("1");

        SOAPElement updateTime = ns1.addChildElement("updateTime", "ns1");
        updateTime.addTextNode(stamp);

        SOAPElement updateDesc = ns1.addChildElement("updateDesc", "ns1");
        updateDesc.addTextNode("Addition");

        SOAPElement effectiveTime = ns1.addChildElement("effectiveTime", "ns1");
        effectiveTime.addTextNode(stamp);

        SOAPElement expiryTime = ns1.addChildElement("expiryTime", "ns1");
        expiryTime.addTextNode("20361231160000");

        SOAPElement extensionInfo = ns1.addChildElement("extensionInfo", "ns1");
        SOAPElement item1 = extensionInfo.addChildElement("item");
        SOAPElement key1 = item1.addChildElement("key");
        key1.addTextNode("accessCode");
        SOAPElement value1 = item1.addChildElement("value");
        value1.addTextNode("83930098400217");

        SOAPElement item2 = extensionInfo.addChildElement("item");
        SOAPElement key2 = item2.addChildElement("key");
        key2.addTextNode("operCode");
        SOAPElement value2 = item2.addChildElement("value");
        value2.addTextNode("zh_CN");

        SOAPElement item3 = extensionInfo.addChildElement("item");
        SOAPElement key3 = item3.addChildElement("key");
        key3.addTextNode("chargeMode");
        SOAPElement value3 = item3.addChildElement("value");
        value3.addTextNode("0");

        SOAPElement item4 = extensionInfo.addChildElement("item");
        SOAPElement key4 = item4.addChildElement("key");
        key4.addTextNode("MDSPSUBEXPMODE");
        SOAPElement value4 = item4.addChildElement("value");
        value4.addTextNode("1");

        SOAPElement item5 = extensionInfo.addChildElement("item");
        SOAPElement key5 = item5.addChildElement("key");
        key5.addTextNode("objectType");
        SOAPElement value5 = item5.addChildElement("value");
        value5.addTextNode("1");

        SOAPElement item6 = extensionInfo.addChildElement("item");
        SOAPElement key6 = item6.addChildElement("key");
        key6.addTextNode("Starttime");
        SOAPElement value6 = item6.addChildElement("value");
        value6.addTextNode("20130723082551");

        SOAPElement item7 = extensionInfo.addChildElement("item");
        SOAPElement key7 = item7.addChildElement("key");
        key7.addTextNode("isFreePeriod");
        SOAPElement value7 = item7.addChildElement("value");
        value7.addTextNode("false");

        SOAPElement item8 = extensionInfo.addChildElement("item");

        SOAPElement item8_1 = item8.addChildElement("item");
        SOAPElement key8_1 = item8_1.addChildElement("key");
        key8_1.addTextNode("operatorID");
        SOAPElement value8_1 = item8_1.addChildElement("value");
        value8_1.addTextNode("26001");

        SOAPElement item8_2 = item8.addChildElement("item");
        SOAPElement key8_2 = item8_2.addChildElement("key");
        key8_2.addTextNode("payType");
        SOAPElement value8_2 = item8_2.addChildElement("value");
        value8_2.addTextNode("0");

        SOAPElement item9 = extensionInfo.addChildElement("item");
        SOAPElement key9 = item9.addChildElement("key");
        key9.addTextNode("transactionID");
        SOAPElement value9 = item9.addChildElement("value");
        value9.addTextNode("504016000001307231624304170004");

        SOAPElement item10 = extensionInfo.addChildElement("item");
        SOAPElement key10 = item10.addChildElement("key");
        key10.addTextNode("orderKey");
        SOAPElement value10 = item10.addChildElement("value");
        value10.addTextNode("999000000000000194");

        SOAPElement item11 = extensionInfo.addChildElement("item");
        SOAPElement key11 = item11.addChildElement("key");
        key11.addTextNode("keyword");
        SOAPElement value11 = item11.addChildElement("value");
        value11.addTextNode("sub");

        SOAPElement item12 = extensionInfo.addChildElement("item");
        SOAPElement key12 = item12.addChildElement("key");
        key12.addTextNode("cycleEndTime");
        SOAPElement value12 = item12.addChildElement("value");
        value12.addTextNode("20170822160000");

        SOAPElement item13 = extensionInfo.addChildElement("item");
        SOAPElement key13 = item13.addChildElement("key");
        key13.addTextNode("durationOfGracePeriod");
        SOAPElement value13 = item13.addChildElement("value");
        value13.addTextNode("-1");

        SOAPElement item14 = extensionInfo.addChildElement("item");
        SOAPElement key14 = item14.addChildElement("key");
        key14.addTextNode("serviceAvailability");
        SOAPElement value14 = item14.addChildElement("value");
        value14.addTextNode("0");

        SOAPElement item15 = extensionInfo.addChildElement("item");
        SOAPElement key15 = item15.addChildElement("key");
        key15.addTextNode("durationOfSuspendPeriod");
        SOAPElement value15 = item15.addChildElement("value");
        value15.addTextNode("0");

        SOAPElement item16 = extensionInfo.addChildElement("item");
        SOAPElement key16 = item16.addChildElement("key");
        key16.addTextNode("fee");
        SOAPElement value16 = item16.addChildElement("value");
        value16.addTextNode("0");

        SOAPElement item17 = extensionInfo.addChildElement("item");
        SOAPElement key17 = item17.addChildElement("key");
        key17.addTextNode("servicePayType");
        SOAPElement value17 = item17.addChildElement("value");
        value17.addTextNode("0");

        SOAPElement item18 = extensionInfo.addChildElement("item");
        SOAPElement key18 = item18.addChildElement("key");
        key18.addTextNode("cycleEndTime");
        SOAPElement value18 = item18.addChildElement("value");
        value18.addTextNode("20130813021650");

        SOAPElement item19 = extensionInfo.addChildElement("item");
        SOAPElement key19 = item19.addChildElement("key");
        key19.addTextNode("channelID");
        SOAPElement value19 = item19.addChildElement("value");
        value19.addTextNode("1");

        SOAPElement item20 = extensionInfo.addChildElement("item");
        SOAPElement key20 = item20.addChildElement("key");
        key20.addTextNode("TraceUniqueID");
        SOAPElement value20 = item20.addChildElement("value");
        value20.addTextNode("504016000001307231624304170005");

        SOAPElement item21 = extensionInfo.addChildElement("item");
        SOAPElement key21 = item21.addChildElement("key");
        key21.addTextNode("rentSuccess");
        SOAPElement value21 = item21.addChildElement("value");
        value21.addTextNode("true");
        
        SOAPElement item22 = extensionInfo.addChildElement("item");
        SOAPElement key22 = item22.addChildElement("key");
        key22.addTextNode("try");
        SOAPElement value22 = item22.addChildElement("value");
        value22.addTextNode("false");
        
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }
}
