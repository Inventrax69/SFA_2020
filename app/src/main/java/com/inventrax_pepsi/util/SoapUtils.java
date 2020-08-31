package com.inventrax_pepsi.util;

import android.app.Activity;
import android.util.Log;

import com.inventrax_pepsi.common.constants.ServiceURLConstants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

/**
 * Created by Naresh on 28-Jan-16.
 */
public class SoapUtils {

    private static int TIME_OUT = 60 * 1000 ;



    public static SoapPrimitive callWebMethodAsString(List<PropertyInfo> soapProps,
                                                      String OPERATION_NAME, Activity activity) {

        SoapPrimitive resultObj = null;

        SoapObject request = new SoapObject(ServiceURLConstants.WSDL_TARGET_NAMESPACE,
                OPERATION_NAME);

        for (PropertyInfo pIf : soapProps) {

            request.addProperty(pIf.getName(), pIf.getValue());
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(ServiceURLConstants.SOAP_ADDRESS,SoapUtils.TIME_OUT);

        try {

            httpTransport.call(ServiceURLConstants.SOAP_ACTION_URL + OPERATION_NAME,
                    envelope);

            resultObj = (SoapPrimitive) envelope.getResponse();
            // resultObj = (Object)envelope.bodyIn;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return resultObj;
    }

    public static SoapPrimitive webMethodAsString(List<PropertyInfo> soapProps,
                                                  String REQUEST_URL, Activity activity) {

        SoapPrimitive resultObj = null;

        SoapObject request = new SoapObject(ServiceURLConstants.WSDL_TARGET_NAMESPACE,
                REQUEST_URL);

        for (PropertyInfo pIf : soapProps) {

            request.addProperty(pIf.getName(), pIf.getValue());
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);


        HttpTransportSE httpTransport = new HttpTransportSE(REQUEST_URL,SoapUtils.TIME_OUT);


        try {

            httpTransport.call(REQUEST_URL,
                    envelope);

            resultObj = (SoapPrimitive) envelope.getResponse();
            // resultObj = (Object)envelope.bodyIn;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return resultObj;
    }


    public static String getJSONResponse(List<PropertyInfo> soapProps,String methodName){

        SoapPrimitive resultObj = null;
        try
        {
            SoapObject request = new SoapObject(ServiceURLConstants.WSDL_TARGET_NAMESPACE, methodName);

            for (PropertyInfo pIf : soapProps) {
                request.addProperty(pIf.getName(), pIf.getValue());
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(ServiceURLConstants.SOAP_ADDRESS_ASMX,SoapUtils.TIME_OUT);


            httpTransport.call(ServiceURLConstants.SOAP_ACTION_URL + methodName,
                    envelope);

            resultObj = (SoapPrimitive) envelope.getResponse();

            if (resultObj!=null)
            {
                return resultObj.toString();

            }
            else
            {
                return null;
            }

        }catch (Exception ex){
            Log.d("EX",ex.toString());
            return  null;
        }
    }
}
