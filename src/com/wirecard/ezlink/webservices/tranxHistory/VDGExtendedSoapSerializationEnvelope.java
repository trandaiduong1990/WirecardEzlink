package com.wirecard.ezlink.webservices.tranxHistory;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.5.2
//
// Created by Quasar Development at 30-03-2015
//
//---------------------------------------------------



import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.*;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;
import java.io.StringReader;
import java.io.StringWriter;

public class VDGExtendedSoapSerializationEnvelope extends SoapSerializationEnvelope {
    static HashMap< java.lang.String,java.lang.Class> classNames = new HashMap< java.lang.String, java.lang.Class>();
    static {
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/request^^EZLING_WS_TRANX_REQ_HEADER",VDGEZLING_WS_TRANX_REQ_HEADER.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/request^^EZLING_WS_TRANX_REQ_BODY",VDGEZLING_WS_TRANX_REQ_BODY.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/request^^TranxHistoryReq",VDGTranxHistoryReq.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/response^^EZLING_WS_TRANX_RES_ENV",VDGEZLING_WS_TRANX_RES_ENV.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/response^^EZLING_WS_TRANX_RES_HEADER",VDGEZLING_WS_TRANX_RES_HEADER.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/response^^EZLING_WS__TRANX_RES_BODY",VDGEZLING_WS__TRANX_RES_BODY.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/response^^TranxHistoryResError",VDGTranxHistoryResError.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/response^^TranxHistoryDetail",VDGTranxHistoryDetail.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory^^TranxHistoryFault",VDGTranxHistoryFault.class);
        classNames.put("http://ezlinkwebservices.com/service/TranxHistory/response^^TranxList",VDGTranxList.class);
    }   

    protected static final int QNAME_NAMESPACE = 0;
    private static final String TYPE_LABEL = "type";

    public VDGExtendedSoapSerializationEnvelope() {
        this(SoapEnvelope.VER11);
    }

    public VDGExtendedSoapSerializationEnvelope(int soapVersion) {
        super(soapVersion);
        implicitTypes = true;
        setAddAdornments(false);
        new VDGMarshalGuid().register(this);
        new VDGMarshalDate().register(this);
        new MarshalFloat().register(this);
    }

    

    @Override
    protected void writeProperty(XmlSerializer writer, java.lang.Object obj, PropertyInfo type) throws IOException {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if (obj == null || obj== SoapPrimitive.NullNilElement) {
            writer.attribute(xsi, version >= VER12 ? NIL_LABEL : NULL_LABEL, "true");
            return;
        }
        java.lang.Object[] qName = getInfo(null, obj);
        if (!type.multiRef && qName[2] == null )
        {
            
            if (!implicitTypes || (obj.getClass() != type.type && !(obj instanceof Vector ) && type.type!=String.class)) {
                java.lang.String xmlName=VDGHelper.getKeyByValue(classNames,obj.getClass());
                if(xmlName!=null) {
                    java.lang.String[] parts = xmlName.split("\\^\\^");
                    java.lang.String prefix = writer.getPrefix(parts[0], true);
                    writer.attribute(xsi, TYPE_LABEL, prefix + ":" + parts[1]);
                }
                else
                {
                    if(type.type instanceof String) {
                        java.lang.String xsdPrefix = writer.getPrefix(xsd, true);
                        java.lang.String myType = (java.lang.String) type.type;
                        java.lang.String[] parts = myType.split("\\^\\^");
                        if (parts.length == 2) {
                            xsdPrefix = writer.getPrefix(parts[0], true);
                            myType = parts[1];
                        }

                        writer.attribute(xsi, TYPE_LABEL, xsdPrefix + ":" + myType);
                    }
                    else
                    {
                        java.lang.String prefix = writer.getPrefix(type.namespace, true);
                        writer.attribute(xsi, TYPE_LABEL, prefix + ":" + obj.getClass().getSimpleName());
                    }

                }
            }
            //super.writeProperty(writer,obj,type);

            //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
            //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
            writeElement(writer, obj, type, qName[QNAME_MARSHAL]);
        }
        else {
            super.writeProperty(writer, obj, type);
        }
    }

    public SoapObject GetExceptionDetail(Element detailElement,java.lang.String exceptionElementNS,java.lang.String exceptionElementName)
    {
        int index=detailElement.indexOf(exceptionElementNS,exceptionElementName,0);
        if(index>-1)
        {
            Element errorElement=detailElement.getElement(index);
            return GetSoapObject(errorElement);
        }
        return null;
    }

    public SoapObject GetSoapObject(Element detailElement) {
        try{
            XmlSerializer xmlSerializer = XmlPullParserFactory.newInstance().newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            detailElement.write(xmlSerializer);
            xmlSerializer.flush();

            XmlPullParser xpp = new KXmlParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

            xpp.setInput(new StringReader(writer.toString()));
            xpp.nextTag();
            SoapObject soapObj = new SoapObject(detailElement.getNamespace(),detailElement.getName());
            readSerializable(xpp,soapObj);
            return soapObj;
        }
        catch (java.lang.Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public java.lang.Object GetHeader(Element detailElement) {
        if(detailElement.getText(0)!=null)
        {
            SoapPrimitive primitive = new SoapPrimitive(detailElement.getNamespace(),detailElement.getName(),detailElement.getText(0));
            return  primitive;
        }
    
        return GetSoapObject(detailElement);
    }
    
    public java.lang.Object get(java.lang.Object soap,java.lang.Class cl)
    {
        if(soap==null)
        {
            return null;
        }
        try
        {
            if(soap instanceof  Vector)
            {
                Constructor ctor = cl.getConstructor(java.lang.Object.class,VDGExtendedSoapSerializationEnvelope.class);
                return ctor.newInstance(soap,this);
            }
            {
                if(soap instanceof SoapObject)
                {
                    java.lang.String key=String.format("%s^^%s",((SoapObject)soap).getNamespace(),((SoapObject)soap).getName());
                    if(classNames.containsKey(key))
                    {
                        cl=classNames.get(key);
                    }
                }
                Constructor ctor = cl.getConstructor(java.lang.Object.class,VDGExtendedSoapSerializationEnvelope.class);
                return ctor.newInstance(soap,this);
            }
        }
        catch (java.lang.Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

} 

