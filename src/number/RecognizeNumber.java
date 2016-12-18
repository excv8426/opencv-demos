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

public class RecognizeNumber {
	static int image_width=8;
	static int image_height=16;
	static Mat layersize=new Mat(1, 5, CvType.CV_32S);
	static int[][] layersizes={
			{image_width*image_height},
			{128},
			{128},
			{128},
			{10}
	};
	static CvANN_MLP bp=new CvANN_MLP();
	static{
		layersize.put(0,0,layersizes[0]);
		layersize.put(0,1,layersizes[1]);
		layersize.put(0,2,layersizes[2]);
		layersize.put(0,3,layersizes[3]);
		layersize.put(0,4,layersizes[4]);
		bp.create(layersize, CvANN_MLP.SIGMOID_SYM, 1, 1);
	}
	
	public static void numberstrain(){
		String trainpath="C:\\charSamples";
		File traindir=new File(trainpath);
		File[] trainimages=null;
		File[] trainclasses=traindir.listFiles();
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
				Imgproc.resize(srcimage, resizedimage, new Size(image_width,image_height), 0, 0, Imgproc.INTER_AREA);
				Imgproc.Canny(resizedimage, resizedimage, 150, 100,3,false);				
				for (int i = 0; i < image_height; i++) {
					for (int j = 0; j < image_width; j++) {
						traindata.put(rowindex, colindex, resizedimage.get(i, j)[0]);
						colindex++;
					}
				}				
				for (int i = 0; i < 10; i++) {
					if (trainimage.getParentFile().getName().equals(Integer.toString(i))) {
						lables.put(rowindex, i, 1);
					} else {
						lables.put(rowindex, i, 0);
					}
				}
				rowindex++;
			}
		}
		CvANN_MLP_TrainParams params=new CvANN_MLP_TrainParams();
		params.set_train_method(CvANN_MLP_TrainParams.BACKPROP);
		params.set_bp_dw_scale(0.001);
		params.set_bp_moment_scale(0.1);
		params.set_term_crit(new TermCriteria(TermCriteria.EPS|TermCriteria.MAX_ITER,10000,0.0001));
		bp.train(traindata, lables, new Mat(), new Mat(), params, 0);
		bp.save("D:\\train.xml");
	}
	
	public static void numbersread(){
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
			Imgproc.Canny(testdata, testdata, 150, 100,3,false);
			for (int i = 0; i < image_height; i++) {
				for (int j = 0; j < image_width; j++) {
					onedimage.put(0, (i*image_width)+j, testdata.get(i, j)[0]);
				}
			}
			Mat output=new Mat(1, 128, CvType.CV_32FC1);
			bp.load("D:\\train.xml");
			bp.predict(onedimage, output);
			for (int i = 0; i < output.rows(); i++) {
				for (int j = 0; j < output.cols(); j++) {
					System.out.println("i:"+i+"j:"+j+"output:"+output.get(i, j)[0]);
				}
			}
			
		}
	}
}
