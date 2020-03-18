package com.mule.zip;


import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

public class FindFileInZipFile implements Callable{

@Override
public Object onCall(MuleEventContext eventContext) throws Exception
{
	MuleMessage message = eventContext.getMessage();
	String filepth = message.getProperty("sourceFilePath", PropertyScope.INVOCATION);
	String innerFileName = null;
	try
	{
		ZipFile sourceZipFile = new ZipFile(filepth);
		Enumeration e = sourceZipFile.entries();
		while(e.hasMoreElements())
		{
		ZipEntry entry = (ZipEntry)e.nextElement();
		innerFileName=entry.getName();
		System.out.println("Found " + entry.getName());
		break;
		}
		sourceZipFile.close();
	}
	catch(IOException ioe)
	{
		System.out.println("Error opening zip file" + ioe);
	}
	
	MuleMessage muleMessage = new DefaultMuleMessage(message, eventContext.getMuleContext());
	muleMessage.setEncoding("UTF-8");
	muleMessage.setProperty("innerFileName", org.apache.commons.io.FilenameUtils.getExtension(innerFileName), PropertyScope.INVOCATION);
	
	
	return muleMessage;
	}
}



