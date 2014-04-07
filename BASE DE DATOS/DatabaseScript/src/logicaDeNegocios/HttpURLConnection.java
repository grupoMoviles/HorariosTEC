/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logicaDeNegocios;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author BRUNO
 */
public class HttpURLConnection {
    
        private final String USER_AGENT = "Mozilla/5.0";
        private final String url ;
        
        public HttpURLConnection(String pUrl)
        {
            url = pUrl;
        }

        // CREATE URL WITH THE PARAMETERS
        public String encodeURL(ArrayList<String> parameters, ArrayList<String> values)
        {
            String finalUrl = "";
            String auxiliar = "";
            
            if(parameters.size() == values.size())
            {
                int lastOne = parameters.size()-1;
                for(int param = 0 ; param < (lastOne+1) ; param++)
                {
                    auxiliar = parameters.get(param) + "=" + values.get(param);
                    if(param != lastOne)
                        auxiliar += "&";
                    finalUrl += auxiliar;
                }
                        
                return finalUrl;
            }
            else
                return "";
        }
        
	// HTTP POST request
	public String sendPost(ArrayList<String> parameters, ArrayList<String> values) throws Exception {
 
		//String url = "http://www.itcr.ac.cr/MatriculaN/ghor/HorDspyIndiv.asp?Filtro=DEPARTAMENTO";
		URL obj = new URL(url);
		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
                //create the url with the parameters
		String urlParameters = encodeURL(parameters,values);
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
                        response.append("\n");
		}
		in.close();
 
                
                return(response.toString());

	}
        
       
}