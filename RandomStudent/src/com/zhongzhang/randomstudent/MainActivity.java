package com.zhongzhang.randomstudent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zhongzhang.randomstudent.R;
import com.zhongzhang.randomstudent.adapter.StudentAdapter;
import com.zhongzhang.randomstudent.bean.StudentBean;
import com.zhongzhang.randomstudent.tool.ClickDeleteStudent;
import com.zhongzhang.randomstudent.tool.ClickPhoto;
import com.zhongzhang.randomstudent.tool.Tool;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends Activity implements OnClickListener,ClickPhoto,ClickDeleteStudent{
	
	private List<File> listFile;
	private List<StudentBean> studentBeans;
	private ListView listView;
	private StudentAdapter adapter;
	private ImageView callStudentImageView;
	private int callNum;
	private LinearLayout addStudent;
	private LinearLayout sortLay;
	private final int RESULT_LOAD_IMAGE = 100;
	private int whichPhoto = 0;
	private LinearLayout importCsv;
	private Spinner spinner;
	private List<String> listName;
	private int whichItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        
    }

	private void initViews() {
		// TODO Auto-generated method stub
		spinner = (Spinner) findViewById(R.id.spinner);
		studentBeans = new ArrayList<StudentBean>();
		listFile = Tool.getCsvFiles(Tool.getSDPath());
		listName = Tool.getFileName(listFile);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listName);
		spinner.setAdapter(arrayAdapter);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 != whichItem) {
					whichItem = arg2;
					updateStudent();
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		listView = (ListView) findViewById(R.id.listview);
		getStudentInfo();
		adapter = new StudentAdapter(this, studentBeans,this,this);
		listView.setAdapter(adapter);
		callStudentImageView = (ImageView) findViewById(R.id.call_student_imageview);
		callStudentImageView.setOnClickListener(this);
		addStudent = (LinearLayout) findViewById(R.id.add_student);
		addStudent.setOnClickListener(this);
		sortLay = (LinearLayout) findViewById(R.id.sort_lay);
		sortLay.setOnClickListener(this);
		importCsv =  (LinearLayout) findViewById(R.id.import_csv);
		importCsv.setOnClickListener(this);
       
	}

	protected void updateStudent() {
		// TODO Auto-generated method stub
		studentBeans.clear();
		getStudentInfo();
		adapter.notifyDataSetChanged();
	}

	private void getStudentInfo() {
		// TODO Auto-generated method stub
		 List<String> listStrings = Tool.importCsv(listFile.get(whichItem));
	        if (listStrings.size() > 0) {
				for (int i = 0; i < listStrings.size(); i++) {
					String[] strings = listStrings.get(i).split(",");
						StudentBean bean = new StudentBean();
						bean.setHeadImage(strings[0]);
						bean.setFirstName(strings[1]);
						bean.setSecondName(strings[2]);
						bean.setGender(strings[3]);
						if (strings[4].equals("yes")) {
							bean.setAttendance(true);
						}else {
							bean.setAttendance(false);
						}
						bean.setCallTime(Integer.parseInt(strings[5]));
						bean.setCorrectTime(Integer.parseInt(strings[6]));
						bean.setIncorrectTime(Integer.parseInt(strings[7]));
						studentBeans.add(bean);
				}
			}else {
				Log.e("zhongzhang", "null content");
			}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.call_student_imageview:
			initCallDialog();
			break;

		case R.id.add_student:
			addStudent();
			break;
			
		case R.id.sort_lay:
			sortList();
			break;
			
		case R.id.import_csv:
			SaveToCsv();
			break;
		}
	}

	private void SaveToCsv() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < studentBeans.size(); i++) {
			String csvStr = "";
			csvStr = csvStr + studentBeans.get(i).getHeadImage() + "," + studentBeans.get(i).getFirstName()
					+ "," + studentBeans.get(i).getSecondName() + "," + studentBeans.get(i).getGender() + ",";
			if (studentBeans.get(i).getAttendance()) {
				csvStr  = csvStr + "yes" + ",";
			}else {
				csvStr = csvStr + "no" + ",";
			}
			csvStr = csvStr + studentBeans.get(i).getCallTime() + "," + studentBeans.get(i).getCorrectTime() + "," + studentBeans.get(i).getIncorrectTime();
			list.add(csvStr);
		}
		if (Tool.exportCsv(listFile.get(whichItem), list)) {
			new AlertDialog.Builder(this).setTitle("svae file").setMessage("save success!").show();
		}else {
			new AlertDialog.Builder(this).setTitle("svae file").setMessage("save failed!").show();
		}
	}

	private void sortList() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("sort option").setItems(new String[]{
				"First Name","Second Name","Absences","Percent Correct","Call Frequency"
		}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case 0:
					Collections.sort(studentBeans,Tool.compareName());
					adapter.notifyDataSetChanged();
					break;

				case 1:
					Collections.sort(studentBeans,Tool.compareSecondName());
					adapter.notifyDataSetChanged();
					break;
					
				case 2:
					Collections.sort(studentBeans,Tool.compareAbsence());
					adapter.notifyDataSetChanged();
					break;
					
				case 3:
					Collections.sort(studentBeans,Tool.compareCorrectRate());
					adapter.notifyDataSetChanged();
					break;
					
				case 4:
					Collections.sort(studentBeans,Tool.compareCallTime());
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}).show();
	}

	private void addStudent() {
		// TODO Auto-generated method stub
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("add student");
		View view = LayoutInflater.from(this).inflate(R.layout.add_student, null);
		final EditText firstName = (EditText) view.findViewById(R.id.fist_name);
		final EditText secondName = (EditText) view.findViewById(R.id.second_name);
		final ToggleButton choiceSex = (ToggleButton) view.findViewById(R.id.choice_sex);
		new AlertDialog.Builder(this).setTitle("add student").setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				StudentBean bean = new StudentBean();
				bean.setFirstName(firstName.getText().toString());
				bean.setSecondName(secondName.getText().toString());
				if (choiceSex.isChecked()) {
					bean.setGender("male");
				}else {
					bean.setGender("female");
				}
				studentBeans.add(bean);
				adapter.notifyDataSetChanged();
				arg0.dismiss();
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		}).show();
	}

	private void initCallDialog() {
		// TODO Auto-generated method stub
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		View view = LayoutInflater.from(this).inflate(R.layout.select_dialog, null);
		TextView nameTextView = (TextView) view.findViewById(R.id.name_dialog_title);
		ImageView headImageView = (ImageView) view.findViewById(R.id.head_image_dialog);
		
		callNum = (int) (Math.random() * studentBeans.size());
		boolean isAllUnAttence = true;
		for (int i = 0; i < studentBeans.size(); i++) {
			if ( studentBeans.get(i).getAttendance()) {
				isAllUnAttence = false;
			}
		}
		if (isAllUnAttence) {
			Toast.makeText(this, "Sorry!no one attendance", Toast.LENGTH_SHORT).show();
			return;
		}
		boolean isAllCalled = true;
		for (int i = 0; i < studentBeans.size(); i++) {
			if (!studentBeans.get(i).getIsCalled() && studentBeans.get(i).getAttendance()) {
				isAllCalled = false;
			}
		}
		if (isAllCalled) {
			for (int i = 0; i < studentBeans.size(); i++) {
				studentBeans.get(i).setIsCalled(false);
			}
		}
		while (!studentBeans.get(callNum).getAttendance() || studentBeans.get(callNum).getIsCalled()) {
			callNum = (int) (Math.random() * studentBeans.size());
			
			
		}
		studentBeans.get(callNum).setIsCalled(true);
		ImageView correct,incorrect,dissmiss;
		correct = (ImageView) view.findViewById(R.id.correct_btn);
		incorrect = (ImageView) view.findViewById(R.id.incorrect_btn);
		dissmiss = (ImageView) view.findViewById(R.id.dissmiss_btn);
		correct.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int time = studentBeans.get(callNum).getCorrectTime();
				int callTime = studentBeans.get(callNum).getCallTime();
				time++;
				callTime++;
				studentBeans.get(callNum).setCorrectTime(time);
				studentBeans.get(callNum).setCallTime(callTime);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		incorrect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int time = studentBeans.get(callNum).getIncorrectTime();
				int callTime = studentBeans.get(callNum).getCallTime();
				time--;
				if (time < 0) {
					time = 0;
				}
				callTime++;
				studentBeans.get(callNum).setIncorrectTime(time);
				studentBeans.get(callNum).setCallTime(callTime);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		dissmiss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int callTime = studentBeans.get(callNum).getCallTime();
				callTime++;
				studentBeans.get(callNum).setCallTime(callTime);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		Bitmap bitmap = BitmapFactory.decodeFile(studentBeans.get(callNum).getHeadImage());
		if (studentBeans.get(callNum).getHeadImage().equals("")) {
			headImageView.setImageResource(R.drawable.photo_template_hd);
		}else {
			headImageView.setImageBitmap(bitmap);
		}
		nameTextView.setText(studentBeans.get(callNum).getFirstName() + " " + studentBeans.get(callNum).getSecondName());
		dialog.setView(view);
		dialog.show();
	}

	@Override
	public void onClickPhoto(final int whicth) {
		// TODO Auto-generated method stub
		whichPhoto = whicth;
		new AlertDialog.Builder(this).setTitle("Photo Option").setItems(new String[]{
		"Gallery","Camera","Delete"
		}, new  DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case 0:
					Intent i = new Intent(
							Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(i, RESULT_LOAD_IMAGE);
					break;
		
				case 1:
					
					break;
					
				case 2:
					studentBeans.get(whicth).setHeadImage("");
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	        Uri selectedImage2 = data.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	        Cursor cursor = getContentResolver().query(selectedImage2,
	                filePathColumn, null, null, null);
	        cursor.moveToFirst();
	 
	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        String picturePath = cursor.getString(columnIndex);
	        cursor.close();
	        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
	        studentBeans.get(whichPhoto).setHeadImage(picturePath);
	        adapter.notifyDataSetChanged();
	        // String picturePath contains the path of selected Image
		}
	}

	@Override
	public void onDeleteStudent(final int whicth) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(MainActivity.this).setMessage("are you sure want to delete it ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				studentBeans.remove(whicth);
				adapter.notifyDataSetChanged();
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
			}
		}).show();
	}

   

}
