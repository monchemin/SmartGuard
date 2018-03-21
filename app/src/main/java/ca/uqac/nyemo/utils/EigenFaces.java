package ca.uqac.nyemo.utils;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

import java.io.*;
import java.util.ArrayList;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * Created by Nyemo on 23/01/2018.
 */
public class EigenFaces implements Serializable {

    ArrayList<MatOfInt> imgList = new ArrayList<MatOfInt>();
     transient String trainDir;
    protected double[][] meanMat;
    protected double[][] eigenFacestranspose;
    //Jama.Matrix weights;
    protected double[][] weights;
public static final long serialVersionUID = 2307891229331897664L;

public void EigenFaces() {


    }

    public void trainProcess(String imgDir) {
        this.trainDir = imgDir;
    }

    public void execute(ArrayList<Mat> facesArrayList) throws NullPointerException, IOException {
//        File images = new File(this.trainDir);
        double[][] trainMat = EigenFaces.matToArray(facesArrayList);
        doExecute(trainMat);
        //double[][] trainMat = EigenFaces.loadImageToArray(images.getAbsolutePath());
    }
    public void execute() throws NullPointerException, IOException {
        File images = new File(this.trainDir);
        double[][] trainMat = EigenFaces.loadImageToArray(images.getAbsolutePath());
        doExecute(trainMat);
    }

    public void execute(double[][] trainMat) {

        doExecute(trainMat);
    }

    private void doExecute(double[][] trainMat) {
        this.meanMat = meanMatrix(trainMat);
        double[][] phi = minusMatrix(trainMat, meanMat);
        Jama.Matrix phiMat = new Jama.Matrix(phi);
        Jama.Matrix eigenF = makeEigenFaces(phi);
//        Jama.Matrix reduceEigenF = eigenF.getMatrix(0, eigenF.getColumnDimension(), 0, 5);
        //this.eigenFacestranspose = reduceEigenF.transpose();
        this.eigenFacestranspose = eigenF.transpose().getArray();
        this.weights = matDot(eigenFacestranspose, phiMat.getArray());
    }



    public double find(String imageToFind) throws IOException {

        double[][] imgMat = loadSingleImageToArray(imageToFind);
        double[][] phiImage = minusMatrix(imgMat, meanMat);
        double[][] wImage = matDot(eigenFacestranspose, phiImage);

        double distanceMin = 5000;
        for (int i = 0; i < this.weights.length; i++) {
            double currentDistance = 0;
            for (int j = 0; j < this.weights[0].length; j++) {
                currentDistance += Math.pow((this.weights[j][i] - wImage[j][0]), 2);
            }
            System.out.println("distance : " + i + " : " + currentDistance);
            distanceMin = (distanceMin < currentDistance) ? distanceMin : currentDistance;
            System.out.println("Distance minimale : " + i + " : " + distanceMin);
        }
        System.out.println("Distance minimale : " + distanceMin);
        return distanceMin;

    }

    public double find(Mat imageToFind) throws IOException {

        double[][] imgMat = loadSingleImageToArray(imageToFind);

        return doFind(imgMat);


    }

    public double find(double[][] imgMat) throws IOException {


        return doFind(imgMat);

    }

    private double doFind( double[][] imgMat) {
        double[][] phiImage = minusMatrix(imgMat, meanMat);
        double[][] wImage = matDot(eigenFacestranspose, phiImage);

        double distanceMin = 5000;
        for (int i = 0; i < this.weights.length; i++) {
            double currentDistance = 0;
            for (int j = 0; j < this.weights[0].length; j++) {
                currentDistance += Math.pow((this.weights[j][i] - wImage[j][0]), 2);
            }
            System.out.println("distance : " + i + " : " + currentDistance);
            distanceMin = (distanceMin < currentDistance) ? distanceMin : currentDistance;
            System.out.println("Distance minimale : " + i + " : " + distanceMin);
        }
        System.out.println("Distance minimale : " + distanceMin);
        return distanceMin;
    }



    /**
     * @param imgDir
     * @return list of transformed Mat image
     * @throws IOException
     */
    public static double[][] loadImageToArray(String imgDir) throws IOException {
        File imgDirectory = new File(imgDir); // load file dir
        ArrayList<int[]> imageArrayList = new ArrayList<int[]>();
        if (imgDirectory.isDirectory()) { //scan dir for read files

            //imgDirectory.listFiles().length
            for (File imgFile : imgDirectory.listFiles()) {
                Log.i("Service", "loadImageToArray: process : " + imgFile.getName());
                Mat image = imread(imgFile.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE); // load image in gray scale
                //System.out.println("img row : " + image.rows() + " : " + image.cols() );
                MatOfInt trans = new MatOfInt();
                image.convertTo(trans, CvType.CV_32S); // convert image to MatOfInt
                int[] imageArrayOfInt = new int[(int) (image.total() * image.channels())]; // create
                trans.get(0, 0, imageArrayOfInt);
                System.out.println(imageArrayOfInt.length);
                //System.out.println("trans row : " + trans.rows() + " : " +  trans.cols() );
                imageArrayList.add(imageArrayOfInt); // add to list

            }


        } else {
            System.out.println(imgDir + " not a directory !");

        }

        return ArrayListToMat(imageArrayList, imageArrayList.get(0).length, imageArrayList.size());
    }


    public static double[][] matToArray(ArrayList<Mat> imgDir) throws IOException {

        ArrayList<int[]> imageArrayList = new ArrayList<int[]>();
            for (Mat image : imgDir) {

               //Mat image = imread(imgFile.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE); // load image in gray scale
                //System.out.println("img row : " + image.rows() + " : " + image.cols() );
                MatOfInt trans = new MatOfInt();
                image.convertTo(trans, CvType.CV_32S); // convert image to MatOfInt
                int[] imageArrayOfInt = new int[(int) (image.total() * image.channels())]; // create
                trans.get(0, 0, imageArrayOfInt);
                System.out.println(imageArrayOfInt.length);
                //System.out.println("trans row : " + trans.rows() + " : " +  trans.cols() );
                imageArrayList.add(imageArrayOfInt); // add to list

            }

        return ArrayListToMat(imageArrayList, imageArrayList.get(0).length, imageArrayList.size());
    }



    public static double[][] loadSingleImageToArray(String imgPath) throws IOException, IndexOutOfBoundsException {
        File imgFile = new File(imgPath); // load file dir
        ArrayList<int[]> imageArrayList = new ArrayList<int[]>();

        System.out.println("process : " + imgFile.getName());
        Mat image = imread(imgFile.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE); // load image in gray scale
        System.out.println("img row : " + image.rows() + " : " + image.cols() );
        MatOfInt trans = new MatOfInt();
        image.convertTo(trans, CvType.CV_32S); // convert image to MatOfInt
        int[] imageArrayOfInt = new int[(int) (image.total() * image.channels())]; // create
        trans.get(0, 0, imageArrayOfInt);
        System.out.println(imageArrayOfInt.length);
        //System.out.println("trans row : " + trans.rows() + " : " +  trans.cols() );
        imageArrayList.add(imageArrayOfInt); // add to list

        return ArrayListToMat(imageArrayList, imageArrayList.get(0).length, imageArrayList.size());
    }

    public static double[][] loadSingleImageToArray(Mat image) throws IOException {
        //File imgFile = new File(imgPath); // load file dir
        ArrayList<int[]> imageArrayList = new ArrayList<int[]>();

        //System.out.println("process : " + imgFile.getName());
        //Mat image = imread(imgFile.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE); // load image in gray scale
       // Mat image = new Mat();
        //Imgproc.cvtColor(imgPath, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        //System.out.println("img row : " + image.rows() + " : " + image.cols() );
        MatOfInt trans = new MatOfInt();
        image.convertTo(trans, CvType.CV_32S); // convert image to MatOfInt
        int[] imageArrayOfInt = new int[(int) (image.total() * image.channels())]; // create
        trans.get(0, 0, imageArrayOfInt);
        System.out.println(imageArrayOfInt.length);
        //System.out.println("trans row : " + trans.rows() + " : " +  trans.cols() );
        imageArrayList.add(imageArrayOfInt); // add to list

        return ArrayListToMat(imageArrayList, imageArrayList.get(0).length, imageArrayList.size());
    }

    /**
     * @param theListe
     * @param lines
     * @param cols
     * @return mat of image converted to int (result)
     */
    public static double[][] ArrayListToMat(ArrayList<int[]> theListe, int lines, int cols) throws IndexOutOfBoundsException {
        double[][] result = new double[lines][cols];
        for (int col = 0; col < cols; col++) {
            for (int line = 0; line < lines; line++) {
                result[line][col] = theListe.get(col)[line];
            }
        }
        return result;
    }

    /**
     * @param mBase : matrix of images
     * @return mean matrix
     */
    public static double[][] meanMatrix(double[][] mBase) throws ArrayIndexOutOfBoundsException {

        int lines = mBase.length;
        int cols = mBase[0].length;
        double[][] meanMat = new double[lines][1];
        for (int line = 0; line < lines; line++) {
            double value = 0;
            for (int col = 0; col < cols; col++) {

                value += mBase[line][col];
            }
            value /= cols;
            meanMat[line][0] = value;

        }
        return meanMat;
    }

    /**
     * @param matA
     * @param matMean
     * @return matA - matMean
     * @throws ArrayIndexOutOfBoundsException
     */
    public static double[][] minusMatrix(double[][] matA, double[][] matMean) throws ArrayIndexOutOfBoundsException {

        int lines = matA.length;
        int cols = matA[0].length;
        System.out.println("nous somme dans minusMatrix");
        System.out.println("MatA : " + lines + " : " + cols);
        System.out.println("MatB : " + matMean.length + " : " + matMean[0].length);
        double[][] minusMat = new double[lines][cols];
        for (int line = 0; line < lines; line++) {
            double value = 0;
            for (int col = 0; col < cols; col++) {
                minusMat[line][col] = matA[line][col] - matMean[line][0];
                //double a = matA[line][col] - matMean[line][0];
            }
        }
        return minusMat;
    }

    /**
     * compute eigenfaces;
     *
     * @param phi
     * @return
     */

    public static Jama.Matrix makeEigenFaces(double[][] phi) {
        Jama.Matrix phiSimple = new Jama.Matrix(phi);

        return phiSimple.svd().getV();
    }

    /**
     * @param matA
     * @param matB
     * @return matA dot matB
     * @throws IndexOutOfBoundsException
     * @ssume that line matA = colmatB
     */

    public static double[][] matDot(double[][] matA, double[][] matB) throws IndexOutOfBoundsException {

        int lineA = matA.length;
        int colA = matA[0].length;
        int colB = matB[0].length;
        double dot[][] = new double[lineA][colB];

        for (int i = 0; i < lineA; i++) { // iterate for matA line

            for (int j = 0; j < colB; j++) { // itarate for matB col
                dot[i][j] = 0; // initilize dot[i][j]
                for (int k = 0; k < colA; k++) { // last iterate
                    dot[i][j] += matA[i][k] * matB[k][j];
                }
            }

        }

        return dot;
    }

    /**
     * print matrix
     *
     * @param mat
     */

    public static void matToString(double[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /**
     * Save the model
     * @throws IOException
     */
    public void saveModel(String fileName) throws IOException {
        Log.i("FaceActivity", "saveModel: " + fileName);

        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(fileName))));
            oos.writeObject(this);

            oos.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}


    }

    /**
     *
     * @param modelfile
     * @return the model
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static EigenFaces loadModel(String modelfile) throws IOException, ClassNotFoundException {
        ObjectInputStream ois;
        ois = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(
                                new File(modelfile))));

        try {
            return (EigenFaces)ois.readObject();
            //Object o = ois.readObject();
            //System.out.println(o.getClass());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
        e.printStackTrace();
        }
        finally {
            ois.close();
        }

    return null;
}
    public double[][]  getMeanMat() {
        return this.meanMat;
    }
}
