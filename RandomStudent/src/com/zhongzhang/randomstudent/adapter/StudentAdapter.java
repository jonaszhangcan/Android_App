package com.zhongzhang.randomstudent.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.zhongzhang.randomstudent.R;
import com.zhongzhang.randomstudent.bean.StudentBean;
import com.zhongzhang.randomstudent.tool.ClickDeleteStudent;
import com.zhongzhang.randomstudent.tool.ClickPhoto;
import com.zhongzhang.randomstudent.tool.Tool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentAdapter extends BaseAdapter{
	
	private List<StudentBean> beans;
	private Context mContext;
	private ClickPhoto mClickPhoto;
	private ClickDeleteStudent mDeleteStudent;
	
	public StudentAdapter(Context mContext,List<StudentBean> beans,ClickPhoto clickd,ClickDeleteStudent deleteStudent){
		
		this.mContext = mContext;
		this.beans = beans; //The data which is shown in the listview
		this.mClickPhoto = mClickPhoto;
		this.mDeleteStudent = deleteStudent;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (arg1 == null) {
			viewHolder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.list_item2, null);
			viewHolder.headImage = (ImageView) arg1.findViewById(R.id.head_image);
			viewHolder.firstName = (TextView) arg1.findViewById(R.id.first_name);
			viewHolder.secondName = (TextView) arg1.findViewById(R.id.second_name);
			viewHolder.correctRate = (TextView) arg1.findViewById(R.id.correct_rate);
			viewHolder.attendance = (ImageView) arg1.findViewById(R.id.attendance_image);
			viewHolder.callTime = (TextView) arg1.findViewById(R.id.call_time);
			viewHolder.correctTime = (TextView) arg1.findViewById(R.id.correct_time);
			viewHolder.incorrectTime = (TextView) arg1.findViewById(R.id.incorrect_time);
			arg1.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		//Bitmap bitmap = BitmapFactory.decodeFile(Tool.getSDPath() + "/" + beans.get(arg0).getHeadImage());
		Bitmap bitmap = BitmapFactory.decodeFile(beans.get(arg0).getHeadImage());
		if (beans.get(arg0).getHeadImage().equals("")) {
			viewHolder.headImage.setImageResource(R.drawable.photo_template_hd);
		}else {
			viewHolder.headImage.setImageBitmap(bitmap);
		}
		viewHolder.headImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				mClickPhoto.onClickPhoto(arg0);
			}
		});
		viewHolder.firstName.setText(beans.get(arg0).getFirstName());
		viewHolder.secondName.setText(beans.get(arg0).getSecondName());
		viewHolder.callTime.setText("" + beans.get(arg0).getCallTime());
		viewHolder.correctTime.setText("" + beans.get(arg0).getCorrectTime());
		viewHolder.incorrectTime.setText("" + beans.get(arg0).getIncorrectTime());
		if (beans.get(arg0).getAttendance()) {
			viewHolder.attendance.setImageResource(R.drawable.present_icon1);
		}else {
			viewHolder.attendance.setImageResource(R.drawable.absent_icon1);
		}
		viewHolder.correctRate.setText(beans.get(arg0).getCorrectRate());
		viewHolder.attendance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (beans.get(arg0).getAttendance()) {
					viewHolder.attendance.setImageResource(R.drawable.absent_icon1);
					beans.get(arg0).setAttendance(false);
				}else {
					viewHolder.attendance.setImageResource(R.drawable.present_icon1);
					beans.get(arg0).setAttendance(true);
				}
			}
		});
		viewHolder.callTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext).setTitle("Call Time Option").setItems(new String[]{
						"INCREASE","DECREASE","RESET TO ZERO"
				}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialogInterface, int whitch) {
						// TODO Auto-generated method stub
						int time = beans.get(arg0).getCallTime();
						switch (whitch) {
						case 0:
							time++;
							viewHolder.callTime.setText("" + time);
							beans.get(arg0).setCallTime(time);
							break;
							
						case 1:
							time--;
							if (time < 0) {
								time = 0;
							}
							viewHolder.callTime.setText("" + time);
							beans.get(arg0).setCallTime(time);
							break;
						
						case 2:
							time = 0;
							viewHolder.callTime.setText("" + time);
							beans.get(arg0).setCallTime(time);
							break;
						}
					}


				}).show();
			}
		});
		
		viewHolder.correctTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext).setTitle("Correct Time Option").setItems(new String[]{
						"INCREASE","DECREASE","RESET TO ZERO"//setting the showing words
				}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialogInterface, int whitch) {
						// TODO Auto-generated method stub
						int time = beans.get(arg0).getCorrectTime();
						switch (whitch) {
						case 0:
							time++;
							viewHolder.correctTime.setText("" + time);
							beans.get(arg0).setCorrectTime(time);
							viewHolder.correctRate.setText(beans.get(arg0).getCorrectRate());
							break;
							
						case 1:
							time--;
							if (time < 0) {
								time = 0;
							}
							viewHolder.correctTime.setText("" + time);
							beans.get(arg0).setCorrectTime(time);
							viewHolder.correctRate.setText(beans.get(arg0).getCorrectRate());
							break;
						
						case 2:
							time = 0;
							viewHolder.correctTime.setText("" + time);
							beans.get(arg0).setCorrectTime(time);
							viewHolder.correctRate.setText(beans.get(arg0).getCorrectRate());
							break;
						}
					}


				}).show();
			}
		});
		
		viewHolder.firstName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mDeleteStudent.onDeleteStudent(arg0);
			}
		});
		
		viewHolder.secondName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mDeleteStudent.onDeleteStudent(arg0);
			}
		});
		
		viewHolder.incorrectTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext).setTitle("InCorrect Time Option").setItems(new String[]{
						"INCREASE","DECREASE","RESET TO ZERO"
				}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialogInterface, int whitch) {
						// TODO Auto-generated method stub
						int time = beans.get(arg0).getIncorrectTime();
						switch (whitch) {
						case 0:
							time++;
							viewHolder.incorrectTime.setText("" + time);
							beans.get(arg0).setIncorrectTime(time);
							viewHolder.correctRate.setText(beans.get(arg0).getCorrectRate());
							break;
							
						case 1:
							time--;
							if (time < 0) {
								time = 0;
							}
							viewHolder.incorrectTime.setText("" + time);
							beans.get(arg0).setIncorrectTime(time);
							viewHolder.correctRate.setText(beans.get(arg0).getCorrectRate());
							break;
						
						case 2:
							time = 0;
							viewHolder.incorrectTime.setText("" + time);
							beans.get(arg0).setIncorrectTime(time);
							viewHolder.correctRate.setText(beans.get(arg0).getCorrectRate());
							break;
						}
					}


				}).show();
			}
		});
		return arg1;
	}
	
	public class ViewHolder{
		private ImageView headImage;
		private TextView firstName;
		private TextView secondName;
		private TextView correctRate;
		private ImageView attendance;
		private TextView callTime;
		private TextView correctTime;
		private TextView incorrectTime;
	}

}
