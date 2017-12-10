package com.core.jadlwork.utils;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

public class TestLog4J extends AppenderSkeleton {

	@Override
	protected void append(LoggingEvent arg0) {
		if (arg0.getLevel().equals(Level.ERROR)) {
			
			System.out.println("--------------------------------------start-----------------------------------------");
			
			System.out.println("------------检测到error信息，msg为："+arg0.getMessage());
			System.out.println("---------locationInfo--->"+arg0.getLocationInformation());
			System.out.println("----loggerName-->"+arg0.getLoggerName());
			System.out.println("-----threadName-->"+arg0.getThreadName());
			System.out.println("------throwableStrP--->"+arg0.getThrowableStrRep());
			
			ThrowableInformation throwableInformation = arg0.getThrowableInformation();
			System.out.println("---------throwableInformationgetClass----------->"+throwableInformation.getClass());
			String[] throwableStrRep = throwableInformation.getThrowableStrRep();
			if (throwableStrRep != null && throwableStrRep.length>0) {
				System.out.println("-----throwableStrRep===start------");
				for (String string : throwableStrRep) {
					System.out.println(string);
				}
				System.out.println("-----throwableStrRep===end------");
			}
			
			Throwable throwable = throwableInformation.getThrowable();
			Class errClass = throwable.getClass();
			System.out.println("errClass---------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+errClass);
			System.out.println("throwable.getCause()-------------->>>>>>>>>>>>>>>>>>>"+throwable.getCause());
			System.out.println("throwable.getMessage()-------------->>>>>>>>>>>>>>>>>>>"+throwable.getMessage());
			
			System.out.println("---------throwable.getLocalizedMessage---------->"+throwable.getLocalizedMessage());
			System.out.println("---------throwable.getCause---------->"+throwable.getCause());
			System.out.println("---------throwable.getStackTrace---------->"+throwable.getStackTrace());
			StackTraceElement[] stackTrace = throwable.getStackTrace();
			if (stackTrace != null && stackTrace.length>0) {
				System.out.println("-----------throwable.getStackTrace()====start----------------");
				for (StackTraceElement stackTraceElement : stackTrace) {
					System.out.println("-------stackTraceElement.getClassName--------->"+stackTraceElement.getClassName());
					System.out.println("-------stackTraceElement.getFileName--------->"+stackTraceElement.getFileName());
					System.out.println("-------stackTraceElement.getLineNumber--------->"+stackTraceElement.getLineNumber());
					System.out.println("-------stackTraceElement.getMethodName--------->"+stackTraceElement.getMethodName());
					System.out.println("----------------------------------------------------------------------------------------------------------------");
				}
				System.out.println("-----------throwable.getStackTrace()====end----------------");
			}
			
			System.out.println("-------------------------------end---------------------------------------");
		}
	}

	@Override
	public void close() {
		
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

}
