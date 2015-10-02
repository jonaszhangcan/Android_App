package com.zhongzhang.randomstudent.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.zhongzhang.randomstudent.bean.StudentBean;

import android.os.Environment;
import android.util.Log;

public class Tool {
	
	public static final String CSV_PATH = getSDPath() + "/student";
	
	public static File getSDPath(){ 
	       File sdDir = null; 
	       boolean sdCardExist = Environment.getExternalStorageState()   
	                           .equals(Environment.MEDIA_MOUNTED);   //Checking whether SD card is existing
	       if   (sdCardExist)
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//Get the path
	      }
	       Log.e("zhongzhang", "sdDir = " + sdDir.toString());//Show words if SD card is not existed.
	       return sdDir;
	}
	
    public static List<File> getCsvFiles(File root){  
        File files[] = root.listFiles();  
        List<File> list = new ArrayList<File>();
        if(files != null){  
            for (File f : files){  
                if(!f.isDirectory() && f.toString().endsWith(".csv")){  
                      list.add(f);//find out all CSV files 
                } 
            }  
        }
        return list;
    }
    
    public static List<String> getFileName(List<File> list) {
    	List<String> liststring = new ArrayList<String>();
    	for (int i = 0; i < list.size(); i++) {
			String str = list.get(i).getName();
			liststring.add(str);
		}
    	return liststring;
    }
    
    /**
     * Import CSV files
 	 *
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return dataList;
    }
    
    /**
     * Export data to CSV files
     * 
     * TIPS: new CSV files cannot create 
     * dataList contains data
     * 
     */
    public static boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;
        
        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }
        
        return isSucess;
    }
    /**
     *  Supporting the sort options
     * 
     */
    public static Comparator<StudentBean> compareName(){
    	Comparator<StudentBean> comparator = new Comparator<StudentBean>() {

			@Override
			public int compare(StudentBean arg0, StudentBean arg1) {
				// TODO Auto-generated method stub
				return arg0.getFirstName().compareTo(arg1.getFirstName());
			}
		};
		return comparator;
    }
    
    public static Comparator<StudentBean> compareSecondName(){
    	Comparator<StudentBean> comparator = new Comparator<StudentBean>() {

			@Override
			public int compare(StudentBean arg0, StudentBean arg1) {
				// TODO Auto-generated method stub
				return arg0.getSecondName().compareTo(arg1.getSecondName());
			}
		};
		return comparator;
    }
    
    public static Comparator<StudentBean> compareCorrectRate(){
    	Comparator<StudentBean> comparator = new Comparator<StudentBean>() {

			@Override
			public int compare(StudentBean arg0, StudentBean arg1) {
				// TODO Auto-generated method stub
				double t1 = 0;
				if (arg0.getIncorrectTime() == 0) {
					t1 = 1.0;
				}else {
					t1 = (double)(arg0.getCorrectTime()/(double)(arg0.getCorrectTime()+arg0.getIncorrectTime()));
				}
				double t2 = 0;
				if (arg1.getIncorrectTime() == 0) {
					t2 = 1.0;
				}else {
					t2 = (double)(arg1.getCorrectTime()/(double)(arg1.getCorrectTime()+arg1.getIncorrectTime()));
				}
				if ((t1 - t2) >= 0) {
					return -1;
				}else {
					return 1;
				}
			}
		};
		return comparator;
    }
    
    public static Comparator<StudentBean> compareCallTime(){
    	Comparator<StudentBean> comparator = new Comparator<StudentBean>() {

			@Override
			public int compare(StudentBean arg0, StudentBean arg1) {
				// TODO Auto-generated method stub
				return  arg1.getCallTime() - arg0.getCallTime();
			}
		};
		return comparator;
    }
    
    public static Comparator<StudentBean> compareAbsence(){
    	Comparator<StudentBean> comparator = new Comparator<StudentBean>() {

			@Override
			public int compare(StudentBean arg0, StudentBean arg1) {
				// TODO Auto-generated method stub
				boolean t1 = arg0.getAttendance();
				boolean t2 = arg1.getAttendance();
				if (t1) {
					return -1;
				}else {
					return 1;
				}
			}
		};
		return comparator;
    }
}
