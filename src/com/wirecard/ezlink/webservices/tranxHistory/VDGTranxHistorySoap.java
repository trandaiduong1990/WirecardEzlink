package com.wirecard.ezlink.webservices.tranxHistory;




//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.5.2
//
// Created by Quasar Development at 30-03-2015
//
//---------------------------------------------------




import org.ksoap2.HeaderProperty;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.*;
import org.kxml2.kdom.Element;

import com.wirecard.ezlink.constants.StringConstants;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VDGTranxHistorySoap
{
    interface VDGIWcfMethod
    {
        VDGExtendedSoapSerializationEnvelope CreateSoapEnvelope() throws java.lang.Exception;

        java.lang.Object ProcessResult(VDGExtendedSoapSerializationEnvelope __envelope,java.lang.Object result) throws java.lang.Exception;
    }

    String url = StringConstants.WebserviceConnectionUrl.URL_HISTORY;

    int timeOut=60000;
    public List< HeaderProperty> httpHeaders;
    public boolean enableLogging;

    VDGIServiceEvents callback;
    public VDGTranxHistorySoap(){}

    public VDGTranxHistorySoap (VDGIServiceEvents callback)
    {
        this.callback = callback;
    }
    public VDGTranxHistorySoap(VDGIServiceEvents callback,String url)
    {
        this.callback = callback;
        this.url = url;
    }

    public VDGTranxHistorySoap(VDGIServiceEvents callback,String url,int timeOut)
    {
        this.callback = callback;
        this.url = url;
        this.timeOut=timeOut;
    }

    protected org.ksoap2.transport.Transport createTransport()
    {
        try
        {
            java.net.URI uri = new java.net.URI(url);
            if(uri.getScheme().equalsIgnoreCase("https"))
            {
                int port=uri.getPort()>0?uri.getPort():443;
                return new HttpsTransportSE(uri.getHost(), port, uri.getPath(), timeOut);
            }
            else
            {
                return new HttpTransportSE(url,timeOut);
            }

        }
        catch (java.net.URISyntaxException e)
        {
        }
        return null;
    }
    
    protected VDGExtendedSoapSerializationEnvelope createEnvelope()
    {
        VDGExtendedSoapSerializationEnvelope envelope= new VDGExtendedSoapSerializationEnvelope(VDGExtendedSoapSerializationEnvelope.VER11);
        return envelope;
    }
    
    protected java.util.List sendRequest(String methodName,VDGExtendedSoapSerializationEnvelope envelope,org.ksoap2.transport.Transport transport  )throws java.lang.Exception
    {
        return transport.call(methodName, envelope, httpHeaders);
    }

    java.lang.Object getResult(java.lang.Class destObj,java.lang.Object source,String resultName,VDGExtendedSoapSerializationEnvelope __envelope) throws java.lang.Exception
    {
        if(source instanceof SoapPrimitive)
        {
            SoapPrimitive soap =(SoapPrimitive)source;
            if(soap.getName().equals(resultName))
            {
                java.lang.Object instance=__envelope.get(source,destObj);
                return instance;
            }
        }
        else
        {
            SoapObject soap = (SoapObject)source;
            if (soap.hasProperty(resultName))
            {
                java.lang.Object j=soap.getProperty(resultName);
                if(j==null)
                {
                    return null;
                }
                java.lang.Object instance=__envelope.get(j,destObj);
                return instance;
            }
            else if( soap.getName().equals(resultName)) {
                java.lang.Object instance=__envelope.get(source,destObj);
                return instance;
            }
       }

       return null;
    }

        
    public VDGEZLING_WS_TRANX_RES_ENV GetTranxHistory(final VDGEZLING_WS_TRANX_REQ_HEADER EZLING_WS_TRANX_REQ_HEADER,final VDGEZLING_WS_TRANX_REQ_BODY EZLING_WS_TRANX_REQ_BODY ) throws java.lang.Exception
    {
        return (VDGEZLING_WS_TRANX_RES_ENV)execute(new VDGIWcfMethod()
        {
            @Override
            public VDGExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
              VDGExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                __envelope.addMapping("http://ezlinkwebservices.com/service/TranxHistory/request","EZLING_WS_TRANX_REQ_HEADER",new VDGEZLING_WS_TRANX_REQ_HEADER().getClass());
                __envelope.addMapping("http://ezlinkwebservices.com/service/TranxHistory/request","EZLING_WS_TRANX_REQ_BODY",new VDGEZLING_WS_TRANX_REQ_BODY().getClass());
                SoapObject __soapReq = new SoapObject("http://ezlinkwebservices.com/service/TranxHistory/request", "EZLING_WS_TRANX_REQ_ENV");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="http://ezlinkwebservices.com/service/TranxHistory/request";
                __info.name="EZLING_WS_TRANX_REQ_HEADER";
                __info.type=VDGEZLING_WS_TRANX_REQ_HEADER.class;
                __info.setValue(EZLING_WS_TRANX_REQ_HEADER);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://ezlinkwebservices.com/service/TranxHistory/request";
                __info.name="EZLING_WS_TRANX_REQ_BODY";
                __info.type=VDGEZLING_WS_TRANX_REQ_BODY.class;
                __info.setValue(EZLING_WS_TRANX_REQ_BODY);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public java.lang.Object ProcessResult(VDGExtendedSoapSerializationEnvelope __envelope,java.lang.Object __result)throws java.lang.Exception {
                return (VDGEZLING_WS_TRANX_RES_ENV)getResult(VDGEZLING_WS_TRANX_RES_ENV.class,__result,"EZLING_WS_TRANX_RES_ENV",__envelope);
            }
        },"");
    }
    
    public android.os.AsyncTask< Void, Void, VDGOperationResult< VDGEZLING_WS_TRANX_RES_ENV>> GetTranxHistoryAsync(final VDGEZLING_WS_TRANX_REQ_HEADER EZLING_WS_TRANX_REQ_HEADER,final VDGEZLING_WS_TRANX_REQ_BODY EZLING_WS_TRANX_REQ_BODY)
    {
        return executeAsync(new VDGFunctions.IFunc< VDGEZLING_WS_TRANX_RES_ENV>() {
            public VDGEZLING_WS_TRANX_RES_ENV Func() throws java.lang.Exception {
                return GetTranxHistory( EZLING_WS_TRANX_REQ_HEADER,EZLING_WS_TRANX_REQ_BODY);
            }
        });
    }

    
    protected java.lang.Object execute(VDGIWcfMethod wcfMethod,String methodName) throws java.lang.Exception
    {
        org.ksoap2.transport.Transport __httpTransport=createTransport();
        __httpTransport.debug=enableLogging;
        VDGExtendedSoapSerializationEnvelope __envelope=wcfMethod.CreateSoapEnvelope();
        try
        {
            sendRequest(methodName, __envelope, __httpTransport);
            
        }
        finally {
            if (__httpTransport.debug) {
                if (__httpTransport.requestDump != null) {
                    android.util.Log.i("requestDump",__httpTransport.requestDump);
                }
                if (__httpTransport.responseDump != null) {
                    android.util.Log.i("responseDump",__httpTransport.responseDump);
                }
            }
        }
        java.lang.Object __retObj = __envelope.bodyIn;
        if (__retObj instanceof org.ksoap2.SoapFault){
            org.ksoap2.SoapFault __fault = (org.ksoap2.SoapFault)__retObj;
            throw convertToException(__fault,__envelope);
        }else{
            return wcfMethod.ProcessResult(__envelope,__retObj);
        }
    }
    
    protected < T> android.os.AsyncTask< Void, Void, VDGOperationResult< T>>  executeAsync(final VDGFunctions.IFunc< T> func)
    {
        return new android.os.AsyncTask< Void, Void, VDGOperationResult< T>>()
        {
            @Override
            protected void onPreExecute() {
                callback.Starting();
            };
            @Override
            protected VDGOperationResult< T> doInBackground(Void... params) {
                VDGOperationResult< T> result = new VDGOperationResult< T>();
                try
                {
                    result.Result= func.Func();
                }
                catch(java.lang.Exception ex)
                {
                    ex.printStackTrace();
                    result.Exception=ex;
                }
                return result;
            }
            
            @Override
            protected void onPostExecute(VDGOperationResult< T> result)
            {
                callback.Completed(result);
            }
        }.execute();
    }
        
    java.lang.Exception convertToException(org.ksoap2.SoapFault fault,VDGExtendedSoapSerializationEnvelope envelope)
    {

        if(fault.detail!=null && fault.detail.getChildCount()>0)
        {
            Element detailsNode=(Element)fault.detail.getChild(0);
            try
            {
                SoapObject exceptionObject = null;
                exceptionObject=envelope.GetExceptionDetail(detailsNode,"http://ezlinkwebservices.com/service/TranxHistory","TranxHistoryFault");
                if (exceptionObject != null)
                {
                    return new VDGTranxHistoryFault(exceptionObject,envelope);
                }
            }
            catch (java.lang.Exception e)
            {
                e.printStackTrace();
            }
        }
        return new java.lang.Exception(fault.faultstring);
    }
}

