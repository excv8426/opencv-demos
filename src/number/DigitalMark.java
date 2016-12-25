package number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class DigitalMark {
	
	public static void toImage(String complexi,String complexj){
		Mat img1=Highgui.imread(complexi,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		img1.convertTo(img1, CvType.CV_32FC2);
		Mat img2=Highgui.imread(complexj,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		img2.convertTo(img2, CvType.CV_32FC2);
		Mat img3=new Mat(img2.size(),CvType.CV_32FC2);
		List<Mat> inputmats=new ArrayList<>();
		inputmats.add(img1);
		inputmats.add(img2);
		Core.merge(inputmats, img3);
		System.out.println(CvType.typeToString(img1.type()));
		System.out.println(CvType.typeToString(img3.type()));
		Core.dft(img3, img3,Core.DFT_INVERSE , 0);
		List<Mat> outputmats=new ArrayList<>();
		Core.split(img3, outputmats);
		
		Highgui.imwrite("D:\\recover0.jpg", outputmats.get(0));
		Highgui.imwrite("D:\\recover1.jpg", outputmats.get(1));
		Mat re=outputmats.get(0);
		Mat im=outputmats.get(1);
		Core.pow(re, 2, re);
		Core.pow(im, 2, im);
		Core.add(re, im, im);
		
		Core.pow(im, 0.5, im);
		Highgui.imwrite("D:\\recover2.jpg", im);
		MinMaxLocResult minMaxLocResult=Core.minMaxLoc(im);
		double scale = 255/(minMaxLocResult.maxVal - minMaxLocResult.minVal);
		double shift = -minMaxLocResult.minVal * scale;
		Core.convertScaleAbs(im, im, scale, shift);
		
		/*Mat[] planes={img3,new Mat(img3.size(), CvType.CV_32F, Scalar.all(0))};
		Core.merge(Arrays.asList(planes), img3);
		System.out.println(CvType.typeToString(img3.type()));
		Highgui.imwrite("D:\\recover2.jpg", img3);*/
	}
	
	public static Mat dft(String path){
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
	
	
	public static Mat dft(Mat imported){
		Mat padded = new Mat(); 
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
	
	public static void mark(String original,String mark,String output){
		Mat img1=dft(original);
		Mat img2=Highgui.imread(mark,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		List<Mat> mats=new ArrayList<>();
		Core.split(img1, mats);
		Mat re=mats.get(0);
		Mat im=mats.get(1);
		img2.convertTo(img2, CvType.CV_32FC1);
		for (int i = 0; i < 100; i++) {
			Core.add(re, img2, re);
			Core.add(im, img2, im);
		}
		//Highgui.imwrite("D:\\output\\390mark.jpg", re);
		//Highgui.imwrite("D:\\output\\391mark.jpg", im);
		Mat img3=new Mat(img2.size(),CvType.CV_32F);
		Core.merge(Arrays.asList(re,im), img3);
		Core.dft(img3, img3,Core.DFT_SCALE , 0);
		Core.split(img3, mats);
		Highgui.imwrite(output, mats.get(0));
		//Highgui.imwrite("D:\\output\\391marked.jpg", mats.get(1));
	}
	
	
	
	public static void readMark(String marked,String mark,String output){
		Mat img1=dft(marked);
		Mat img2=dft(mark);
		List<Mat> img1s=new ArrayList<>();
		List<Mat> img2s=new ArrayList<>();	
		Core.split(img1, img1s);
		Core.split(img2, img2s);
		Mat readmark=new Mat(img2.size(),CvType.CV_32FC2);;
		Core.subtract(img1s.get(0), img2s.get(0), readmark);
		Highgui.imwrite(output, readmark);
	}
	
	
	
	
}
