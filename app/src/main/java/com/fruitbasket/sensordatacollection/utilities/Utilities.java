package com.fruitbasket.sensordatacollection.utilities;

public class Utilities {
	private static final String TAG="utilities.Utilities";
	
	private static Utilities mUtilities=new Utilities();
	
	private Utilities(){}
	
	public static Utilities getInstance(){
		return mUtilities;
	}

}
