
package com.tianjian.teachingclient.basic.bean.InTeachManagerSrv;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.tianjian.teachingclient.basic.bean.InQueryTasksSrv.InQueryTaskSrvOutputItem;


/**
 * <p>InTeachManagerSrvOutputCollection complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="InTeachManagerSrvOutputCollection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InTeachManagerSrvOutputItem" type="{http://tj.teach.com/InTeachManagerSrv}InTeachManagerSrvOutputItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class InTeachManagerSrvOutputCollection  implements KvmSerializable{

    protected List<InTeachManagerSrvOutputItem> inTeachManagerSrvOutputItem;

    /**
     * Gets the value of the inTeachManagerSrvOutputItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inTeachManagerSrvOutputItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInTeachManagerSrvOutputItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InTeachManagerSrvOutputItem }
     * 
     * 
     */
    public List<InTeachManagerSrvOutputItem> getInTeachManagerSrvOutputItem() {
        if (inTeachManagerSrvOutputItem == null) {
            inTeachManagerSrvOutputItem = new ArrayList<InTeachManagerSrvOutputItem>();
        }
        return this.inTeachManagerSrvOutputItem;
    }

    @Override
   	public Object getProperty(int arg0) {
   		switch (arg0) {
   		case 0:
   			return inTeachManagerSrvOutputItem;

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
   			arg2.name ="InTeachManagerSrvOutputItem";arg2.type = getInTeachManagerSrvOutputItem().getClass();
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
   			inTeachManagerSrvOutputItem = getInTeachManagerSrvOutputItem();
   			InTeachManagerSrvOutputItem item = new InTeachManagerSrvOutputItem(); 
   			for(int i =0 ;i< soapObject.getPropertyCount();i++){
   				item.setProperty(i, soapObject.getProperty(i));
   			}
   			inTeachManagerSrvOutputItem.add(item);
   			break;

   		default:
   			break;
   		}
   	}
}
