package com.wirecard.ezlink.webservices.debitCommand;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.5.2
//
// Created by Quasar Development at 30-03-2015
//
//---------------------------------------------------


import java.util.Hashtable;
import org.ksoap2.serialization.*;

public class EZLING_WS_RES_ENV extends AttributeContainer implements KvmSerializable
{
    
    public EZLING_WS_HEADER_1 EZLING_WS_HEADER;
    
    public EZLING_WS_RES_BODY EZLING_WS_RES_BODY;

    public EZLING_WS_RES_ENV ()
    {
    }

    public EZLING_WS_RES_ENV (java.lang.Object paramObj,ExtendedSoapSerializationEnvelope __envelope)
    {
	    
	    if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;


        SoapObject soapObject=(SoapObject)inObj;  
        if (soapObject.hasProperty("EZLING_WS_HEADER"))
        {	
	        java.lang.Object j = soapObject.getProperty("EZLING_WS_HEADER");
	        this.EZLING_WS_HEADER = (EZLING_WS_HEADER_1)__envelope.get(j,EZLING_WS_HEADER_1.class);
        }
        if (soapObject.hasProperty("EZLING_WS_RES_BODY"))
        {	
	        java.lang.Object j = soapObject.getProperty("EZLING_WS_RES_BODY");
	        this.EZLING_WS_RES_BODY = (EZLING_WS_RES_BODY)__envelope.get(j,EZLING_WS_RES_BODY.class);
        }


    }

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return EZLING_WS_HEADER;
        }
        if(propertyIndex==1)
        {
            return EZLING_WS_RES_BODY;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = EZLING_WS_HEADER_1.class;
            info.name = "EZLING_WS_HEADER";
            info.namespace= "http://ezlinkwebservices.com/service/DebitCommand/response";
        }
        if(propertyIndex==1)
        {
            info.type = EZLING_WS_RES_BODY.class;
            info.name = "EZLING_WS_RES_BODY";
            info.namespace= "http://ezlinkwebservices.com/service/DebitCommand/response";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }

    @Override
    public String getInnerText() {
        return null;
    }

    @Override
    public void setInnerText(String s) {

    }
}