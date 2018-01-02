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
package org.ounl.lifelonglearninghub.nfclearntracker.nfcrecord;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static IParsedNdefRecord parseFirstNdefMessage(NdefMessage message) {

    	Log.d(CLASSNAME, "This NdefMessage contains "+message.getRecords().length + " NdefRecords. Only the first one will be used.");


    	
    	int i = 0;
    	while (i < message.getRecords().length) {
			
	    	NdefRecord el = message.getRecords()[i];	    	
	    	switch (el.getTnf()) {	
				case NdefRecord.TNF_ABSOLUTE_URI:
					return UriRecord.parse(el);			
				case NdefRecord.TNF_WELL_KNOWN:
					return UriRecord.parse(el);
				case NdefRecord.TNF_MIME_MEDIA:
					return MimeMediaRecord.parse(el);
				default:
					Log.e(CLASSNAME, "Not compatible tag :"+el.getTnf());
			}
	    	i++;    	
    	}
    	
    	return null;
    	
//    	switch (element.getTnf()) {
//		case NdefRecord.TNF_MIME_MEDIA:
//			return MimeMediaRecord.parse(element);		
//		case NdefRecord.TNF_ABSOLUTE_URI:
//			return UriRecord.parse(element);				
//		case NdefRecord.TNF_WELL_KNOWN:
//			return UriRecord.parse(element);		
//		default:
//			Log.e("NdefMessageParser", "Not compatible tag :"+element.getTnf());
//			return null;
//		}    	
//    	
//    	if (element.getTnf() == NdefRecord.TNF_MIME_MEDIA){
//             return MimeMediaRecord.parse(element);        	
//        } else if (UriRecord.isUri(element)) {
//            return UriRecord.parse(element);
//        } else if (TextRecord.isText(element)) {
//            return TextRecord.parse(element);
//        } else if (SmartPoster.isPoster(element)) {
//             return SmartPoster.parse(element);
//        } else {        	
//        	Log.e("NdefMessageParser", "Not compatible tag");
//        }
//        return null;
    	
    }
    
    /** Parse an NdefMessage */
    public static List<IParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }    

    public static List<IParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<IParsedNdefRecord> elements = new ArrayList<IParsedNdefRecord>();
        for (final NdefRecord record : records) {
        	
       	
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
            	
            	Log.e("NdefMessageParser", "Not compatible tag");

            }
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
    
    
}