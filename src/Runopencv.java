import java.util.Collections;

import org.opencv.core.Core;
import number.DFT;

public class Runopencv {
	public static void main( String[] args ){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		DFT.mark();
		DFT.readMark();
		
	}
}
