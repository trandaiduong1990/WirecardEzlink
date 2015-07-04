package com.wirecard.ezlink.webservices.tranxHistory;

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

public class HTIRecieptResError extends AttributeContainer implements KvmSerializable
{
    
    public String REERRORCODE;
    
    public String REERRORDESC;

    public HTIRecieptResError ()
    {
    }

    public HTIRecieptResError (java.lang.Object paramObj,HTIExtendedSoapSerializationEnvelope __envelope)
    {
	    
	    if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;


        SoapObject soapObject=(SoapObject)inObj;  
        if (soapObject.hasProperty("REERRORCODE"))
        {	
	        java.lang.Object obj = soapObject.getProperty("REERRORCODE");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.REERRORCODE = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.REERRORCODE = (String)obj;
            }    
        }
        if (soapObject.hasProperty("REERRORDESC"))
        {	
	        java.lang.Object obj = soapObject.getProperty("REERRORDESC");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class))
            {
                SoapPrimitive j =(SoapPrimitive) obj;
                if(j.toString()!=null)
                {
                    this.REERRORDESC = j.toString();
                }
	        }
	        else if (obj!= null && obj instanceof String){
                this.REERRORDESC = (String)obj;
            }    
        }


    }

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return REERRORCODE;
        }
        if(propertyIndex==1)
        {
            return REERRORDESC;
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
            info.name = "REERRORCODE";
            info.namespace= "http://ezlinkwebservices.com/service/Reciept/response";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "REERRORDESC";
            info.namespace= "http://ezlinkwebservices.com/service/Reciept/response";
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
