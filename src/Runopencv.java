import org.opencv.core.Core;

import number.DigitalMark;
import number.RecognizeNumber;

public class Runopencv {
	public static void main( String[] args ){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		//DigitalMark.mark("D:\\opencvdata\\input\\39.jpg","D:\\opencvdata\\input\\mark.jpg","D:\\opencvdata\\output\\390marked.jpg");
		//DigitalMark.readMark("D:\\opencvdata\\output\\390marked1.jpg","D:\\opencvdata\\input\\39.jpg","D:\\opencvdata\\output\\readmark.jpg");
		DigitalMark.colourdMark("D:\\opencvdata\\input\\39.jpg","D:\\opencvdata\\input\\mark.jpg","D:\\opencvdata\\output\\390marked.jpg");
	}
}
