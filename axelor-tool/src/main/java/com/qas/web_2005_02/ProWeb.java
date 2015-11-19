/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2015 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.qas.web_2005_02;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2012-09-12T15:14:19.685+02:00
 * Generated source version: 2.6.2
 * 
 */
@WebServiceClient(name = "ProWeb", 
                  wsdlLocation = "http://ip.axelor.com:2021/proweb.wsdl",
                  targetNamespace = "http://www.qas.com/web-2005-02") 
public class ProWeb extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.qas.com/web-2005-02", "ProWeb");
    public final static QName QAPortType = new QName("http://www.qas.com/web-2005-02", "QAPortType");
    static {
        URL url = null;
        try {
            url = new URL("http://ip.axelor.com:2021/proweb.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(ProWeb.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://ip.axelor.com:2021/proweb.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public ProWeb(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ProWeb(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ProWeb() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns QAPortType
     */
    @WebEndpoint(name = "QAPortType")
    public QAPortType getQAPortType() {
        return super.getPort(QAPortType, QAPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns QAPortType
     */
    @WebEndpoint(name = "QAPortType")
    public QAPortType getQAPortType(WebServiceFeature... features) {
        return super.getPort(QAPortType, QAPortType.class, features);
    }

}
