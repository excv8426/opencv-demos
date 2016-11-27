package number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class DFT {
	public static void idft(){
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
	
	private static List<Swap> randShuffle(Mat mat){
		int rows=mat.rows();
		Mat mat1d=mat.reshape(0, 1);
		int size=mat1d.cols();
		Random random=new Random();
		double [] tem;
		List<Swap> swaps=new ArrayList<>();
		for (int i = 0; i < mat1d.cols(); i++) {
			Swap swap=new Swap(i, random.nextInt(size));
			tem=mat1d.get(0, i);
			mat1d.put(0, i, mat1d.get(0, swap.getJ()));
			mat1d.put(0, swap.getJ(), tem);
			swaps.add(swap);
		}
		mat=mat1d.reshape(0, rows);
		return swaps;

	}
	
	private static void reShuffle(Mat mat,List<Swap> swaps){
		int rows=mat.rows();
		Mat mat1d=mat.reshape(0, 1);
		int size=mat1d.cols();
		double [] tem;
		Swap swap;
		for (int i = swaps.size()-1; i > -1; i--) {
			swap=swaps.get(i);
			tem=mat1d.get(0, swap.getI());
			mat1d.put(0, swap.getI(), mat1d.get(0, swap.getJ()));
			mat1d.put(0, swap.getJ(), tem);
		}
		mat=mat1d.reshape(0, rows);
	}
	
	public static void randShuffletest(){
		Mat img1=Highgui.imread("E:\\40.bmp");
		List<Swap> swaps=randShuffle(img1);
		//reShuffle(img1,swaps);
		/*reShuffle(img1,swaps);
		reShuffle(img1,swaps);*/
		Highgui.imwrite("D:\\51.jpg", img1);
	}
	
	
	
	
}
