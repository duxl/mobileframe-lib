package com.duxl.mobileframe.util;

import com.duxl.mobileframe.MobileFrameApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化数据保存到文件
 * @param <T>
 */
public class SerializableDataUtil<T extends Serializable> {

	public void delSerializableObj(String key) {
		String path = MobileFrameApp.getInstance().getCacheDir().getAbsolutePath() + "/"+key;
		new File(path).delete();
	}
	
	public void serializableObj(T object, String key) {
		ObjectOutputStream oos = null;
		String path = MobileFrameApp.getInstance().getCacheDir().getAbsolutePath() + "/"+key;
		File file = new File(path);
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public T unSerializableObj(String key) {
		ObjectInputStream ois = null;
		T obj = null;
		try {
			String path = MobileFrameApp.getInstance().getCacheDir().getAbsolutePath() + "/"+key;
			File file = new File(path);
			if(file.exists()) {
				ois = new ObjectInputStream(new FileInputStream(file));
				obj = (T) ois.readObject();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
}
