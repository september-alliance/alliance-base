package org.september.smartdao.common;

import java.util.Date;

import org.september.core.component.log.LogHelper;
import org.september.core.constant.enums.DeleteFlag;
import org.september.smartdao.CommonDao;
import org.september.smartdao.CommonDaoHolder;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

	protected final LogHelper logger = LogHelper.getLogger(this.getClass());
	
    public void save(BaseEntity entity) {
        entity.setAddtime(new Date());
        entity.setDeleteFlag(DeleteFlag.NotDelete.ordinal());
        getCommonDao().save(entity);
    }
    
    public void save(BaseEntity entity,String tableName) {
        entity.setAddtime(new Date());
        entity.setDeleteFlag(DeleteFlag.NotDelete.ordinal());
        getCommonDao().save(entity,tableName);
    }
    
    public void update(BaseEntity entity) {
        entity.setUpdateTime(new Date());
        getCommonDao().update(entity);
    }
    
    public void updateWithNullFields(BaseEntity entity) {
        entity.setUpdateTime(new Date());
        getCommonDao().updateWithNullFields(entity);
    }

    public CommonDao getCommonDao() {
        return CommonDaoHolder.getCommonDao();
    }

    public void delete(BaseEntity po) {
        po.setDeleteFlag(DeleteFlag.Deleted.ordinal());
        getCommonDao().update(po);
    }
    /**
     * 物理删除
     */
    public void deleteForPhysical(BaseEntity po){
    	getCommonDao().delete(po);
    }
    
}
