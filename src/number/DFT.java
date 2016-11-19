package number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class DFT {
	public static void cal(){
		Mat img1=dft("E:\\39.jpg");
		Mat img2=dft("E:\\40.bmp");
		
		System.out.println(img1.size());
		System.out.println(img2.size());
		Mat img3=new Mat(img2.size(),CvType.CV_32F);
		Core.add(img1, img2, img3);
		Core.dft(img3, img3,Core.DFT_SCALE , 0);
		//img1.convertTo(img1, CvType.CV_8U);
		
		List<Mat> mats=new ArrayList<>();
		Core.split(img3, mats);
		Highgui.imwrite("D:\\39.jpg", mats.get(0));
	}
	
	private static Mat dft(String path){
		Mat padded = new Mat(); 
		Mat imported=Highgui.imread(path,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		int m =Core.getOptimalDFTSize(imported.rows());
		int n =Core.getOptimalDFTSize(imported.cols());
		Imgproc.copyMakeBorder(imported, padded, 0, m-imported.rows(), 0, n-imported.cols(), Imgproc.BORDER_CONSTANT, Scalar.all(0));
		Mat complexI=new Mat(padded.size(),CvType.CV_32F);
		padded.convertTo(padded, CvType.CV_32F);
		Mat[] planes={padded,new Mat(padded.size(), CvType.CV_32F, Scalar.all(0))};
		Core.merge(Arrays.asList(planes), complexI);
		Core.dft(complexI, complexI);
		List<Mat> dftimg=new ArrayList<>();
		Core.split(complexI, dftimg);
		return complexI;
	}
}
