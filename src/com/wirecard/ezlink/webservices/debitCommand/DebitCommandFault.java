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

public class DebitCommandFault extends java.lang.Exception implements KvmSerializable
{
    
    public String faultInfo;
    
    public String message;

    public DebitCommandFault ()
    {
    }

    public DebitCommandFault (java.lang.Object paramObj,ExtendedSoapSerializationEnvelope __envelope)
    {
	    
	    if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;


        SoapObject soapObject=(SoapObject)inObj;  
        if (soapObject.hasProperty("faultInfo"))
        {	
	        java.lang.Object obj = soapObject.getProperty("faultInfo");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.faultInfo = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.faultInfo = (String)obj;
            }    
        }
        if (soapObject.hasProperty("message"))
        {	
	        java.lang.Object obj = soapObject.getProperty("message");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.message = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.message = (String)obj;
            }    
        }


    }

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return faultInfo;
        }
        if(propertyIndex==1)
        {
            return message;
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
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "faultInfo";
            info.namespace= "http://ezlinkwebservices.com/service/DebitCommand";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "message";
            info.namespace= "http://ezlinkwebservices.com/service/DebitCommand";
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
