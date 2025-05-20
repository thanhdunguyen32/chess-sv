package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.dao.GsDao;
import cc.mrbird.febs.system.entity.GsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GsService {

    @Autowired
    private GsDao gsDao;

    public Collection<GsEntity> getValidGsEntity() {
        Iterable<GsEntity> gsAll = gsDao.getGsAll();
        Map<String,GsEntity> gsMap = new HashMap<>();
        for(GsEntity aGsEntity : gsAll) {
            gsMap.put(aGsEntity.getHost()+":"+aGsEntity.getPort(), aGsEntity);
        }
        return gsMap.values();
    }

    public List<GsEntity> getGsList(){
        return gsDao.getGsAll();
    }

    public GsEntity getGsById(int gs_id){
        return gsDao.getGsById(gs_id);
    }

}
