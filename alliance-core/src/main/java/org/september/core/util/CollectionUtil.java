package org.september.core.util;

import java.util.List;

public class CollectionUtil {

	public static boolean containsOne(List<?> coll, Object[] targets){
		if(targets==null){
			return false;
		}
		if(coll==null){
			return false;
		}
		for(int i=0;i<targets.length;i++){
			if(coll.contains(targets[i])){
				return true;
			}
		}
		return false;
	}
}
