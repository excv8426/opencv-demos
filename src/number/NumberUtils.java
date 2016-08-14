package number;

import java.io.File;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvANN_MLP;
import org.opencv.ml.CvANN_MLP_TrainParams;

public class NumberUtils {
	static CvANN_MLP bp=new CvANN_MLP();
	public static void numberstrain(){
		String trainpath="C:\\charSamples";
		File traindir=new File(trainpath);
		File[] trainimages=null;
		File[] trainclasses=traindir.listFiles();
		int image_width=8;
		int image_height=16;
		
		Mat srcimage=null;
		Mat resizedimage=new Mat();
		Mat traindata=new Mat(10*50, image_width*image_height, CvType.CV_32FC1);
		Mat lables=new Mat(10*50, 10, CvType.CV_32FC1);
		int rowindex=0;
		int colindex=0;
		for (File trainclass : trainclasses) {
			trainimages=trainclass.listFiles();
			for (File trainimage : trainimages) {
				colindex=0;
				srcimage=Highgui.imread(trainimage.getAbsolutePath());
				//System.out.println(trainimage.getParentFile().getName());
				Imgproc.resize(srcimage, resizedimage, new Size(image_width,image_height), 0, 0, Imgproc.INTER_AREA);
				//System.out.println(resizedimage.get(0, 0).length);
				//Imgproc.threshold(resizedimage, grayimage, 0, 255, Imgproc.THRESH_BINARY);
				Imgproc.Canny(resizedimage, resizedimage, 150, 100,3,false);
				//System.out.println(resizedimage.get(0, 0).length);
				
				for (int i = 0; i < image_height; i++) {
					for (int j = 0; j < image_width; j++) {
						/*double[] d=srcimage.get(i, j);
						for (double e : d) {
							System.out.println(e);
						}*/
						/*System.out.println(i+"  "+j);
						System.out.println(resizedimage.get(i, j).length);*/
						//System.out.println(resizedimage.get(i, j)[0]);
						traindata.put(rowindex, colindex, resizedimage.get(i, j)[0]);
						//System.out.print(resizedimage.get(i, j)[0]);
						/*if (rowindex=colindex) {
							
						} else {

						}*/
						colindex++;
					}
				}
				//System.out.println("----------");
				
				for (int i = 0; i < 10; i++) {
					if (trainimage.getParentFile().getName().equals(Integer.toString(i))) {
						lables.put(rowindex, i, 1);
					} else {
						lables.put(rowindex, i, 0);
					}
					
				}
				rowindex++;
				
				//System.out.println(rowindex);
				/*System.out.println(resizedimage.rows());
				System.out.println(resizedimage.cols());*/
				
			}
		}
		
		CvANN_MLP_TrainParams params=new CvANN_MLP_TrainParams();
		params.set_train_method(CvANN_MLP_TrainParams.BACKPROP);
		params.set_bp_dw_scale(0.001);
		params.set_bp_moment_scale(0.1);
		params.set_term_crit(new TermCriteria(TermCriteria.EPS|TermCriteria.MAX_ITER,10000,0.0001));
		Mat layersize=new Mat(1, 5, CvType.CV_32S);
		int[][] layersizes={
				{image_width*image_height},
				{128},
				{128},
				{128},
				{10}
		};
		layersize.put(0,0,layersizes[0]);
		layersize.put(0,1,layersizes[1]);
		layersize.put(0,2,layersizes[2]);
		layersize.put(0,3,layersizes[3]);
		layersize.put(0,4,layersizes[4]);
		//System.out.println(layersize.rows());
		//System.out.println(layersize.cols());
		bp.create(layersize, CvANN_MLP.SIGMOID_SYM, 1, 1);
		bp.train(traindata, lables, new Mat(), new Mat(), params, 0);
		bp.save("D:\\train.xml");
		/*int class_num=10;
		int samples_perclass=20;
		int image_width=8;
		int image_height=16;
		float trainingData[][]=new float[class_num*samples_perclass][image_width*image_height];
		
		CvANN_MLP bp=new CvANN_MLP();
		CvANN_MLP_TrainParams params=new CvANN_MLP_TrainParams();
		params.set_train_method(CvANN_MLP_TrainParams.BACKPROP);
		params.set_bp_dw_scale(0.001);
		params.set_bp_moment_scale(0.1);
		params.set_term_crit(new TermCriteria(TermCriteria.EPS|TermCriteria.MAX_ITER,10000,0.0001));
		
		bp.train(trainingDataMat, labelsMat, new Mat(),new Mat(), params);*/
	}
	
	public static void numberstest(){
		String testpath="D:\\testimage";
		File testdir=new File(testpath);
		File[] testimages=testdir.listFiles();
		Mat testdata=null;
		int image_width=8;
		int image_height=16;
		
		for (File testimage : testimages) {
			Mat onedimage=new Mat(1, image_width*image_height, CvType.CV_32FC1);
			testdata=Highgui.imread(testimage.getAbsolutePath());
			Imgproc.resize(testdata, testdata, new Size(image_width,image_height), 0, 0, Imgproc.INTER_AREA);
			//System.out.println(resizedimage.get(0, 0).length);
			//Imgproc.threshold(resizedimage, grayimage, 0, 255, Imgproc.THRESH_BINARY);
			Imgproc.Canny(testdata, testdata, 150, 100,3,false);
			
			
			//System.out.println(testdata.rows());
			//System.out.println(testdata.cols());
			//System.out.println("0---"+testdata.get(0,0).length);
			//System.out.println("1---"+testdata.get(0,0)[1]);
			//System.out.println("2---"+testdata.get(0,0)[2]);
			for (int i = 0; i < image_height; i++) {
				for (int j = 0; j < image_width; j++) {
					//System.out.println(testdata.get(i, j)[0]);
					onedimage.put(0, (i*image_width)+j, testdata.get(i, j)[0]);
					//System.out.print(testdata.get(i, j)[0]);
				}
			}
			//System.out.println("------------");
			Mat output=new Mat(1, 128, CvType.CV_32FC1);
			
			/*for (int i = 0; i < 128; i++) {
				System.out.print(onedimage.get(0, i)[0]);
			}
			System.out.println("----------");*/
			bp.predict(onedimage, output);
			
			
			/*for (int i = 0; i < 128; i++) {
				System.out.print(onedimage.get(0, i)[0]);
			}
			System.out.println("----------");*/
			for (int i = 0; i < output.rows(); i++) {
				for (int j = 0; j < output.cols(); j++) {
					System.out.println("i:"+i+"j:"+j+"output:"+output.get(i, j)[0]);
				}
			}
			
		}
	}
}
