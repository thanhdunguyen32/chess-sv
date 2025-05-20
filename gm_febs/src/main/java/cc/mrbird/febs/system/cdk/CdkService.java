package cc.mrbird.febs.system.cdk;

import cc.mrbird.febs.system.dao.CdkDaoDb;
import cc.mrbird.febs.system.entity.Cdk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

//Spring Bean的标识.
@Component
public class CdkService {

	@Autowired
	private CdkDaoDb cdkDaoDb;

	public void batchSaveCdk(Collection<Cdk> cdkAll) {
		cdkDaoDb.batchAddCdkey(cdkAll);
	}

	public CdkDaoDb getCdkDaoDb() {
		return cdkDaoDb;
	}

	public void setCdkDaoDb(CdkDaoDb cdkDaoDb) {
		this.cdkDaoDb = cdkDaoDb;
	}

	public int getCdkIdFromDb() {
		return cdkDaoDb.getCdkIdFromDb();
	}

}
