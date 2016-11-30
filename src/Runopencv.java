import java.util.Collections;

import org.opencv.core.Core;
import number.DFT;

public class Runopencv {
	public static void main( String[] args ){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    /*Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    System.out.println( "mat = " + mat.dump() );
	    NumberUtils.numberstrain();
	    NumberUtils.numberstest();*/
		//DFT.toImage("D:\\39.jpg","D:\\391.jpg");
		DFT.idft();
		
		
	}
}
