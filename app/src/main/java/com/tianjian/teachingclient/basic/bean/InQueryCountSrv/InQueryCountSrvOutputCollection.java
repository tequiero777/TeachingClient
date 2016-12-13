
package com.tianjian.teachingclient.basic.bean.InQueryCountSrv;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.tianjian.teachingclient.basic.bean.InLoginSrv.InLoginSrvOutputItem;


/**
 * <p>InQueryCountSrvOutputCollection complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="InQueryCountSrvOutputCollection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InQueryCountSrvOutputItem" type="{http://tj.teach.com/InQueryCountSrv}InQueryCountSrvOutputItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class InQueryCountSrvOutputCollection implements KvmSerializable{

    protected List<InQueryCountSrvOutputItem> inQueryCountSrvOutputItem;

    /**
     * Gets the value of the inQueryCountSrvOutputItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inQueryCountSrvOutputItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInQueryCountSrvOutputItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InQueryCountSrvOutputItem }
     * 
     * 
     */
    public List<InQueryCountSrvOutputItem> getInQueryCountSrvOutputItem() {
        if (inQueryCountSrvOutputItem == null) {
            inQueryCountSrvOutputItem = new ArrayList<InQueryCountSrvOutputItem>();
        }
        return this.inQueryCountSrvOutputItem;
    }

    @Override
   	public Object getProperty(int arg0) {
   		switch (arg0) {
   		case 0:
   			return inQueryCountSrvOutputItem;

   		default:
   			break;
   		}
   		return null;
   	}

   	@Override
   	public int getPropertyCount() {
   		return 1;
   	}

   	@Override
   	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
   		switch (arg0) {
   		case 0:
   			arg2.name ="InQueryCountSrvOutputItem";arg2.type = getInQueryCountSrvOutputItem().getClass();
   			break;

   		default:
   			break;
   		}
   		
   	}

   	@Override
   	public void setProperty(int arg0, Object arg1) {
   		switch (arg0) {
   		case 0:
   			SoapObject soapObject = (SoapObject) arg1;
   			inQueryCountSrvOutputItem = getInQueryCountSrvOutputItem();
   			InQueryCountSrvOutputItem item = new InQueryCountSrvOutputItem(); 
   			for(int i =0 ;i< soapObject.getPropertyCount();i++){
   				item.setProperty(i, soapObject.getProperty(i));
   			}
   			inQueryCountSrvOutputItem.add(item);
   			break;

   		default:
   			break;
   		}
   	}
}
