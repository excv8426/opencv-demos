import org.opencv.core.Core;
import number.RecognizeNumber;

public class Runopencv {
	public static void main( String[] args ){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		//RecognizeNumber.numberstrain();
		RecognizeNumber.numbersread();
		/*DFT.mark();
		DFT.readMark();*/
		
	}
}
