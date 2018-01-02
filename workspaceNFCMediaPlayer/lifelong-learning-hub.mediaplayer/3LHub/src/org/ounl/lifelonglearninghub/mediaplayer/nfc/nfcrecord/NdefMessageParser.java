 /*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.ounl.lifelonglearninghub.mediaplayer.nfc.nfcrecord;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.wearable.NodeApi.GetConnectedNodesResult;
import com.google.common.primitives.Bytes;

import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.util.Log;

// Interesting tutorial
// http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278


/**
 * Utility class for creating {@link ParsedNdefMessage}s.
 */
public class NdefMessageParser {
	
	private static final String CLASSNAME = "NdefMessageParser";

    // Utility class
    private NdefMessageParser() {

    }


    /**
     * Note that only the first NdefRecord 
     * from the first NdefMessage will be parsed
     * The rest of the messages are discarded
     * 
     * @param message
     * @return
     */
    public static IParsedNdefCommand parseFirstNdefMessage(NdefMessage message) {

    	Log.d(CLASSNAME, "This NdefMessage contains "+message.getRecords().length + " NdefRecords. Only the first one will be used.");


    	
    	int i = 0;
    	while (i < message.getRecords().length) {
			
	    	NdefRecord el = message.getRecords()[i];
	    	if(el.getTnf()==NdefRecord.TNF_MIME_MEDIA){
	    		return buildCommand(el);
	    	}else{
	    		Log.e(CLASSNAME, "Not compatible tag :"+el.getTnf());
	    	}
	    	i++;    	
    	}
    	
    	return null;
    	

    	
    }
    
    /** Parse an NdefMessage */
    public static List<IParsedNdefCommand> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }    

    public static List<IParsedNdefCommand> getRecords(NdefRecord[] records) {
        List<IParsedNdefCommand> elements = new ArrayList<IParsedNdefCommand>();
        for (final NdefRecord record : records) {
        	
       	

            	Log.e("NdefMessageParser", "Not compatible tag. Armandola parda "+record.getType().toString());


        }
        return elements;
    }
    
    
    public static void printNdefMessage(NdefMessage message) {

    	Log.d(CLASSNAME, "  NdefMessage contains "+message.getRecords().length + " NdefRecords.");

    	int i = 0;
    	while (i < message.getRecords().length) {
			
	    	NdefRecord el = message.getRecords()[i];
	    	
	    	

	        String sId = new String(el.getId(), Charset.forName("UTF-8"));
	        String sPayload = new String(el.getPayload(), Charset.forName("UTF-8"));
	            

	        Log.d(CLASSNAME, "   ["+i+"] RecordType:"+el.getTnf()+ " | Id:"+sId+ " | PayLoad:"+sPayload);


	    	i++;    	
    	}    	
    	
    }
    
    public static void printNdefMessages(NdefMessage[] messages) {

    	Log.d(CLASSNAME, "NdefMessage[] contains "+messages.length + " NdefMessages.");

    	for (int i = 0; i < messages.length; i++) {
    		Log.d(CLASSNAME, "  NdefMessage["+i+"] ");
			printNdefMessage(messages[i]);
		}
   	
    }
    
	/**
	 * Setting a name with Trigger Tasker should have the following payload
	 * 
	 * enZ:1:3lhub#COLORINHEX#COLORNAME;a:http&#58/www.a.es:0
	 * 
	 * Text within separator # will be taken as id 
	 * 
	 * COLORINHEX
	 * 
	 */
	private static IParsedNdefCommand buildCommand(NdefRecord record) {


		String sPayload = new String(record.getPayload(),
				Charset.forName("UTF-8"));
		int ini = sPayload.indexOf("#") + 1;
		int mid = sPayload.indexOf("#", ini);
		//int end = sPayload.indexOf(";", mid);
		String sColorInHex = sPayload.substring(ini, mid);
		String sCommand = sPayload.substring(mid+1, sPayload.length());
		
		Log.d(CLASSNAME, "Reading NdefRecord. Color:"+ sColorInHex + " Command:"+sCommand);
		
		if(sCommand.contains(IParsedNdefCommand.COMMAND_FORWARD)){
			return new ForwardCommand(sColorInHex);
		}else if(sCommand.contains(IParsedNdefCommand.COMMAND_PLAY)){
			return new PlayCommand(sColorInHex);
		}else if(sCommand.contains(IParsedNdefCommand.COMMAND_PAUSE)){
			return new PauseCommand(sColorInHex);
		}else if(sCommand.contains(IParsedNdefCommand.COMMAND_STOP)){			
			return new StopCommand(sColorInHex);
		}else if(sCommand.contains(IParsedNdefCommand.COMMAND_CAST)){			
			return new CastCommand(sColorInHex);			
		}else{
			Log.e(CLASSNAME, "Unknown tag. Color:"+ sColorInHex + " Command:"+sCommand);
			return null;
		}
		
	}    
    
    
}