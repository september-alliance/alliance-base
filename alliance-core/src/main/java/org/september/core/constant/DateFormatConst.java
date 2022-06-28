package org.september.core.constant;

import java.text.SimpleDateFormat;

public interface DateFormatConst {

	public final static SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
	public final static SimpleDateFormat yyyy_MM = new SimpleDateFormat("yyyy-MM");
	public final static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat HH_mm_ss = new SimpleDateFormat("HH:mm:ss");
	public final static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public final static SimpleDateFormat yyyy_MM_dd_Zn_Ch = new SimpleDateFormat("yyyy年MM月dd日");
	
	public final static SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
	
}
